/**
 * 
 */
package com.google.gwt.user.server.rpc;

import java.util.ArrayList;
import java.util.List;

/**
 * Factory grouping the serialization extension points (filters, transformers, ...)
 * @author bruno.marchesson
 */
public class SerializationExtensionFactory
{
	//----
	// Singleton
	//----
	/**
	 * The instance of singleton
	 */
	private static SerializationExtensionFactory _instance = null;
	
	/**
	 * @return the unique instance of the singleton
	 */
	public static synchronized SerializationExtensionFactory getInstance()
	{
		if (_instance == null)
		{
			_instance = new SerializationExtensionFactory();
		}
		return _instance;
	}
	
	/**
	 * Private constructor
	 */
	private SerializationExtensionFactory()
	{
		serializationTransformers = new ArrayList<ISerializationTransformer>();
	}
	
	//-----
	// Attributes
	//-----
	/**
	 * The associated serialization filter
	 */
	private ISerializationFilter serializationFilter;
	
	/**
	 * The associated serialization transformers
	 */
	private List<ISerializationTransformer> serializationTransformers;
	
	//----
	// Properties
	//----
	/**
	 * @return the serializationFilter
	 */
	public ISerializationFilter getSerializationFilter()
	{
		return serializationFilter;
	}

	/**
	 * @param serializationFilter the serializationFilter to set
	 */
	public void setSerializationFilter(ISerializationFilter filter)
	{
		this.serializationFilter = filter;
	}

	/**
	 * @return the serializationTransformers
	 */
	public List<ISerializationTransformer> getSerializationTransformers()
	{
		return serializationTransformers;
	}

	//-------------------------------------------------------------------------
	//
	// Public interface
	//
	//-------------------------------------------------------------------------
	/**
	 * @param serializationTransformers the serializationTransformers to set
	 */
	public void addSerializationTransformer(ISerializationTransformer transformer)
	{
		serializationTransformers.add(transformer);
	}
	
	/**
	 * @param instance
	 * @return the instance associated serialization transformer if any, null otherwise.
	 */
	public ISerializationTransformer getSerializationTransformerFor(Object instance)
	{
		for (ISerializationTransformer transformer : serializationTransformers)
		{
			if (transformer.isTransformable(instance))
			{
				return transformer;
			}
		}
		
	//	Not transformer is associated to instance
	//
		return null;
	}
	
	
}
