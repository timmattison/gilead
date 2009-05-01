/**
 * 
 */
package net.sf.gilead.adapter4appengine.datanucleus;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.Map.Entry;

import javax.jdo.spi.PersistenceCapable;

import com.google.appengine.repackaged.org.apache.commons.logging.Log;
import com.google.appengine.repackaged.org.apache.commons.logging.LogFactory;
import com.google.gwt.user.server.rpc.ISerializationFilter;

/**
 * Serialization filter for populating DataNucleus entity JDO fields.
 * @author bruno.marchesson
 *
 */
public class JdoReadSerializationFilter implements ISerializationFilter
{
	//----
	// Attributes
	//----
	/**
	 * Log channel
	 */
	private static Log log = LogFactory.getLog(JdoReadSerializationFilter.class);
	
	/**
	 * Stack for field values. They while be set back to DataNucleus entity after
	 * deserialization
	 */
	private Stack<Map<String, Object>> fieldValuesStack;
	
	//-------------------------------------------------------------------------
	//
	// Constructor
	//
	//-------------------------------------------------------------------------
	/**
	 * Empty constructor
	 */
	public JdoReadSerializationFilter()
	{
		fieldValuesStack = new Stack<Map<String, Object>>();
	}
	
	//-------------------------------------------------------------------------
	//
	// ISerializationFilter
	//
	//-------------------------------------------------------------------------
	/* (non-Javadoc)
	 * @see com.google.gwt.user.server.rpc.ISerializationAdapter#beforeSerialization(java.lang.Object)
	 */
	public void beforeSerialization(Object obj)
	{
		if (obj instanceof PersistenceCapable)
		{
		//	Start a new entity
		//
			fieldValuesStack.add(new HashMap<String, Object>());
		}
	}


	/* (non-Javadoc)
	 * @see com.google.gwt.user.server.rpc.ISerializationAdapter#afterSerialization(java.lang.Object)
	 */
	public void afterSerialization(Object obj)
	{
		if (obj instanceof PersistenceCapable)
		{
		//	Get field values
		//
			Map<String, Object> fieldValues = fieldValuesStack.pop();
			
		//	Restore JDO detached state
		//
			Object id = getId(obj, fieldValues);
			if (id != null)
			{
				restoreJdoDetachedState(obj, id);
			}
			
		//	Call setter on each value
		//
			for(Entry<String, Object> fieldValue : fieldValues.entrySet())
			{
				log.info("Setting value " + fieldValue.getValue() 
						+" for field " + fieldValue.getKey());
				
				Method setter = getSetterFor(obj, fieldValue.getKey());
				if (setter != null)
				{
				//	Set new value
				//
					try 
					{
						setter.invoke(obj, fieldValue.getValue());
					} 
					catch (Exception e)
					{
						throw new RuntimeException(e);
					}
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.user.server.rpc.ISerializationAdapter#shouldSerialize(java.lang.Class, java.lang.String)
	 */
	public boolean shouldSerialize(Class<?> clazz, String fieldName)
	{
		return (fieldName.startsWith("jdo") == false);
	}
	

	/* (non-Javadoc)
	 * @see com.google.gwt.user.server.rpc.ISerializationAdapter#shouldSerialize(java.lang.Object, java.lang.String, java.lang.Object)
	 */
	public boolean shouldSerialize(Object obj, String fieldName, Object value)
	{
	//	Exclude "jdo" fields
	//
		if (fieldName.startsWith("jdo"))
		{
			return false;
		}
		
	//	Store field value if needed
	//
		if (obj instanceof PersistenceCapable)
		{
			fieldValuesStack.peek().put(fieldName, value);
		}
		return true;
	}
	
	//-------------------------------------------------------------------------
	//
	// Private methods
	//
	//-------------------------------------------------------------------------
	/**
	 * @return the ID of the argument persistent ID
	 */
	protected Object getId(Object obj, Map<String, Object> fieldValues)
	{
		// TODO get ID field name (stored in LightEntity ????)
		return fieldValues.get("id");
	}
	
	/**
	 * Get stored detached state for the input obj and its associated id
	 * @param obj
	 * @param id
	 * @return
	 */
	protected void restoreJdoDetachedState(Object obj, Object id)
	{
		try
		{
			PersistenceCapable stored = JdoEntityStore.getInstance().getEntity(id);
			
		//	Copy JDO detached state
		//
			Field field = obj.getClass().getDeclaredField("jdoDetachedState");
			field.setAccessible(true);
			
			Object[] detachedState = (Object[]) field.get(stored);
			
			field.set(obj, detachedState);
		}
		catch(Exception ex)
		{
			throw new RuntimeException(ex);
		}
	}
	
	/**
	 * Get setter method for the argument field
	 * @param obj
	 * @param fieldName
	 * @return
	 */
	protected Method getSetterFor(Object obj, String fieldName)
	{
	//	Compute setter name
	//
		StringBuilder setter = new StringBuilder("set");
		setter.append(fieldName.substring(0, 1).toUpperCase());
		setter.append(fieldName.substring(1));
		String setterName = setter.toString();
		
	//	Get setter method
	//
		Method[] methods = obj.getClass().getDeclaredMethods();
		for (Method method : methods)
		{
			if ((setterName.equals(method.getName())) &&
				(method.getParameterTypes().length == 0))
			{
				method.setAccessible(true);
				return method;
			}
		}
		
	//	Not found
	//
		log.warn("No setter for " + fieldName);
		return null;
	}
}
