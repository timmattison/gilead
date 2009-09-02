package com.google.appengine.api.datastore;

import java.io.Serializable;

//import com.google.gwt.core.client.GWT;

/**
 * JSNI emulation of DataNucleus {@link com.google.appengine.api.datastore.AppIdNamespace} class
 * @author Alex Dobjanschi (alex.dobjanschi@gmail.com)
 *
 */
public class AppIdNamespace implements Serializable, Comparable {
	
	private final String appId;
	private final String namespace;
	
	public AppIdNamespace (String appId, String namespace) {
		if (appId == null || namespace == null) {
			throw new IllegalArgumentException("appId or namespace may not be null");
		}
		
		if (appId.indexOf('!') != -1 || namespace.indexOf('!') != -1) {
			throw new IllegalArgumentException("appId or namespace cannot contain '!'");
		}
		
		this.appId = appId;
		this.namespace = namespace;
	}

	public int compareTo (Object o) {
		
		// TODO A ClassCastException could be thrown here - use Java 1.5 Comparable instead
		//
		AppIdNamespace other = (AppIdNamespace) o;
		
		int appidCompare = appId.compareTo(other.appId);
		if (appidCompare == 0)
			return namespace.compareTo(other.namespace);
		
		return 0;
	}

	public String getAppId() {
		return appId;
	}

	public String getNamespace() {
		return namespace;
	}

	public boolean equals(Object obj) {
		
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AppIdNamespace other = (AppIdNamespace) obj;
		if (appId == null) { 
			if (other.appId != null)
				return false;
		} else if (!appId.equals(other.appId))
			return false;
		
		if (namespace == null) {
			if (other.namespace != null)
				return false;
		}
		
		return namespace.equals (other.namespace);
	}

	public int hashCode() {
		int result = 1;
		result = 31 * result + (appId != null ? appId.hashCode() : 0);
		result = 31 * result + (namespace != null ? namespace.hashCode() : 0);
		
		return result;
	}

	public String toString() {
		
		if (namespace.equals(""))
			return appId;
		else
			return appId + "!" + namespace;
	}
	
	/**
	 * Generate a {@link AppIdNamespace} from an encoded string {@link encodedAppIdNamespace}
	 * @param encodedAppIdNamespace The encoded appId
	 * @return
	 */
	public static AppIdNamespace parseEncodedAppIdNamespace (String encodedAppIdNamespace) {
		
		// make sure we are using this on server-side
		// But we could certainly create {@link AppIdNamespace} objects on client-side too
		//assert (!GWT.isClient());
		
		if (encodedAppIdNamespace == null)
			throw new IllegalArgumentException ("appIdNamespace string may not be null");
		int index = encodedAppIdNamespace.indexOf('!');
		if (index == -1)
			return new AppIdNamespace (encodedAppIdNamespace, "");
		
		String appId = encodedAppIdNamespace.substring(0, index);
		String namespace = encodedAppIdNamespace.substring(index + 1);
		if (namespace.length() == 0) 
			throw new IllegalArgumentException ("appIdNamespace string with empty namespace many not contain '!'");
		
		return new AppIdNamespace (appId, namespace);
	}
}
