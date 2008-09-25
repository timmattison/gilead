package net.sf.gilead.loading.proxy;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

import net.sf.gilead.loading.LoadingHelper;
import net.sf.gilead.proxy.xml.Method;
import net.sf.gilead.proxy.xml.Parameter;

/**
 * Static helper for additional code generation.
 * @author bruno.marchesson
 *
 */
public class AdditionalCodeHelper
{
	//-----
	// Constants
	//----
	/**
	 * Public visibility
	 */
	public static final String PUBLIC_VISIBILITY = "public";
	
	/**
	 * Protected visibility
	 */
	public static final String PROTECTED_VISIBILITY = "protected";
	
	/**
	 * Protected visibility
	 */
	public static final String PRIVATE_VISIBILITY = "private";
	
	//-------------------------------------------------------------------------
	//
	// Static helpers
	//
	//-------------------------------------------------------------------------
	/**
	 * Convert a java.lang method declaration into an additional code one
	 */
	public static Method convertMethod(java.lang.reflect.Method method)
	{
		Method result = new Method();
		result.setName(method.getName());
		
		// Visibility conversion
		int modifier = method.getModifiers();
		if (Modifier.isPrivate(modifier))
		{
			result.setVisibility(PRIVATE_VISIBILITY);
		}
		else if (Modifier.isProtected(modifier))
		{
			result.setVisibility(PROTECTED_VISIBILITY);
		}
		else if (Modifier.isPublic(modifier))
		{
			result.setVisibility(PUBLIC_VISIBILITY);
		}
		
		// Return type
		Class<?> returnType = method.getReturnType();
		result.setReturnType(returnType.getCanonicalName());
		
		// Collection handling
		if ((Collection.class.isAssignableFrom(returnType)) ||
			(Map.class.isAssignableFrom(returnType)))
		{
		//	Get collection type based on generic
		//
			result.setReturnCollectionType(computeCollectionType(method.getGenericReturnType()));
		}
		
		// Convert parameters
		Class<?> [] parameters = method.getParameterTypes();
		if (parameters != null)
		{
			for (int index =0; index < parameters.length; index ++)
			{
				Class<?> methodParameter = parameters[index];
				
				Parameter parameter = new Parameter();
				parameter.setType(methodParameter.getCanonicalName());
				parameter.setName("param" + index);
				
				if ((Collection.class.isAssignableFrom(methodParameter)) ||
					(Map.class.isAssignableFrom(methodParameter)))
				{
					Type genericParameterType = method.getGenericParameterTypes()[index];
					parameter.setCollectionType(computeCollectionType(genericParameterType));
				}
				
				result.addParameter(parameter);
			}
		}
		
		return result;
	}
	
	//-------------------------------------------------------------------------
	//
	// Internal methods
	//
	//-------------------------------------------------------------------------
	/**
	 * Compute the collection type string
	 * @param collectionType
	 * @return
	 */
	private static String computeCollectionType(Type collectionType)
	{
		StringBuilder result = new StringBuilder();
		
		Class<?>[] collectionTypes = LoadingHelper.getCollectionTypes(collectionType);
		if (collectionTypes != null)
		{
		//	Convert to string
		//
			for (Class<?> type : collectionTypes)
			{
				if (result.length() > 0)
				{
					result.append(',');
				}
				result.append(type.getCanonicalName());
			}
		}
		
		return result.toString();
	}
}
