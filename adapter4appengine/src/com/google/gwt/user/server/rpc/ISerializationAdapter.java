package com.google.gwt.user.server.rpc;

/**
 * Interface of serialization adapter
 * @author bruno.marchesson
 *
 */
public interface ISerializationAdapter
{
	/**
	 * Indicates if the field should be used for CRC computation in serialization process
	 * @param clazz the target class
	 * @param fieldName the field name
	 * @return
	 */
	public boolean shouldSerialize(Class<?> clazz, String fieldName);
	
	/**
	 * Indicates if the value of the argument field name should be serialized for this object
	 * @param obj
	 * @param fieldName
	 * @return
	 */
	public boolean shouldSerialize(Object obj, String fieldName, Object value);
	
	/**
	 * Called before serialization of the argument object
	 * @param obj
	 */
	public void beforeSerialization(Object obj);
	
	/**
	 * Called after the serialization of the argument object
	 * @param obj
	 */
	public void afterSerialization(Object obj);
}
