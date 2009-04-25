package com.google.gwt.user.server.rpc;

/**
 * Interface for transforming data to be serialized
 * @author bruno.marchesson
 *
 */
public interface ISerializationTransformer
{
	/**
	 * Indicates if the instance must be transformed by the implementation of the interface or not.
	 * @param instance
	 * @return
	 */
	public boolean isTransformable(Object instance);
	
	/**
	 * Transformation method.
	 * @param instance
	 * @return
	 */
	public Object transform(Object instance);
}
