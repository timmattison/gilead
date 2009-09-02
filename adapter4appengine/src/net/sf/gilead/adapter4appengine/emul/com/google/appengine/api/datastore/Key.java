package com.google.appengine.api.datastore;

import java.io.Serializable;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * JSNI emulation for DataNucleus {@link com.google.appengine.api.datastore.Key} class
 * @author Alex Dobjanschi (alex.dobjanschi@gmail.com)
 *
 */
public final class Key implements Serializable, Comparable {

	/**
	 * Self-explanatory - this is the parent key. May be null
	 */
    private final Key parentKey;
    
    /**
     * The kind of this key
     */
    private final String kind;
    
    /**
     * AppId, taken from {@link #appIdNamespace}.toString (). Not used since {@link #appIdNamespace} is always
     * <code>null</code>
     */
    @SuppressWarnings("unused")
	private String appId;
    
    /**
     * Id for this key
     */
    private long id;
    
    /**
     * Name of the key
     */
    private final String name;
    
    /**
     * Not used. 
     * @see #Key(String, Key, long, String, AppIdNamespace)
     */
    private transient AppIdNamespace appIdNamespace;
	
    /**
     * Needed by serialization API
     */
    @SuppressWarnings("unused")
	private Key() {
        parentKey = null;
        kind = null;
        appIdNamespace = null;
        id = 0L;
        name = null;
    }

    Key (String kind) {
        this(kind, null, 0L);
    }

    Key(String kind, String name) {
        this(kind, null, name);
    }

    Key(String kind, Key parentKey) {
        this(kind, parentKey, 0L);
    }

    Key(String kind, Key parentKey, long id) {
        this(kind, parentKey, id, null, null);
    }

    Key(String kind, Key parentKey, String name) {
        this(kind, parentKey, 0L, name, null);
    }

    /**
     * Complete constructor. All fields are specified
     * @param kind
     * 			The kind of key
     * @param parentKey
     * 			Optionally, you can specify a parent key
     * @param id
     * @param name
     * 			Name of the key
     * @param appIdNamespace The original code inits the internal {@link #appIdNamespace} even if this 
     * parameter is <code>null</code>, using the {@link DatastoreApiHelper} which cannot be emulated.
     * In this case checking for <code>null</code> cannot be done.
     *   
     */
    Key (String kind, Key parentKey, long id, String name, AppIdNamespace appIdNamespace) {
    	
        if(kind == null || kind.length() == 0)
            throw new IllegalArgumentException("No kind specified.");
        
        if(name != null) {
        	
            if(name.length() == 0)
                throw new IllegalArgumentException("Name may not be empty.");
            if(Character.isDigit(name.charAt(0)))
                throw new IllegalArgumentException("Name may not start with a digit.");
            if(id != 0L)
                throw new IllegalArgumentException("Id and name may not both be specified at once.");
            
        }
        
        this.name = name;
        this.id = id;
        this.parentKey = parentKey;
        this.kind = kind;
        this.appIdNamespace = appIdNamespace;
        this.appId = ""; // appIdNamespace.toString(); // avoid NullPointerException 
    }

    /**
     * Getter for {@link #kind}
     * @return
     */
    public String getKind() {
        return kind;
    }

    /**
     * Getter for {@link #parentKey}
     * @return
     */
    public Key getParent() {
        return parentKey;
    }

    /**
     * Getter for {@link #appIdNamespace}. This will always return <code>null</code>
     * @return
     */
    AppIdNamespace getAppIdNamespace() {
        return appIdNamespace;
    }

    /**
     * Getter for {@link #id}
     * @return
     */
    public long getId() {
        return id;
    }

    /**
     * Getter for {@link #name}
     * @return
     */
    public String getName() {
        return name;
    }
    

    public int hashCode() {
        int result = 1;
        result = 31 * result + (appIdNamespace != null ? appIdNamespace.hashCode() : 0);
        result = 31 * result + (int)(id ^ id >>> 32);
        result = 31 * result + (kind != null ? kind.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (parentKey != null ? parentKey.hashCode() : 0);
        return result;
    }

    public String toString() {
    	String result = "";
    	if (parentKey != null) {
    		result += parentKey.toString() + "/";
    	} else if (appIdNamespace != null) {
    		String namespace = appIdNamespace.getNamespace();
    		if (namespace.length() > 0) {
    			result += "!" + namespace + ":";
    		}
    	}

    	result += kind + "(";
    	if (name != null)
    		result += name + ")";
    	else if (id == 0L)
    		result += "no-id-yet)";
    	else
    		result += id + ")";
    	
    	return result;
    }

    public boolean equals (Object object) {
    	
        if(object instanceof Key)
        {
            Key key = (Key) object;
            if(this == key)
                return true;
            
            //if(!appIdNamespace.equals(key.appIdNamespace)) // skip this test since both are always null
            //   return false;
            
            if(name == null && id == 0L && key.id == 0L)
                return false;
            if(id != key.id || !kind.equals(key.kind) || name != null && !name.equals(key.name))
                return false;
            return parentKey == key.parentKey || parentKey != null && parentKey.equals(key.parentKey);
        }
        
        return false;
    }

    public Key getChild (String kind, long id) {
        if(!isComplete())
            throw new IllegalStateException("Cannot get a child of an incomplete key.");
        else
            return new Key (kind, this, id);
    }

    public Key getChild (String kind, String name) {
        if(!isComplete())
            throw new IllegalStateException("Cannot get a child of an incomplete key.");
        else
            return new Key(kind, this, name);
    }

    public boolean isComplete() {
        return id != 0L || name != null;
    }

    /**
     * Setter for {@link #id}
     * @param id
     */
    void setId(long id) {
        if(name != null)
            throw new IllegalArgumentException("Cannot set id; key already has a name.");
        
        this.id = id;
    }

    @SuppressWarnings("unchecked")
	private static Iterator getPathIterator(Key key) {
    	
        LinkedList stack = new LinkedList();
        do {
            stack.addFirst(key);
            key = key.getParent();
        } while(key != null);
        
        return stack.iterator();
    }

    private static int compareToInternal(Key thisKey, Key otherKey)
    {
        if(thisKey == otherKey)
            return 0;
        int result = thisKey.getKind().compareTo(otherKey.getKind());
        if(result != 0)
            return result;
        if(!thisKey.isComplete() && !otherKey.isComplete())
            return Integer.valueOf(thisKey.hashCode()).compareTo(Integer.valueOf(otherKey.hashCode()));
        if(thisKey.getId() != 0L)
            if(otherKey.getId() == 0L)
                return -1;
            else
                return Long.valueOf(thisKey.getId()).compareTo(Long.valueOf(otherKey.getId()));
        if(otherKey.getId() != 0L)
            return 1;
        else
            return thisKey.getName().compareTo(otherKey.getName());
    }


    /**
     * Not a safe implementation (lacks Java 1.5 generics). Also, it is not recommended to do client-side
     * key comparisons (or any other way in which this method would be called)
     */
    @SuppressWarnings("unchecked")
	public int compareTo (Object other) {
    	
        if(this == other)
            return 0;
        if (other == null)
        	return -1;
        
        Iterator thisPath  = getPathIterator (this);
        Iterator otherPath = getPathIterator ((Key) other);
        
        while(thisPath.hasNext()) {
            Key thisKey = (Key)thisPath.next();
            if(otherPath.hasNext()) {
                Key otherKey = (Key)otherPath.next();
                int result = compareToInternal(thisKey, otherKey);
                if(result != 0)
                    return result;
            } else
            	return 1;
        }
        return otherPath.hasNext() ? -1 : 0;    	
    }
}