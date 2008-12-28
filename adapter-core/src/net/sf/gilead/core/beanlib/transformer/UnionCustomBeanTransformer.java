package net.sf.gilead.core.beanlib.transformer;


import net.sf.beanlib.PropertyInfo;
import net.sf.beanlib.spi.CustomBeanTransformerSpi;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The UnionCustomBeanTransformer loads the files named after the constant TRANSFORMER_XML_FILENAME, reading the CustomTransformers
 * class fully qualified name. After loading all classes it looks for the Constructor of each of those classes which takes a
 * BeanTransformerSpi class as an argument, storing these constructors in a list, where they will be instantiated for
 * every call to the UnionCustomBeanTransformer.newInstance() method. The XML parsing will be done only once, at the first time
 * the method is called. 
 * 
 * @author Hanson Char, Alexandre Pretyman
 * 
 *         This is a copy and paste from
 *         http://groups.google.com/group/beanlib/browse_thread
 *         /thread/f300b5470c08f683 with implementation to load custom
 *         transformers for Hibernate4Gwt
 *         
 */
public class UnionCustomBeanTransformer implements CustomBeanTransformerSpi
{
	//----
	// Attributes
	//----
	/**
	 * Log channel
	 */
	private static Log _log = LogFactory.getLog(UnionCustomBeanTransformer.class);

	/**
	 * Current custom transformers
	 */
	private CustomBeanTransformerSpi[] _customTransformers;

	//-------------------------------------------------------------------------
	//
	// Constructor
	//
	//-------------------------------------------------------------------------
	/**
	 * Constructor
	 * @param customTransformers
	 */
	public UnionCustomBeanTransformer(
			CustomBeanTransformerSpi... customTransformers)
	{
		this._customTransformers = customTransformers;
	}

	//-------------------------------------------------------------------------
	//
	// CustomBeanTransformerSpi implementation
	//
	//-------------------------------------------------------------------------
	// @Override
	public <T> boolean isTransformable(Object from, Class<T> toClass,
			PropertyInfo propertyInfo)
	{
	//	Iterate over custom transformers
	//
		for (CustomBeanTransformerSpi cbt : _customTransformers)
		{
			if (cbt.isTransformable(from, toClass, propertyInfo))
				return true;
		}
		return false;
	}

	// @Override
	public <T> T transform(Object in, Class<T> toClass,
						   PropertyInfo propertyInfo)
	{
		boolean isTransformed = false;
		T out = null;

		for (CustomBeanTransformerSpi cbt : _customTransformers)
		{
			if (cbt.isTransformable(in, toClass, propertyInfo))
			{
				out = cbt.transform(isTransformed ? out : in, toClass,
								  	propertyInfo);
				isTransformed = true;
			}
		}
		
		return out;
	}
}