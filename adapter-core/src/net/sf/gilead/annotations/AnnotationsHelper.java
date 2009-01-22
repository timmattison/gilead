package net.sf.gilead.annotations;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.gilead.util.IntrospectionHelper;

/**
 * Helper class for annotation management
 * @author bruno.marchesson
 *
 */
public class AnnotationsHelper
{
	//----
	// Attributes
	//----
	/**
	 * Log channel
	 */
	private static Log _log = LogFactory.getLog(AnnotationsHelper.class);
	
	/**
	 * Annotation map.
	 * It is filled with associated Gilead annotation for all classes and properties 
	 * for performance purpose (computing it each time is very expensive)
	 */
	private static Map<Class<?>, Map<String, Class<?>>> _annotationMap = new HashMap<Class<?>, Map<String,Class<?>>>();
	
	//-------------------------------------------------------------------------
	//
	// Static helpers
	//
	//-------------------------------------------------------------------------
	/**
	 * Indicated if the argument has "ServerOnly" annotation on one of its field.
	 */
	public static boolean hasServerOnlyAnnotations(Class<?> clazz)
	{
	//	Map checking
	//
		Map<String, Class<?>> propertyAnnotations = _annotationMap.get(clazz);
		if (propertyAnnotations == null)
		{
		//	Compute property annotations
		//
			propertyAnnotations = getGileadAnnotations(clazz);
			synchronized (_annotationMap)
			{
				_annotationMap.put(clazz, propertyAnnotations);
			}
		}
		
	//	Does the map contains @ServerOnly annotation ?
	//
		return propertyAnnotations.containsValue(ServerOnly.class);
	}
	
	/**
	 * Indicated if the argument has "ServerOnly" annotation on the named field.
	 */
	public static boolean isServerOnly(Class<?> clazz, String propertyName)
	{
	//	Map checking
	//
		Map<String, Class<?>> propertyAnnotations = _annotationMap.get(clazz);
		if (propertyAnnotations == null)
		{
		//	Compute property annotations
		//
			propertyAnnotations = getGileadAnnotations(clazz);
			synchronized (_annotationMap)
			{
				_annotationMap.put(clazz, propertyAnnotations);
			}
		}
		
	//	Does the map contains @ServerOnly annotation for the argument property ?
	//
		Class<?> annotation  = propertyAnnotations.get(propertyName);
		return ((annotation != null) && (ServerOnly.class.equals(annotation)));
	}
	
	/**
	 * Indicated if the argument has "ServerOnly" annotation on one of its field.
	 */
	public static boolean hasServerOnlyOrReadOnlyAnnotations(Class<?> clazz)
	{
	//	Map checking
	//
		Map<String, Class<?>> propertyAnnotations = _annotationMap.get(clazz);
		if (propertyAnnotations == null)
		{
		//	Compute property annotations
		//
			propertyAnnotations = getGileadAnnotations(clazz);
			synchronized (_annotationMap)
			{
				_annotationMap.put(clazz, propertyAnnotations);
			}
		}
		
	//	Does the map contains @ServerOnly or @ReadOnly annotation ?
	//
		return ((propertyAnnotations.containsValue(ServerOnly.class)) ||
				(propertyAnnotations.containsValue(ReadOnly.class)));
	}
	
	/**
	 * Indicated if the argument has "ReadOnly" annotation on one of its field.
	 */
	public static boolean hasReadOnlyAnnotations(Class<?> clazz)
	{
	//	Map checking
	//
		Map<String, Class<?>> propertyAnnotations = _annotationMap.get(clazz);
		if (propertyAnnotations == null)
		{
		//	Compute property annotations
		//
			propertyAnnotations = getGileadAnnotations(clazz);
			synchronized (_annotationMap)
			{
				_annotationMap.put(clazz, propertyAnnotations);
			}
		}
		
	//	Does the map contains @ServerOnly annotation ?
	//
		return propertyAnnotations.containsValue(ReadOnly.class);
	}
	
	/**
	 * Indicated if the argument has "ReadOnly" annotation on the named field.
	 */
	public static boolean isReadOnly(Class<?> clazz, String propertyName)
	{
	//	Map checking
	//
		Map<String, Class<?>> propertyAnnotations = _annotationMap.get(clazz);
		if (propertyAnnotations == null)
		{
		//	Compute property annotations
		//
			propertyAnnotations = getGileadAnnotations(clazz);
			synchronized (_annotationMap)
			{
				_annotationMap.put(clazz, propertyAnnotations);
			}
		}
		
	//	Does the map contains @ReadOnly annotation for the argument property ?
	//
		Class<?> annotation  = propertyAnnotations.get(propertyName);
		return ((annotation != null) && (ReadOnly.class.equals(annotation)));
	}
	
	//-------------------------------------------------------------------------
	//
	// Internal methods
	//
	//--------------------------------------------------------------------------
	/**
	 * Indicated if the argument has "ReadOnly" annotation on one of its field.
	 */
	private static Map<String,Class<?>> getGileadAnnotations(Class<?> clazz)
	{
		if (_log.isDebugEnabled())
		{
			_log.debug("Looking for Gilead annotations for " + clazz);
		}
		
		Map<String, Class<?>> result = new HashMap<String, Class<?>>();
		
	//	Search annotations on fields
	//
		Field[] fields = IntrospectionHelper.getRecursiveDeclaredFields(clazz);
		for (Field field : fields)
		{
			String propertyName = field.getName();
			if (field.isAnnotationPresent(ReadOnly.class))
			{
				if (_log.isDebugEnabled())
				{
					_log.debug(propertyName + " member has @ReadOnly");
				}
				result.put(propertyName, ReadOnly.class);
			}
			else if (field.isAnnotationPresent(ServerOnly.class))
			{
				if (_log.isDebugEnabled())
				{
					_log.debug(propertyName + " member has @ServerOnly");
				}
				result.put(propertyName, ServerOnly.class);
			}
			else
			{
				if (_log.isTraceEnabled())
				{
					_log.trace(propertyName + " member has no Gilead annotation");
				}
				
				result.put(propertyName, null);
			}
		}
		
	//	Search annotation on getters
	//
		try 
		{
			BeanInfo info = Introspector.getBeanInfo(clazz);
			PropertyDescriptor[] descriptors = info.getPropertyDescriptors();
			for (int index = 0; index < descriptors.length; index++)
			{
				PropertyDescriptor descriptor = descriptors[index];
				if ((descriptor != null) && (descriptor.getReadMethod() != null))
				{
					String propertyName = descriptor.getName();
					if (descriptor.getReadMethod().isAnnotationPresent(ReadOnly.class))
					{
						if (_log.isDebugEnabled())
						{
							_log.debug(propertyName + " getter has @ReadOnly");
						}
						result.put(propertyName, ReadOnly.class);
					}
					else if (descriptor.getReadMethod().isAnnotationPresent(ServerOnly.class))
					{
						if (_log.isDebugEnabled())
						{
							_log.debug(propertyName + " getter has @ServerOnly");
						}
						result.put(propertyName, ServerOnly.class);
					}
				}
			}
		} 
		catch (IntrospectionException e)
		{
			throw new RuntimeException("Cannot inspect class " + clazz, e);
		}
		
	//	Return annotation map
	//
		return result;
	}
	
}
