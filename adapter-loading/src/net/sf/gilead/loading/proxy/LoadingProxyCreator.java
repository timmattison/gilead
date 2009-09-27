package net.sf.gilead.loading.proxy;

import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.gilead.loading.LoadingHelper;
import net.sf.gilead.loading.annotations.LoadingInterface;
import net.sf.gilead.proxy.xml.AdditionalCode;
import net.sf.gilead.proxy.xml.Constructor;
import net.sf.gilead.proxy.xml.Method;
import net.sf.gilead.proxy.xml.Parameter;

/**
 * Class for creating loading interface proxies
 * @author bruno.marchesson
 *
 */
public class LoadingProxyCreator
{
	//----
	// Attributes
	//----
	/**
	 * Logger channel
	 */
	private static Logger _log = LoggerFactory.getLogger(LoadingProxyCreator.class);
	
	//-------------------------------------------------------------------------
	//
	// Internal methods
	//
	//-------------------------------------------------------------------------
	/**
	 * Generate code for loading wrapper
	 * @param persistentClass the root persistent class
	 * @param loadingInterface the associated loading interface
	 * @return the additional code to generate
	 */
	public static AdditionalCode generateProxyFor(Class<?> loadingInterface)
	{
	//	Get persistent class
	//
		Class<?> persistentClass = LoadingHelper.getPersistentClass(loadingInterface);
		_log.info("Generating wrapper code for " + loadingInterface.getName());
		
	//	Create additional code
	//
		AdditionalCode code = new AdditionalCode();
		
	//	Set suffix and implemented interfaces
	//
		code.setSuffix(loadingInterface.getSimpleName());
		code.setImplementedInterface(loadingInterface.getCanonicalName());
			
	//	Add constructor
	//
		Constructor constructor = new Constructor();
		constructor.setVisibility("public");
		
		Parameter parameter = new Parameter();
		parameter.setName("data");
		parameter.setType(persistentClass.getCanonicalName());
		constructor.addParameter(parameter);
		
		constructor.setCode("{ _data = data; }");
		code.addConstructor(constructor);
		
	//	Generate interface needed methods
	//
		for (java.lang.reflect.Method interfaceMethod : loadingInterface.getMethods())
		{
		//	Convert java.lang.reflect.method to additional code method
		//
			Method additionalMethod = AdditionalCodeHelper.convertMethod(interfaceMethod);
			additionalMethod.setCode(generateMethodProxy(persistentClass, interfaceMethod));
			
			code.addMethod(additionalMethod);
		}
		
		return code;
	}
	/**
	 * Generate the additional method code for the loading interface method
	 */
	protected static String generateMethodProxy(Class<?> persistentClass,
								java.lang.reflect.Method interfaceMethod)
	{
		StringBuilder code = new StringBuilder();
		code.append("{ ");
		Class<?> returnType = interfaceMethod.getReturnType();
		
	//	Return instruction handling (Loading interfaces must be wrapped)
	//
		if (returnType != Void.TYPE)
		{
			if (Collection.class.isAssignableFrom(returnType))
			{
				// Generate wrapper for nested Loading interface
				Class<?> listItemClass = interfaceMethod.getGenericReturnType().getClass(); 
				forceProxyGenerationFor(listItemClass);
				
				code.append(" return wrapCollectionAs(");
				code.append(listItemClass.getName());
				code.append(".class,");
			}
			else if (Map.class.isAssignableFrom(returnType))
			{
				// TODO : get map key and values
				throw new RuntimeException("Not yet implemented  : map wrapping !");
			}
			else if (returnType.getAnnotation(LoadingInterface.class) != null)
			{
			//	Wrapping needed
			//
				// Generate wrapper for nested Loading interface
				forceProxyGenerationFor(returnType);
				
				code.append(" return wrapAs(");
				code.append(returnType.getName());
				code.append(".class,");
			}
			else
			{
				code.append(" return (");
			}
			
		}
		code.append("((");
		code.append(persistentClass.getName());
		code.append(")_data).");
		code.append(interfaceMethod.getName());
		code.append('(');
		
	//	Parameters handling
	//
		Class<?>[] parameters = interfaceMethod.getParameterTypes();
		if (parameters != null)
		{
		// 	Get persistent class parameters
		//
			for (int index = 0; index < parameters.length ; index ++)
			{
				Class<?> parameter = parameters[index];
				if (index > 0)
				{
					code.append(',');
				}
				
				if ((Collection.class.isAssignableFrom(parameter)) ||
					(Map.class.isAssignableFrom(parameter)))
				{
					// TODO
					throw new RuntimeException("Not yet implemented  : collection wrapping !");
				}
				else if (parameter.getAnnotation(LoadingInterface.class) != null)
				{
				//	Conversion from wrapper
				//
					// Generate wrapper for nested Loading interface
					forceProxyGenerationFor(parameter);
					
					Class<?> parameterPersistentClass = LoadingHelper.getPersistentClass(parameter);
					
					code.append("((");
					code.append(parameterPersistentClass.getName());
					code.append(")unwrap(param");
					code.append(index);
					code.append("))");
				}
				else
				{
					code.append("param");
					code.append(index);
				}
			}
		}
		
		if (returnType != Void.TYPE)
		{
			code.append(")");
		}
		code.append("); }");
		return code.toString();
	}
	
	/**
	 * Force proxy generation for the argument class
	 * @param clazz
	 */
	protected static void forceProxyGenerationFor(Class<?> clazz)
	{
		LoadingProxyManager.getInstance().getWrapper(clazz);
	}
}
