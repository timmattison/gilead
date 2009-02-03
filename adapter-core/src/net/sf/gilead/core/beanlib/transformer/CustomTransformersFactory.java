package net.sf.gilead.core.beanlib.transformer;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import net.sf.beanlib.spi.BeanTransformerSpi;
import net.sf.beanlib.spi.CustomBeanTransformerSpi;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Factory for gilead custom transformers
 * @author Alexandre Pretyman, Bruno Marchesson
 *
 */
public class CustomTransformersFactory
{
	//----
	// Attributes
	//----
	/**
	 * Log channel
	 */
	private static Log _log = LogFactory.getLog(CustomTransformersFactory.class);

	/**
	 * Singleton instance
	 */
	private static CustomTransformersFactory _instance = null;
	
	/**
	 * List of custom bean transformers constructors
	 */
	private List<Constructor<CustomBeanTransformerSpi>> _constructorList;
	
	//----
	// Singleton
	//----
	/**
	 * @return the unique instance of the factory
	 */
	public static synchronized CustomTransformersFactory getInstance()
	{
		if (_instance == null)
		{
			_instance = new CustomTransformersFactory();
		}
		return _instance;
	}
	
	//-------------------------------------------------------------------------
	//
	// Constructor
	//
	//-------------------------------------------------------------------------
	/**
	 * Private constructor
	 */
	private CustomTransformersFactory()
	{
		_constructorList = new ArrayList<Constructor<CustomBeanTransformerSpi>>();
		
	//	Timestamp transformer is needed for Gilead
	//
		addCustomBeanTransformer(TimestampCustomTransformer.class);

	}
	
	//-------------------------------------------------------------------------
	//
	// Public interface
	//
	//-------------------------------------------------------------------------
	
	/**
	 * Add a custom bean transformer
	 */
	@SuppressWarnings("unchecked")
	public void addCustomBeanTransformer(Class transformerClass)
	{
		_constructorList.add(getConstructorFor(transformerClass));
	}
	
	/**
	 * Create a union transformer if needed
	 * @param beanTransformer the input bean transformer
	 * @return the beanlib CustomBeanTransformer.
	 */
	public CustomBeanTransformerSpi createUnionCustomBeanTransformer(BeanTransformerSpi beanTransformer)
	{
		int transformerCount = _constructorList.size();
		if (transformerCount == 1)
		{
		//	No additional custom transformer defined : just use the one
		//
			return instantiate(_constructorList.get(0), beanTransformer);
		}
		else
		{
		//	Create each trasnformer
		//
			CustomBeanTransformerSpi[] customBeanTransformers = new CustomBeanTransformerSpi[transformerCount];
			for (int index = 0 ; index < transformerCount ; index ++)
			{
				customBeanTransformers[index] = instantiate(_constructorList.get(index), 
															beanTransformer);
			}
			
			return new UnionCustomBeanTransformer(customBeanTransformers);
		}
	}
	
	//-------------------------------------------------------------------------
	//
	// Internal methods
	//
	//-------------------------------------------------------------------------
	/**
	 * Get constructor for the argument class
	 * @param clazz the custom bean transformer class
	 * @return the constructor that take a BeanTransformerSpi as argument from the class
	 */
	protected Constructor<CustomBeanTransformerSpi> getConstructorFor(Class<CustomBeanTransformerSpi> clazz)
	{
	//	Constructor has a transformer argument
	//
		Class<?>[] ctArg = { BeanTransformerSpi.class };
		
		try 
		{
			// Search for constructors with BeanTransformerSpi argument
			return clazz.getConstructor(ctArg);
		} 
		catch (Exception e)
		{
			_log.error("Cannot find constructor with BeanTransformerSpi argument for class " + clazz, e);
			throw new RuntimeException("Error retrieving constructor for " + clazz, e);
		} 
	}
	
	/**
	 * Create a new transformer instance
	 * @param constructor the constructor
	 * @param beanTransformer the bean transformer needed argument
	 * @return the CustomBeanTransformer
	 */
	protected CustomBeanTransformerSpi instantiate(Constructor<CustomBeanTransformerSpi> constructor,
												   final BeanTransformerSpi beanTransformer)
	{
		// Constructor argument
		Object[] initArgs = { beanTransformer };

		// New instance of custom transformer
		try 
		{
			return constructor.newInstance(initArgs);
		}
		catch (Exception e)
		{
		//	Throw unrecoverable error on exception
		//
			_log.error("Transformer initialization error", e);
			throw new RuntimeException("Transformer initialization error", e);
		}
	}
}
