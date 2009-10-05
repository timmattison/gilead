package net.sf.gilead.annotations;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import net.sf.gilead.util.IntrospectionHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	 * Logger channel
	 */
	private static Logger _log = LoggerFactory.getLogger(AnnotationsHelper.class);
	
	/**
	 * Annotation map.
	 * It is filled with associated Gilead annotation for all classes and properties 
	 * for performance purpose (computing it each time is very expensive)
	 */
	private static Map<Class<?>, Map<String, GileadAnnotation>> _annotationMap = new HashMap<Class<?>, Map<String,GileadAnnotation>>();
	
	/**
	 * The access manager map, since they are singleton.
	 */
	private static Map<Class<? extends IAccessManager>, IAccessManager> _accessManagerMap = new HashMap<Class<? extends IAccessManager>, IAccessManager>(); 
	
	//-------------------------------------------------------------------------
	//
	// Static helpers
	//
	//-------------------------------------------------------------------------
	/**
	 * Indicated if the argument has "ServerOnly" active annotation on the named field.
	 */
	public static boolean isServerOnly(Object entity, String propertyName)
	{
		return isAnnotedProperty(entity, propertyName, ServerOnly.class);
	}
	
	/**
	 * Indicated if the argument has "ReadOnly" active annotation on the named field.
	 */
	public static boolean isReadOnly(Object entity, String propertyName)
	{
		return isAnnotedProperty(entity, propertyName, ReadOnly.class);
	}
	
	/**
	 * Indicated if the argument has "ServerOnly" or "ReadOnly" active annotation on the named field.
	 */
	public static boolean isServerOrReadOnly(Object entity, String propertyName)
	{
		return isAnnotedProperty(entity, propertyName, null);
	}
	
	/**
	 * Indicated if the argument entity has "ServerOnly" active annotation on one of its field.
	 */
	public static boolean hasServerOnlyAnnotations(Class<?> entityClass)
	{
		return isAnnotedClass(entityClass, ServerOnly.class);
	}
	
	/**
	 * Indicated if the argument has "ServerOnly" annotation on one of its field.
	 */
	public static boolean hasServerOnlyOrReadOnlyAnnotations(Class<?> entityClass)
	{
	//	Search all Gilead annotations
	//
		return isAnnotedClass(entityClass, null);
	}
	
	/**
	 * Indicated if the argument has "ReadOnly" annotation on one of its field.
	 */
	public static boolean hasReadOnlyAnnotations(Class<?> entityClass)
	{
		return isAnnotedClass(entityClass, ReadOnly.class);
	}
	
	/**
	 * Gets the singleton for the argument access manager class.
	 * @param managerClass the access manager class
	 * @return the singleton
	 */
	public synchronized static IAccessManager getAccessManager(Class<? extends IAccessManager> managerClass)
	{
		IAccessManager accessManager = _accessManagerMap.get(managerClass);
		if (accessManager == null)
		{
		//	Create it
		//
			try
			{
				accessManager = managerClass.newInstance();
				_accessManagerMap.put(managerClass, accessManager);
			}
			catch(Exception ex)
			{
				throw new RuntimeException("Error creating Access Manager", ex);
			}
		}
		
		return accessManager;
	}
	//-------------------------------------------------------------------------
	//
	// Internal methods
	//
	//--------------------------------------------------------------------------
	/**
	 * Indicated if the argument has "ReadOnly" annotation on one of its field.
	 */
	private static Map<String, GileadAnnotation> getGileadAnnotations(Class<?> clazz)
	{
		if (_log.isDebugEnabled())
		{
			_log.debug("Looking for Gilead annotations for " + clazz);
		}
		
		Map<String, GileadAnnotation> result = new HashMap<String, GileadAnnotation>();
		
	//	Search annotations on fields
	//
		try 
		{
			Field[] fields = IntrospectionHelper.getRecursiveDeclaredFields(clazz);
			for (Field field : fields)
			{
				String propertyName = field.getName();
				
				// ReadOnly
				ReadOnly readOnly = field.getAnnotation(ReadOnly.class);
				if (readOnly != null)
				{
					if (_log.isDebugEnabled())
					{
						_log.debug(propertyName + " member has @ReadOnly");
					}
					IAccessManager accessManager = getAccessManager(readOnly.accessManager());
					result.put(propertyName, new GileadAnnotation(ReadOnly.class, accessManager));
					continue;
				}
				
				// ServerOnly
				ServerOnly serverOnly = field.getAnnotation(ServerOnly.class);
				if (serverOnly != null)
				{
					if (_log.isDebugEnabled())
					{
						_log.debug(propertyName + " member has @ServerOnly");
					}
					IAccessManager accessManager = getAccessManager(serverOnly.accessManager());
					result.put(propertyName, new GileadAnnotation(ServerOnly.class, accessManager));
					continue;
				}
				
				// No Gilead annotation
				if (_log.isTraceEnabled())
				{
					_log.trace(propertyName + " member has no Gilead annotation");
				}
				
				result.put(propertyName, null);
			}
			
	//	Search annotation on getters
	//
			BeanInfo info = Introspector.getBeanInfo(clazz);
			PropertyDescriptor[] descriptors = info.getPropertyDescriptors();
			for (int index = 0; index < descriptors.length; index++)
			{
				PropertyDescriptor descriptor = descriptors[index];
				if ((descriptor != null) && (descriptor.getReadMethod() != null))
				{
					String propertyName = descriptor.getName();
					
				//	If annotation has been detected on field, no need to search on getter
				//
					if (result.get(propertyName) != null)
					{
						continue;
					}
					
				//	ReadOnly
				//
					ReadOnly readOnly = descriptor.getReadMethod().getAnnotation(ReadOnly.class); 
					if (readOnly != null)
					{
						if (_log.isDebugEnabled())
						{
							_log.debug(propertyName + " getter has @ReadOnly");
						}
						IAccessManager accessManager = getAccessManager(readOnly.accessManager());
						result.put(propertyName, new GileadAnnotation(ReadOnly.class, accessManager));
						continue;
					}
					
					ServerOnly serverOnly = descriptor.getReadMethod().getAnnotation(ServerOnly.class); 
					if (serverOnly != null)
					{
						if (_log.isDebugEnabled())
						{
							_log.debug(propertyName + " getter has @ServerOnly");
						}
						IAccessManager accessManager = getAccessManager(serverOnly.accessManager());
						result.put(propertyName, new GileadAnnotation(ServerOnly.class, accessManager));
					}
				}
			}
		} 
		catch (Exception e)
		{
			throw new RuntimeException("Error inspecting class " + clazz, e);
		}
		
	//	Return annotation map
	//
		return result;
	}
	
	/**
	 * Indicated if the argument has the target Gilead annotation on the named field.
	 */
	private static boolean isAnnotedProperty(Object entity, String propertyName, Class<?> annotationClass)
	{
	//	Map checking
	//
		Class<?> clazz = entity.getClass();
		Map<String, GileadAnnotation> propertyAnnotations = _annotationMap.get(clazz);
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
		
	//	Does the map contains the target annotation for the argument property ?
	//
		GileadAnnotation annotation = propertyAnnotations.get(propertyName); 
		if (annotation == null)
		{
			return false;
		}
		if ((annotationClass != null) &&
			(annotationClass.equals(annotation.annotationClass) == false))
		{
			return false;
		}
		
	// 	Check access manager
	//
		if (annotationClass != null)
		{
			return annotation.accessManager.isAnnotationActive(annotationClass, entity, propertyName);
		}
		else
		{
		//	Check access for every Gilead annotation
		//
			return (annotation.accessManager.isAnnotationActive(ServerOnly.class, entity, propertyName) ||
					annotation.accessManager.isAnnotationActive(ReadOnly.class, entity, propertyName));
		}
	}
	
	/**
	 * Indicated if the argument entity has "ServerOnly" active annotation on one of its field.
	 */
	private static boolean isAnnotedClass(Class<?> entityClass, Class<?> annotationClass)
	{
	//	Map checking
	//
		Map<String, GileadAnnotation> propertyAnnotations = _annotationMap.get(entityClass);
		if (propertyAnnotations == null)
		{
		//	Compute property annotations
		//
			propertyAnnotations = getGileadAnnotations(entityClass);
			synchronized (_annotationMap)
			{
				_annotationMap.put(entityClass, propertyAnnotations);
			}
		}
		
	//	Does the map contains @ServerOnly annotation ?
	//
		if (propertyAnnotations != null)
		{
			for (Map.Entry<String, GileadAnnotation> entry : propertyAnnotations.entrySet())
			{
				GileadAnnotation annotation = entry.getValue();
				if (annotation != null)
				{
					if ((annotationClass == null) ||
						(annotationClass.equals(annotation.annotationClass) == true))
					{
					//	Found 
					//
						return true;
					}
				}
			}
		}
		return false;
	}
}

/**
 * Structure for Gilead annotation
 * @author bruno.marchesson
 *
 */
class GileadAnnotation
{
	//----
	// Fields
	//----
	/**
	 * The associated annotation.
	 */
	public Class<?> annotationClass;
	
	/**
	 * The annotation access manager.
	 */
	public IAccessManager accessManager;
	
	//----
	// Constructors
	//----
	/**
	 * Empty constructor
	 */
	public GileadAnnotation() 
	{
	}

	/**
	 * Complete constructor
	 * @param annotation
	 * @param accessManager
	 */
	public GileadAnnotation(Class<?> annotationClass, IAccessManager accessManager) 
	{
		this.annotationClass = annotationClass;
		this.accessManager = accessManager;
	}
}