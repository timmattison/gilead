/**
 * 
 */
package net.sf.gilead.pojo.gwt.basic;

import java.io.Serializable;
import java.util.Date;

import net.sf.gilead.pojo.gwt.IRequestParameter;

/**
 * Date parameter.
 * @author bruno.marchesson
 *
 */
public class DateParameter implements IRequestParameter, Serializable
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
	private Date value;

	//----
	// Getter and setter
	//----
	/**
	 * Change value.
	 */
	public void setValue(Date value)
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
	public DateParameter(Date value)
	{
		this.value = value;
	}
	
	/**
	 * Empty constructor (needed by GWT)
	 */
	public DateParameter()
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
		return Date.class;
	}
}
