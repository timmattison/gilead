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
		writeSerializationTransformers = new ArrayList<ISerializationTransformer>();
		readSerializationTransformers = new ArrayList<ISerializationTransformer>();
	}
	
	//-----
	// Attributes
	//-----
	/**
	 * The associated serialization filter for write operation
	 */
	private ISerializationFilter writeSerializationFilter;
	
	/**
	 * The associated serialization transformers for write operation
	 */
	private List<ISerializationTransformer> writeSerializationTransformers;
	
	/**
	 * The associated serialization filter for read operation
	 */
	private ISerializationFilter readSerializationFilter;
	
	/**
	 * The associated serialization transformers for read operation
	 */
	private List<ISerializationTransformer> readSerializationTransformers;
	
	//----
	// Properties
	//----
	/**
	 * @return the serialization Filter for write operation
	 */
	public ISerializationFilter getWriteSerializationFilter()
	{
		return writeSerializationFilter;
	}

	/**
	 * @param filter the serializationFilter for write operation
	 */
	public void setWriteSerializationFilter(ISerializationFilter filter)
	{
		this.writeSerializationFilter = filter;
	}

	/**
	 * @return the serialization Transformers list for write operation
	 */
	public List<ISerializationTransformer> getWriteSerializationTransformers()
	{
		return writeSerializationTransformers;
	}

	/**
	 * @return the readSerializationFilter
	 */
	public ISerializationFilter getReadSerializationFilter()
	{
		return readSerializationFilter;
	}

	/**
	 * @param readSerializationFilter the readSerializationFilter to set
	 */
	public void setReadSerializationFilter(
			ISerializationFilter readSerializationFilter)
	{
		this.readSerializationFilter = readSerializationFilter;
	}

	/**
	 * @return the readSerializationTransformers
	 */
	public List<ISerializationTransformer> getReadSerializationTransformers()
	{
		return readSerializationTransformers;
	}

	//-------------------------------------------------------------------------
	//
	// Public interface
	//
	//-------------------------------------------------------------------------
	/**
	 * @param transformer the serializationTransformer to set for both read and write operation
	 */
	public void addSerializationTransformer(ISerializationTransformer transformer)
	{
		writeSerializationTransformers.add(transformer);
		readSerializationTransformers.add(transformer);
	}
	
	/**
	 * @param transformer the transformer to set for write operation
	 */
	public void addWriteSerializationTransformer(ISerializationTransformer transformer)
	{
		writeSerializationTransformers.add(transformer);
	}
	
	/**
	 * @param transformer the transformer to set for read operation
	 */
	public void addReadSerializationTransformer(ISerializationTransformer transformer)
	{
		readSerializationTransformers.add(transformer);
	}
	
	/**
	 * @param instance
	 * @return the instance associated serialization transformer if any, null otherwise.
	 */
	public ISerializationTransformer getWriteSerializationTransformerFor(Object instance)
	{
		for (ISerializationTransformer transformer : writeSerializationTransformers)
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
	
	/**
	 * @param instance
	 * @return the instance associated serialization transformer if any, null otherwise.
	 */
	public ISerializationTransformer getReadSerializationTransformerFor(Object instance)
	{
		for (ISerializationTransformer transformer : readSerializationTransformers)
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
