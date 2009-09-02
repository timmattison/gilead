package com.google.appengine.api.datastore;

import java.io.Serializable;
import java.util.Arrays;

/**
 * JSNI emulation of DataNucleus {@link com.google.appengine.api.datastore.ShortBlob} class.
 * 
 * @author Alex Dobjanschi (alex.dobjanschi@gmail.com)
 */
public final class ShortBlob implements Serializable, Comparable {

	/**
	 * Value for this blob
	 */
    private final byte bytes[];

    /**
     * Constructor (default one ommited)
     * @param bytes
     */
    public ShortBlob (byte bytes[]) {
    	
    	// allocate new memory
        this.bytes = new byte [bytes.length];
        
        // copy the bytes
        System.arraycopy (bytes, 0, this.bytes, 0, bytes.length);
    }

    /**
     * Getter for {@link #bytes}
     * @return
     * 		The bytes held by this object
     */
    public byte[] getBytes() {
        return bytes;
    }

    public int hashCode() {
        return Arrays.hashCode(bytes);
    }

    /**
     * 
     */
    public boolean equals(Object object) {
    	
        if(object instanceof ShortBlob) {
            ShortBlob other = (ShortBlob) object;
            return Arrays.equals(bytes, other.bytes);
        } else
            return false;
    }

    
    public String toString() {
    	return "<ShortBlob: " + bytes.length + " bytes>";
    }
    

    /**
     * Simplified implementation of original code. The {@link DataTypeTranslator}'s {@link ComparableByteArray} with
     * {@link #compareTo(Object)} method is implemented straight here. 
     * @param other
     * @return
     */
    public int compareTo (ShortBlob other) {
    	
    	byte[] otherBytes = other.getBytes();
    	for (int i = 0; i < Math.min(bytes.length, otherBytes.length); i++) {
    		int v1 = bytes [i] & 0xff;
    		int v2 = otherBytes [i] & 0xff;
    		
    		if (v1 != v2)
    			return v1 - v2;
    	}
    	
    	return bytes.length - otherBytes.length;
    }

    /**
     * <b>Unsafe behavior</b> of {@link Comparable#compareTo(Object)} implementation.
     * <br/> 
     * If {@link #object}'s class is not {@link ShortBlob}'s class, a <code>ClassCastException</code> will be thrown
     * (unchecked exception, disrupting program execution) 
     */
    public int compareTo (Object object) {
        return compareTo((ShortBlob) object);
    }
}