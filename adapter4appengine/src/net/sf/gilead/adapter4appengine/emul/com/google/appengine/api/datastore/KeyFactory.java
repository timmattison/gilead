package com.google.appengine.api.datastore;

import com.google.appengine.api.datastore.Key;

/**
 * JSNI emulation for App Engine {@link com.google.appengine.api.datastore.KeyFactory} class
 * @author Vincent Legendre
 *
 */
public class KeyFactory
{
	/**
	 * Creates a new Key with the provided parent from its kind and ID.
	 * @param parent
	 * @param kind
	 * @param id
	 * @return
	 */
	public static Key createKey(Key parent, java.lang.String kind, long id)
	{
		return new Key( kind, parent, id );
	}

	/**
	 * Creates a new Key with the provided parent from its kind and name.
	 * @param parent
	 * @param kind
	 * @param name
	 * @return
	 */
	public static Key createKey(Key parent, java.lang.String kind, java.lang.String name)
	{
		return new Key( kind, parent, name );
	}



}