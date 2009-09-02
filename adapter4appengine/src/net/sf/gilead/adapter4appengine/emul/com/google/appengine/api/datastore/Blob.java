package com.google.appengine.api.datastore;

import java.io.Serializable;

import java.util.Arrays;

/**
 * JSNI emulation of DataNucleus {@link com.google.appengine.api.datastore.Blob} class.
 * This class has no default constructor, serialization is achieved using a <code>CustomFieldSerializer</code> 
 * @author Alex Dobjanschi (alex.dobjanschi@gmail.com)
 */
public final class Blob implements Serializable {
	
	/**
	 * The bytes
	 */
    private final byte bytes[];	

    
    public Blob (byte bytes[]) {
        this.bytes = bytes;
    }

    /**
     * 
     * @return
     * 		Returns the bytes held by this object
     */
    public byte[] getBytes() {
        return bytes;
    }

    /**
     * Calculate the hash
     */
    public int hashCode() {
        return Arrays.hashCode(bytes);
    }

    public boolean equals(Object object) {
        if(object instanceof Blob) {
            Blob key = (Blob) object;
            return Arrays.equals(bytes, key.bytes);
        } else {
            return false;
        }
    }

    /**
     * Return a simple info about the object (just its length and type)
     */
    public String toString() {    	
    	return "<Blob: " + bytes.length + " bytes>";
    }
}