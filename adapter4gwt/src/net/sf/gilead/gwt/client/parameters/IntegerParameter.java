/**
 * 
 */
package net.sf.gilead.gwt.client.parameters;

import java.io.Serializable;

/**
 * Integer parameter.
 * @author bruno.marchesson
 *
 */
public class IntegerParameter implements IRequestParameter, Serializable
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
	private Integer value;

	//----
	// Getter and setter
	//----
	/**
	 * Change value.
	 */
	public void setValue(Integer value)
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
	public IntegerParameter(Integer value)
	{
		this.value = value;
	}
	
	/**
	 * Empty constructor (needed by GWT)
	 */
	public IntegerParameter()
	{
	}
}