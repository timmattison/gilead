package com.google.appengine.api.datastore;

import java.io.Serializable;
import java.util.*;

/**
 * JSNI emulation for DataNucleus {@link com.google.appengine.api.datastore.Entity} class - emulated class
 * is greatly simplified, but code was ported as much as it could have. Note that {@link Cloneable} interface
 * is not implemented anymore
 * 
 * @author Alex Dobjanschi (alex.dobjanschi@gmail.com)
 *
 */
public final class Entity implements Serializable {

	static final long serialVersionUID = 0xf463a13c5d979c79L;

	private final Key key;

	@SuppressWarnings("unchecked")
	private final Map propertyMap;	

	@SuppressWarnings("serial")
	static final class UnindexedValue implements Serializable {

		private final Object value;

		UnindexedValue(Object value) {
			this.value = value;
		}    	

		public Object getValue() {
			return value;
		}

		public boolean equals(Object other) {
			if(other instanceof UnindexedValue) {
				UnindexedValue uv = (UnindexedValue) other;
				return value != null ? value.equals (uv.value) : uv.value == null;
			} 

			return false;
		}

		public int hashCode() {
			return value != null ? value.hashCode() : 0;
		}

		public String toString() {
			return value + " (unindexed)";
		}
	}


	public Entity (String kind) {
		this(kind, (Key)null);
	}

	public Entity (String kind, Key parent) {
		this(new Key(kind, parent));
	}

	public Entity (String kind, String keyName) {
		this (new Key (kind, keyName));
	}

	public Entity (String kind, String keyName, Key parent) {
		this(parent != null ? parent.getChild(kind, keyName) : new Key (kind, keyName));
	}

	@SuppressWarnings("unchecked")
	private Entity(Key key) {
		this.key = key;
		propertyMap = new HashMap ();
	}

	public boolean equals(Object object) {
		if(object instanceof Entity) {
			Entity otherEntity = (Entity)object;
			return key.equals(otherEntity.key);
		} 

		return false;
	}

	public Key getKey() {
		return key;
	}

	public String getKind() {
		return key.getKind();
	}

	public Key getParent() {
		return key.getParent();
	}

	public Object getProperty(String propertyName) {
		return unwrapValue(propertyMap.get(propertyName));
	}

	/**
	 * The return value was actually <code>Collection.unmodifiableMap (properties)</code>
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map getProperties() {
		Map properties = new HashMap(propertyMap.size());
		java.util.Map.Entry entry;
		for(Iterator i$ = propertyMap.entrySet().iterator(); i$.hasNext(); properties.put(entry.getKey(), unwrapValue(entry.getValue())))
			entry = (java.util.Map.Entry)i$.next();

		return properties;
	}

	public int hashCode() {
		if (key == null)
			return 0;

		return key.hashCode();
	}

	public boolean hasProperty(String propertyName) {
		return propertyMap.containsKey(propertyName);
	}

	public void removeProperty(String propertyName) {
		propertyMap.remove(propertyName);
	}

	@SuppressWarnings("unchecked")
	public void setProperty(String propertyName, Object value) {
		checkSupportedValue(propertyName, value);
		propertyMap.put(propertyName, value);
	}

	@SuppressWarnings("unchecked")
	public void setUnindexedProperty(String propertyName, Object value) {
		checkSupportedValue(propertyName, value);
		propertyMap.put(propertyName, new UnindexedValue(value));
	}


	@SuppressWarnings("unchecked")
	public String toString() {
		String result = "<Entity [" + key + "]:\n";

		Iterator i = propertyMap.entrySet().iterator();
		while (i.hasNext()) {
			java.util.Map.Entry entry = (java.util.Map.Entry) i.next();
			result += "\t" + entry.getKey() + " = " + entry.getValue() + "\n";
		}

		result += ">\n";
		return result;
	}


	private static Object unwrapValue (Object obj) {
		if(obj instanceof UnindexedValue)
			return ((UnindexedValue)obj).getValue();
		else
			return obj;
	}

	@SuppressWarnings("unchecked")
	public Map getPropertyMap() {
		return propertyMap;
	}

	@SuppressWarnings("unchecked")
	private static void checkSupportedValue (String propertyName, Object value) {

		if (value instanceof Collection) {
			Collection collection = (Collection) value;
			Iterator i$ = collection.iterator();
			while (i$.hasNext()) {
				checkSingleSupportedValue (propertyName, i$.next());
			}
		} else {
			checkSingleSupportedValue (propertyName, value);
		}
	}

	/**
	 * Check for various DataNucleus class limitations (such as: 
	 * {@link com.google.appengine.api.datastore.Link} cannot contain more than 2048 characters and many others)
	 * @param propertyName
	 * @param value
	 */
	private static void checkSingleSupportedValue (String propertyName, Object value) {
		// TODO
	}
}
