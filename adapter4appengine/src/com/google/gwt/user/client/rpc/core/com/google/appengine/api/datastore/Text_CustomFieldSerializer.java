package com.google.gwt.user.client.rpc.core.com.google.appengine.api.datastore;

import com.google.appengine.api.datastore.Text;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;

/**
 * Custom serializer for GAE Datastore Text class.
 * Mandatory since Text does not have a public empty constructor
 * @author bruno.marchesson
 *
 */
public final class Text_CustomFieldSerializer {

    public static void deserialize(SerializationStreamReader streamReader,
            Text instance) throws SerializationException
    {
        // no fields
    }

    public static Text instantiate(SerializationStreamReader streamReader)
                                        throws SerializationException {
        Text instance =  new Text(streamReader.readString());
       
        return instance;
    }

    public static void serialize(SerializationStreamWriter streamWriter,
                                 Text instance) throws SerializationException
    {
        streamWriter.writeString(instance.getValue());
    }

}