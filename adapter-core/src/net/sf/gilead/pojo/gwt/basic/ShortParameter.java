/**
 * 
 */
package net.sf.gilead.pojo.gwt.basic;

import java.io.Serializable;

import net.sf.gilead.pojo.gwt.IRequestParameter;

/**
 * Short parameter.
 * @author bruno.marchesson
 *
 */
public class ShortParameter implements IRequestParameter, Serializable
{
	//----
	// Attributes
	//----
	/**
	 * Serialization ID.
	 */
	private static final long serialVersionUID = 2165631776081297491L;

	/**
	 * The underlying value.
	 */
	private Short value;

	//----
	// Getter and setter
	//----
	/**
	 * Change value.
	 */
	public void setValue(Short value)
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
	public ShortParameter(Short value)
	{
		this.value = value;
	}
	
	/**
	 * Empty constructor (needed by GWT)
	 */
	public ShortParameter()
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
		return Short.class;
	}
}
