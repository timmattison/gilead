package com.google.gwt.user.client.rpc.core.com.google.appengine.api.datastore;

import com.google.appengine.api.datastore.Blob;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;

/**
 * Custom serializer for DataNucleus {@link com.google.appengine.api.datastore.Blob} class
 * (no empty constructor)
 * @author Alex Dobjanschi (alex.dobjanschi@gmail.com)
 */
public final class Blob_CustomFieldSerializer {
	
    public static void deserialize(SerializationStreamReader streamReader,
            Blob instance) throws SerializationException
    {
        // no fields
    }

    public static Blob instantiate(SerializationStreamReader streamReader)
                                        throws SerializationException {
    	
    	int length = streamReader.readInt();
    	byte[] bytes = new byte [length];

    	for (int i = 0; i < length; i++) {
    		bytes [i] = streamReader.readByte(); 
    	}
    	
        Blob instance =  new Blob (bytes);
        return instance;
    }

    public static void serialize(SerializationStreamWriter streamWriter,
                                 Blob instance) throws SerializationException
    {
    	byte[] bytes = instance.getBytes();    	
    	int length   = bytes.length;

    	streamWriter.writeInt(length);
    	for (int i = 0; i < length; i++) {
    		streamWriter.writeByte(bytes [i]);
    	}
    }
}
