/**
 * 
 */
package net.sf.gilead.pojo.gwt.basic;

import java.io.Serializable;

import net.sf.gilead.pojo.gwt.IRequestParameter;

/**
 * Double parameter.
 * @author bruno.marchesson
 *
 */
public class DoubleParameter implements IRequestParameter, Serializable
{
	//----
	// Attributes
	//----
	/**
	 * Serialization ID.
	 */
	private static final long serialVersionUID = 2165631776081297493L;

	/**
	 * The underlying value.
	 */
	private Double value;

	//----
	// Getter and setter
	//----
	/**
	 * Change value.
	 */
	public void setValue(Double value)
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
	public DoubleParameter(Double value)
	{
		this.value = value;
	}
	
	/**
	 * Empty constructor (needed by GWT)
	 */
	public DoubleParameter()
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
		return Double.class;
	}
}
