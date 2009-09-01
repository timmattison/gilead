/**
 * 
 */
package com.google.gwt.user.client.rpc.core.java.lang;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;

/**
 * Custom Field Serializer for class... Class that does not provide an empty constructor
 * @author bruno.marchesson
 *
 */
public class Class_CustomFieldSerializer
{
	public static void deserialize(SerializationStreamReader streamReader,
								   Class instance) throws SerializationException
	{
		// no fields
	}

	public static Class instantiate(SerializationStreamReader streamReader)
										throws SerializationException
	{
		try
		{
			Class instance =  Class.forName(streamReader.readString());
			return instance;
		}
		catch(Exception ex)
		{
			throw new SerializationException(ex);
		}
	}

	public static void serialize(SerializationStreamWriter streamWriter,
								 Class instance) throws SerializationException
    {
		streamWriter.writeString(instance.getName());
	}
}
