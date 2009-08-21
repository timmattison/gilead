package com.google.appengine.api.datastore;

import java.io.Serializable;
import java.util.Arrays;

/**
 * JSNI emulation of DataNucleus {@link com.google.appengine.api.datastore.Blob} class
 * @author Alex Dobjanschi
 *
 */
public class Blob implements Serializable {

	/**
	 * The bytes representing this BLOB object.
	 */
	private final byte[] bytes;
	
	/**
	 * Constructor ({@link #bytes} field is treated as <code>final</code>)
	 * @param bytes
	 */
	public Blob (byte[] bytes) {
		this.bytes = bytes;
	}

	/**
	 * Getter for {@link #bytes} member
	 * @return
	 * 		The bytes representing this BLOB object
	 */
	public byte[] getBytes() {
		return bytes;
	}

	@Override
	public boolean equals(Object obj) {

		// check for null object
		//
		if (obj == null)
			return false;
		
		// check same class signature
		//
		if ( !(obj instanceof Blob) )
			return false;
		
		// cast
		Blob other = (Blob) obj;
		
		//
		return Arrays.equals(bytes, other.getBytes());
	}

	@Override
	public int hashCode() {		
		return Arrays.hashCode(bytes);
	}

	/**
	 * Return a simple information about this blob (just the length of {@link bytes})
	 */
	@Override
	public String toString() {
		
		return "<Blob: " + bytes.length + " bytes>";
	}
}
