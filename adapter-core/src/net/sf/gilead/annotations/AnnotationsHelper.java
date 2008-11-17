package net.sf.gilead.annotations;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;

import net.sf.gilead.util.IntrospectionHelper;

/**
 * Helper class for annotation management
 * @author bruno.marchesson
 *
 */
public class AnnotationsHelper
{
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
	//	Search annotations on fields
	//
		Field[] fields = IntrospectionHelper.getRecursiveDeclaredFields(clazz);
		for (Field field : fields)
		{
			if (field.isAnnotationPresent(ServerOnly.class))
			{
				return true;
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
					if (descriptor.getReadMethod().isAnnotationPresent(ServerOnly.class))
					{
						return true;
					}
				}
			}
		} 
		catch (IntrospectionException e)
		{
			throw new RuntimeException("Cannot inspect class " + clazz, e);
		}
		
	//	Annotation not found
	//
		return false;
	}
	
	/**
	 * Indicated if the argument has "ServerOnly" annotation on the named field.
	 */
	public static boolean isServerOnly(Class<?> clazz, String propertyName)
	{
	//	Search annotations on fields
	//
		try
		{
			Field field = clazz.getDeclaredField(propertyName);
			if ((field != null) &&
				(field.isAnnotationPresent(ServerOnly.class)))
			{
				return true;
			}
		}
		catch (NoSuchFieldException e)
		{
			// search property instead
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
					if ((descriptor.getName().equals(propertyName)) &&
						(descriptor.getReadMethod().isAnnotationPresent(ServerOnly.class)))
					{
						return true;
					}
				}
			}
		} 
		catch (IntrospectionException e)
		{
			throw new RuntimeException("Cannot inspect class " + clazz, e);
		}
		
	//	Annotation not found
	//
		return false;
	}
	
	/**
	 * Indicated if the argument has "ServerOnly" annotation on one of its field.
	 */
	public static boolean hasServerOnlyOrReadOnlyAnnotations(Class<?> clazz)
	{
	//	Search annotations on fields
	//
		Field[] fields = IntrospectionHelper.getRecursiveDeclaredFields(clazz);
		for (Field field : fields)
		{
			if (field.isAnnotationPresent(ServerOnly.class) ||
				field.isAnnotationPresent(ReadOnly.class))
			{
				return true;
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
					if ((descriptor.getReadMethod().isAnnotationPresent(ServerOnly.class)) ||
						(descriptor.getReadMethod().isAnnotationPresent(ReadOnly.class)))
					{
						return true;
					}
				}
			}
		} 
		catch (IntrospectionException e)
		{
			throw new RuntimeException("Cannot inspect class " + clazz, e);
		}
		
	//	Annotation not found
	//
		return false;
	}
	
	/**
	 * Indicated if the argument has "ReadOnly" annotation on one of its field.
	 */
	public static boolean hasReadOnlyAnnotations(Class<?> clazz)
	{
	//	Search annotations on fields
	//
		Field[] fields = IntrospectionHelper.getRecursiveDeclaredFields(clazz);
		for (Field field : fields)
		{
			if (field.isAnnotationPresent(ReadOnly.class))
			{
				return true;
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
					if (descriptor.getReadMethod().isAnnotationPresent(ReadOnly.class))
					{
						return true;
					}
				}
			}
		} 
		catch (IntrospectionException e)
		{
			throw new RuntimeException("Cannot inspect class " + clazz, e);
		}
		
	//	Annotation not found
	//
		return false;
	}
	
	/**
	 * Indicated if the argument has "ReadOnly" annotation on the named field.
	 */
	public static boolean isReadOnly(Class<?> clazz, String propertyName)
	{
	//	Search annotations on fields
	//
		try
		{
			Field field = clazz.getDeclaredField(propertyName);
			if ((field != null) &&
				(field.isAnnotationPresent(ReadOnly.class)))
			{
				return true;
			}
		}
		catch (NoSuchFieldException e)
		{
			// search property instead
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
					if ((descriptor.getName().equals(propertyName)) &&
						(descriptor.getReadMethod().isAnnotationPresent(ReadOnly.class)))
					{
						return true;
					}
				}
			}
		} 
		catch (IntrospectionException e)
		{
			throw new RuntimeException("Cannot inspect class " + clazz, e);
		}
		
	//	Annotation not found
	//
		return false;
	}
}
