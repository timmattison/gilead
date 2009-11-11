package com.google.gwt.user.client.rpc.core.com.google.appengine.api.datastore;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;

/**
 * Custom serializer for DataNucleus {@link com.google.appengine.api.datastore.Key} class. The complexity of the
 * serializer reflects the complex nature of the class
 * @author Alex Dobjanschi (alex.dobjanschi@gmail.com)
 *
 */
public class Key_CustomFieldSerializer {

	   public static void deserialize(SerializationStreamReader streamReader,
			   Key instance) throws SerializationException
	    {
	        // no fields
	    }

	    public static Key instantiate (SerializationStreamReader streamReader)
	                                        throws SerializationException {
	    	
    		Key instance = null;
    		Key parent   = null;
	    	
	    	boolean hasParent = streamReader.readBoolean();
	    	if (hasParent) {
	    		parent = instantiate (streamReader);
	    	}

    		String kind = streamReader.readString();
    		if ("null".equals(kind))
    			kind = null;
	    		
    		String name = streamReader.readString();
    		if ("null".equals(name))
    			name = null;
	    		
    		long id = streamReader.readLong();
	    	
    		if (name == null)
    			instance = KeyFactory.createKey (parent, kind, id);
    		else
    			instance = KeyFactory.createKey(parent, kind, name);

	        return instance;
	    }

	    public static void serialize (SerializationStreamWriter streamWriter,
	    							Key instance) throws SerializationException
	    {
	        if (instance.getParent() != null) {
	        	streamWriter.writeBoolean (true);
	        	serialize (streamWriter, instance.getParent());
	        } else {
	        	streamWriter.writeBoolean (false);
	        }
	    	
	    	String kind = instance.getKind() != null ? instance.getKind() : "null";
	        streamWriter.writeString (kind);
	        
	        String name = instance.getName() != null ? instance.getName() : "null";
	        long id = instance.getId();
	        
	        streamWriter.writeString(name);
	        streamWriter.writeLong(id);	        
	    }
}