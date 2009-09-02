package com.google.gwt.user.client.rpc.core.com.google.appengine.api.datastore;

import com.google.appengine.api.datastore.Link;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;

/**
 * 
 * @author Alex Dobjanschi (alex.dobjanschi@gmail.com)
 *
 */
public class Link_CustomFieldSerializer {
    public static void deserialize(SerializationStreamReader streamReader,
    		Link instance) throws SerializationException
    {
        // no fields
    }

    public static Link instantiate(SerializationStreamReader streamReader)
                                        throws SerializationException {
    	Link instance =  new Link(streamReader.readString());
        return instance;
    }

    public static void serialize(SerializationStreamWriter streamWriter,
    							Link instance) throws SerializationException
    {
        streamWriter.writeString(instance.getValue());
    }
}
