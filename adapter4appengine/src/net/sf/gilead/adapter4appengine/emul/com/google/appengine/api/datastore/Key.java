package com.google.appengine.api.datastore;

import java.io.Serializable;

/**
 * JSNI emulation of DataNucleus KEY class
 * From http://fredsa.allen-sauer.com/2009/04/1st-look-at-app-engine-using-jdo.html
 * @author bruno.marchesson
 * 
 */
public class Key implements Serializable, Comparable
{
	//----
	// Attributes
	//----
	// Field descriptor #18 Ljava/lang/String;
	private String appId;

	// Field descriptor #12 J
	private long id;

	//----
	// Getters
	//----
	/**
	 * @return the id
	 */
	public long getId()
	{
		return id;
	}

	/**
	 * @return the name
	 */
	public String getAppId()
	{
		return appId;
	}

	/**
	 * @param kind
	 */
	public Key() {
		super();
	}
	
	/**
	 * Comparator implementation
	 */
	public int compareTo(Object o) {
	    throw new UnsupportedOperationException();
	  }

}
