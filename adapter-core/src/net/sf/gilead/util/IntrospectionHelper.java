package net.sf.gilead.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Static helper for instrospection
 * @author bruno.marchesson
 *
 */
public class IntrospectionHelper
{
	//-------------------------------------------------------------------------
	//
	// Static helper
	//
	//-------------------------------------------------------------------------
	/**
	 * Recursively get declared fields
	 */
	public static Field[] getRecursiveDeclaredFields(Class<?> clazz)
	{
	//	Create field list
	//
		List<Field> fieldList = new ArrayList<Field>();
		
	//	Recursive get superclass declared fields
	//
		while(clazz != null)
		{
			fieldList.addAll(Arrays.asList(clazz.getDeclaredFields()));
			clazz = clazz.getSuperclass();
		}
	
	//	Convert field list to array
	//
		return fieldList.toArray(new Field[fieldList.size()]);
	}
	
	/**
	 * Recursively find declared method with the argument name
	 */
	public static Method getRecursiveDeclaredMethod(Class<?> clazz, String methodName,
													 Class<?>... parameterTypes) 
													throws NoSuchMethodException
	{
	//	Recursive get superclass declared fields
	//
		while(clazz != null)
		{
			try
			{
				return clazz.getDeclaredMethod(methodName, parameterTypes);
			}
			catch(NoSuchMethodException ex)
			{
			//	Search in superclass
			//
				clazz = clazz.getSuperclass();
			}
		}
	
	//	Method not found
	//
		throw new NoSuchMethodException(methodName);
	}
}
