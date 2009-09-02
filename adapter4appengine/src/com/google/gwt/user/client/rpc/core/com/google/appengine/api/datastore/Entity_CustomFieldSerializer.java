package com.google.gwt.user.client.rpc.core.com.google.appengine.api.datastore;

import java.util.Map;
import java.util.Iterator;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;

/**
 * Custom serializer for DataNucleus {@link com.google.appengine.api.datastore.Entity} class. This is one of the
 * most complex classes in datastore, resulting in a complex serializer.
 * @author Alex Dobjanschi (alex.dobjanschi@gmail.com)
 *
 */
public class Entity_CustomFieldSerializer {
	public static void deserialize(SerializationStreamReader streamReader,
			Entity instance) throws SerializationException
			{
		// no fields
			}

	public static Entity instantiate (SerializationStreamReader streamReader)
	throws SerializationException {

		Key k = Key_CustomFieldSerializer.instantiate (streamReader);
		Entity instance = new Entity (k.getKind(), k.getName());
		
		int propertiesSize = streamReader.readInt();
		for (int i = 0; i < propertiesSize; i++) {
			String key = streamReader.readString();
			Object value = streamReader.readObject();
			
			instance.setProperty(key, value);
		}

		return instance;
	}

	public static void serialize (SerializationStreamWriter streamWriter,
			Entity instance) throws SerializationException
			{

		Key_CustomFieldSerializer.serialize(streamWriter, instance.getKey());

		Map<String, Object> properties = instance.getProperties();
		streamWriter.writeInt(properties.size());

		Iterator<String> i = properties.keySet().iterator();
		while (i.hasNext()) {
			String key = i.next();
			Object value = properties.get (key);

			streamWriter.writeString (key);
			streamWriter.writeObject (value);
		}
	}	
}
