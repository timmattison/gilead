package com.google.appengine.api.datastore;

import java.io.Serializable;

/**
 * JSNI emulation of DataNucleus {@link com.google.appengine.datastore.api.Link} class
 * @author Alex Dobjanschi (alex.dobjanschi@gmail.com)
 *
 */
public final class Link implements Serializable, Comparable {

	/**
	 * The value
	 */
    private final String value;
	
    
    /**
     * Constructor 
     * @param value
     */
    public Link(String value) {
        this.value = value;
    }

    /**
     * Getter for {@link #value}
     * @return
     * 		The value held by this {@link Link} class
     */
    public String getValue() {
        return value;
    }

    public int hashCode() {
        return value.hashCode();
    }

    public boolean equals (Object object) {
        if(object instanceof Link) {
        	Link key = (Link) object;
        	
        	// avoid NullPointerException (value.equals...) 
        	//
        	if (value == null) { 
        		if (key == null)
        			return true;

        		return false;
        	} else {
        		return value.equals(key.getValue());
        	}
        } else
        	return false;
    }

    /**
     * Equivalent to {@link #getValue()}
     */
    public String toString() {
        return value;
    }

    /**
     * Safe comparator implementation
     * @param l
     * 		The other link object
     * @return
     * 		The signed comparisson between the values
     */
    public int compareTo (Link l) {
        return value.compareTo (l.value);
    }

    /**
     * <b>Unsafe behavior</b> of {@link Comparable#compareTo(Object)} implementation.
     * <br/> 
     * If {@link #object}'s class is not {@link Link}'s class, a <code>ClassCastException</code> will be thrown
     * (unchecked exception, disrupting program execution) 
     * @param object
     * @return 
     */
    public int compareTo (Object object) {
        return compareTo((Link) object);
    }
}