package com.google.gwt.user.client.rpc.core.com.google.appengine.api.datastore;

import com.google.appengine.api.datastore.ShortBlob;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;

/**
 * 
 * @author Alex Dobjanschi (alex.dobjanschi@gmail.com)
 *
 */
public class ShortBlob_CustomFieldSerializer {
    public static void deserialize(SerializationStreamReader streamReader,
    		ShortBlob instance) throws SerializationException
    {
        // no fields
    }

    public static ShortBlob instantiate(SerializationStreamReader streamReader)
                                        throws SerializationException {
    	
    	int length = streamReader.readInt();
    	byte[] bytes = new byte [length];

    	for (int i = 0; i < length; i++) {
    		bytes [i] = streamReader.readByte(); 
    	}
    	
    	ShortBlob instance =  new ShortBlob (bytes);
        return instance;
    }

    public static void serialize(SerializationStreamWriter streamWriter,
    							ShortBlob instance) throws SerializationException
    {
    	byte[] bytes = instance.getBytes();    	
    	int length   = bytes.length;

    	streamWriter.writeInt(length);
    	for (int i = 0; i < length; i++) {
    		streamWriter.writeByte(bytes [i]);
    	}
    }
}
