/**
 * 
 */
package net.sf.gilead.adapter4appengine.datanucleus;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.jdo.annotations.PrimaryKey;
import javax.jdo.spi.PersistenceCapable;
import javax.persistence.Id;

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
	 * Logger channel
	 */
	private static final Logger log = Logger.getLogger(JdoReadSerializationFilter.class.getName());
	
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
		//	Cannot use jdoGetObjectId because it seems based on jdoDetachedState !
		//
			Object id = getId(obj, fieldValues);
			if (id == null)
			{
				// new item : nothing to do ?
				//
				return;
			}
			
			Object stored = JdoEntityStore.getInstance().getEntity(id);
			
			restoreJdoDetachedState(obj, stored);
			
		//	Call setter on each value
		//
			for(Entry<String, Object> fieldValue : fieldValues.entrySet())
			{
				String fieldName = fieldValue.getKey();
				Object value = fieldValue.getValue();
				if (value instanceof Collection)
				{
				//	TODO Collection handling
				//
					updateCollection(obj, fieldName, (Collection<Object>) value, stored);
				}
				else
				{
				//	Simple value
				//
					log.info("Setting value " + value
							+" for field " + fieldName);
					
					Method setter = getSetterFor(obj, fieldName);
					if (setter != null)
					{
					//	Set new value
					//
						try 
						{
							setter.invoke(obj, value);
						} 
						catch (Exception e)
						{
							throw new RuntimeException(e);
						}
					}
				}
			}
			
		//	Restore stored Id if needed
		//
			restoreStoredId(obj, stored);
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
	 * Returns a Field object that reflects the primary key of the class or 
	 * interface represented by this Class object. 
	 * 
	 */
	protected Field getDeclaredId(Object obj)
	{
		Class<?> theClass = obj.getClass();
		while( theClass != null )
		{
			Field[] fields = theClass.getDeclaredFields();
			for( Field field : fields )
			{
				if( (field.getAnnotation( PrimaryKey.class ) != null)
						|| (field.getAnnotation( Id.class ) != null) )
				{
					// Id field
					//
					return field;
				}
			}
			// Id field not found in base class: take a look in super class
			theClass = theClass.getSuperclass();
		}

		throw new RuntimeException( "Id field not found for class " + obj.getClass().getName() );
	}

	/**
	 * @return the ID of the argument persistent ID
	 */
	protected Object getId(Object obj, Map<String, Object> fieldValues)
	{
		Field field = getDeclaredId( obj );
		return fieldValues.get( field.getName() );
	}
	


	/**
	 * Get stored detached state for the input obj and its associated id
	 * @param obj
	 * @param id
	 * @return
	 */
	protected void restoreJdoDetachedState(Object obj, Object stored)
	{
		try
		{
		//	Copy all JDO fields
		//
			Field[] fields = obj.getClass().getDeclaredFields();
			for (Field field : fields)
			{
				if ((field.getName().startsWith("jdo")) &&
					(Modifier.isStatic(field.getModifiers()) == false))
				{
					field.setAccessible(true);
					Object jdoValue = field.get(stored);
					field.set(obj, jdoValue);
				}
			}
		}
		catch(Exception ex)
		{
			throw new RuntimeException(ex);
		}
	}
	
	/**
	 * Restore stored ID (for Key handling)
	 * @param obj
	 * @param stored
	 */
	protected void restoreStoredId(Object obj, Object stored)
	{
		try
		{
			Field idField = getDeclaredId( obj );
			idField.setAccessible(true);
			
			// Get stored ID
			Object value = idField.get(stored);
			
			// Set stored id
			idField.set(obj, value);
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
				(method.getParameterTypes().length == 1))
			{
				method.setAccessible(true);
				return method;
			}
		}
		
	//	Not found
	//
		log.warning("No setter for " + fieldName);
		return null;
	}
	
	/**
	 * Update collection field.
	 * This method is based on previously stored collection so persistence marker on 
	 * DN implementation will be updated correctly.
	 * @param obj
	 * @param fieldName
	 * @param value
	 * @param stored
	 */
	protected void updateCollection(Object obj, String fieldName, 
									Collection<Object> values, Object stored)
	{
		try
		{
		//	Get stored collection
		//
			Field field = stored.getClass().getDeclaredField(fieldName);
			assert(field != null);
			field.setAccessible(true);
			
			Collection<Object> storedCollection = (Collection<Object>) field.get(stored);
			if (storedCollection == null)
			{
				// No collection before sending : nothing to do
				return;
			}
			
		//	TODO stored collection update to be improved
		//
			storedCollection.clear();
			storedCollection.addAll(values);
			
		//	Update the deserialized object with stored DataNucleus collection
		//
			field.set(obj, storedCollection);
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
	}
}
