package net.sf.gilead.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
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
	
	
	/**
	 * Search nested class from the attributes of the argument
	 * @param clazz the searched class
	 * @param root the root object
	 * @return the object if found, null otherwise
	 */
	public static Object searchMember(Class<?> clazz, Object root)
	{
		return searchMember(clazz, root, new ArrayList<Object>());
	}
	
	//-------------------------------------------------------------------------
	//
	// Internal method
	//
	//-------------------------------------------------------------------------
	/**
	 * Search nested class from the attributes of the argument
	 * @param clazz the searched class
	 * @param root the root object
	 * @return the object if found, null otherwise
	 */
	private static Object searchMember(Class<?> clazz, Object root, List<Object> alreadyChecked)
	{
	//	Precondition checking
	//
		if ((root == null) ||
			(root.getClass().getName().startsWith("java.")))
		{
			return null;
		}
		
	//	Already checked ?
	//
		if (alreadyChecked.contains(root) == true)
		{
			return null;
		}
		alreadyChecked.add(root);
		
		if (clazz.isInstance(root))
		{
			return root;
		}
		
	//	Iterate over fields
	//
		Field[] fields = getRecursiveDeclaredFields(root.getClass());
		for (Field field : fields)
		{
		//	Recursive search
		//
			field.setAccessible(true);
			try
			{
				Object member = searchMember(clazz, field.get(root), alreadyChecked);
				if (member != null)
				{
					return member;
				}
			}
			catch (Exception e)
			{
			//	Should not happen
			//
				throw new RuntimeException(e);
			}
		}
		
	//	Final verification : is the current class is a proxy ?
	//
		if (root.getClass().getName().endsWith("Proxy"))
		{
		//	Is there a 'getProxy' method ?
		//
			try 
			{
				Method getProxyMethod = getRecursiveDeclaredMethod(root.getClass(), "getProxy", (Class<?>[])null);
				if (getProxyMethod != null)
				{
					try 
					{
						Object result = getProxyMethod.invoke(root, (Object[]) null);
						result = searchMember(clazz, result, alreadyChecked);
						if (result != null)
						{
							return result;
						}
					}
					catch (Exception e)
					{
						throw new RuntimeException(e);
					}
					
				}
			}
			catch (NoSuchMethodException e)
			{
			//	No getProxy method, probably not a Spring proxy...
			}
		}
		
	//	Member not found
	//
		return null;
	}
	
}
