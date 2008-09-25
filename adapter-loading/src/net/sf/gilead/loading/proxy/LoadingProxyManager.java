package net.sf.gilead.loading.proxy;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.sf.gilead.loading.LoadingHelper;
import net.sf.gilead.loading.annotations.LoadingInterface;
import net.sf.gilead.proxy.JavassistProxyGenerator;
import net.sf.gilead.proxy.xml.AdditionalCode;
import net.sf.gilead.proxy.xml.Constructor;
import net.sf.gilead.proxy.xml.Method;
import net.sf.gilead.proxy.xml.Parameter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Additional code generator for loading interfaces.
 * @author bruno.marchesson
 */
public class LoadingProxyManager
{
	//----
	// Attributes
	//----
	/**
	 * Log channel.
	 */
	private static Log _log = LogFactory.getLog(LoadingProxyManager.class);
	
	/**
	 * Proxy map.
	 * Key is the loading interface, value is the associated wrapper.
	 */
	private Map<Class<?>,Class<?>> _proxyMap;
	//-----
	// Singleton
	//-----
	/**
	 * Unique instance of singleton
	 */
	private static LoadingProxyManager _instance;
	
	/**
	 * @return the unique instance of the singleton
	 */
	public static LoadingProxyManager getInstance()
	{
		if (_instance == null)
		{
			_instance = new LoadingProxyManager();
		}
		return _instance;
	}
	
	//-------------------------------------------------------------------------
	//
	// Constructor
	//
	//-------------------------------------------------------------------------
	/**
	 * Ctor
	 */
	protected LoadingProxyManager()
	{
		_proxyMap = new HashMap<Class<?>, Class<?>>();
	}
	
	//-------------------------------------------------------------------------
	//
	// Public interface
	//
	//-------------------------------------------------------------------------
	/**
	 * Returns the wrapper association with the loading interface
	 * @param loadingInterface
	 * @return
	 */
	public Class<?> getWrapper(Class<?> loadingInterface)
	{
	//	Precondition checking
	//
		Class<?> wrapperClass = _proxyMap.get(loadingInterface);
		if (wrapperClass != null)
		{
			return wrapperClass;
		}
		
	//	Generate additional code
	//
		// Add the loading interface wrapper as generated to prevent infinite loop
		// (bidirectional associations)
		_proxyMap.put(loadingInterface, getClass());
		
		AdditionalCode additionalCode = generateCodeFor(loadingInterface);
		JavassistProxyGenerator generator = new JavassistProxyGenerator();
		wrapperClass = generator.generateProxyFor(LoadingWrapper.class, additionalCode);
		
	//	Add it to the map
	//
		_proxyMap.put(loadingInterface, wrapperClass);
		return wrapperClass;
	}
	
	/**
	 * Wrap the persistent entity with the loading interface relevant wrapper
	 * @param entity
	 * @param loadingInterface
	 * @return
	 */
	public Object wrapAs(Object entity, Class<?> loadingInterface)
	{
	//	Precondition checking
	//
		if (entity == null)
		{
			return null;
		}
		
		try
		{
			Class<?> wrapperClass = getWrapper(loadingInterface);
			java.lang.reflect.Constructor<?> constructor = 
							wrapperClass.getConstructor(entity.getClass());
			return constructor.newInstance(entity);
		} 
		catch (Exception e)
		{
			throw new RuntimeException("Wrapper creation exception", e);
		}
	}

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
	protected AdditionalCode generateCodeFor(Class<?> loadingInterface)
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
			additionalMethod.setCode(generateCode(persistentClass, interfaceMethod));
			
			code.addMethod(additionalMethod);
		}
		
		return code;
	}
	/**
	 * Generate the additional method code for the loading interface method
	 */
	private String generateCode(Class<?> persistentClass,
								java.lang.reflect.Method interfaceMethod)
	{
		StringBuilder code = new StringBuilder();
		code.append("{ ");
		Class<?> returnType = interfaceMethod.getReturnType();
		
		if (returnType != Void.TYPE)
		{
			if ((Collection.class.isAssignableFrom(returnType)) ||
				(Map.class.isAssignableFrom(returnType)))
			{
				// TODO
				throw new RuntimeException("Not yet implemented  : collection wrapping !");
			}
			else if (returnType.getAnnotation(LoadingInterface.class) != null)
			{
			//	Wrapping needed
			//
				// Generate wrapper for nested Loading interface
				getWrapper(returnType);
				
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
					getWrapper(parameter);
					
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
}
