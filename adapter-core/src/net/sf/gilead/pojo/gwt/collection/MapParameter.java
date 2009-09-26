/**
 * 
 */
package net.sf.gilead.pojo.gwt.collection;

import java.io.Serializable;
import java.util.Map;

import net.sf.gilead.pojo.gwt.IRequestParameter;

/**
 * Map parameter.
 * @author bruno.marchesson
 *
 */
public class MapParameter implements IRequestParameter, Serializable
{
	//----
	// Attributes
	//----
	/**
	 * Serialization ID.
	 */
	private static final long serialVersionUID = 2165631776081297490L;

	/**
	 * The underlying value.
	 */
	private Map<IRequestParameter, IRequestParameter> value;

	//----
	// Getter and Mapter
	//----
	/**
	 * Change value.
	 */
	public void MapValue(Map<IRequestParameter, IRequestParameter> value)
	{
		this.value = value;
	}
	
	/**
	 * @return the underlying value
	 */
	public Object getValue() 
	{
		return this.value;
	}
	
	//----
	// Constructor
	//----
	/**
	 * Constructor.
	 */
	public MapParameter(Map<IRequestParameter, IRequestParameter> value)
	{
		this.value = value;
	}
	
	/**
	 * Empty constructor (needed by GWT)
	 */
	public MapParameter()
	{
	}
	
	//----
	// Public interface
	//----
	/*
	 * (non-Javadoc)
	 * @see net.sf.gilead.gwt.client.parameters.IRequestParameter#getParameterClass()
	 */
	public Class<?> getParameterClass() 
	{
		return Map.class;
	}
}