package net.sf.gilead.loading;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

import net.sf.gilead.loading.annotations.LoadingInterface;
import net.sf.gilead.loading.exception.NoLoadingInterfaceException;

/**
 * Static helper for loading module.
 * @author bruno.marchesson
 *
 */
public class LoadingHelper
{
	//-------------------------------------------------------------------------
	//
	// Static helper
	//
	//-------------------------------------------------------------------------
	/**
	 * Returns the underlying persistent class, an exception if not found
	 */
	public static Class<?> getPersistentClass(Class<?> loadingInterface)
	{
	//	Precondition checking
	//
		if (loadingInterface == null)
		{
			throw new NullPointerException("No loading interface !");
		}
		
	//	Get annotation
	//
		LoadingInterface annotation = loadingInterface.getAnnotation(LoadingInterface.class);
		if (annotation == null)
		{
			throw new NoLoadingInterfaceException(loadingInterface);
		}
		
	//	Get persistent class
	//
		Class<?> persistentClass = annotation.persistentClass();
		if (persistentClass == null)
		{
			throw new RuntimeException("Missing persistent class in @LoadingInterface for " 
									   + loadingInterface.getName());
		}
		
		return persistentClass;
	}
	
	/**
	 * Returns the (generic) types of the argument collection class
	 * @param collectionClass
	 * @return
	 */
	public static Class<?>[] getCollectionTypes(Type collectionType)
	{
		Set<Class<?>> typeList = new HashSet<Class<?>>();
		
	//	Generic introspection
	//
		if ((collectionType != null)&&
			(collectionType instanceof ParameterizedType))
	    {
	        ParameterizedType aType = (ParameterizedType) collectionType;
	        java.lang.reflect.Type[] parameterArgTypes = aType.getActualTypeArguments();
	        for(java.lang.reflect.Type parameterArgType : parameterArgTypes)
	        {
	            typeList.add((Class<?>) parameterArgType);
	        }
	    }
		
	//	Result conversion
	//
		if (typeList.isEmpty())
		{
			return null;
		}
		else
		{
			return typeList.toArray(new Class<?>[typeList.size()]);
		}
	}
}
