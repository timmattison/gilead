package com.google.appengine.api.datastore;

import java.io.Serializable;

/**
 * JSNI emulation of DataNucleus {@link com.google.appengine.api.datastore.Text} class
 * @author Alex Dobjanschi
 *
 */
public final class Text implements Serializable {

	/**
	 * The value
	 */
	private String value;

	/**
	 * Default constructor (private because it is only needed by serialization api)
	 */
	private Text () {}
	
	/**
	 * Constructor ({@link #value} field is treated as <code>final</code>)
	 * @param value
	 */
	public Text (String value) {
		this.value = value;
	}

	/**
	 * Get the data held by {@link #value} 
	 * @return
	 */
	public String getValue() {
		return value;
	}

	@Override
	public int hashCode() {
		
		return value.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		
		// check for null
		//
		if (obj == null)
			return false;
		
		// check for same class signature
		//
		if ( !(obj instanceof Text) )
			return false;
		
		//
		//
		Text other = (Text) obj;
		
		// check for null in either object's values (to avoid <code>NullPointerException</code>)
		//
		if (value == null || other.getValue() == null)
			return false;
		
		return value.equals(other.getValue());
	}

	/**
	 * Simplified version of original code - simply return the value held
	 */
	@Override
	public String toString() {
		return value;
	}
}
