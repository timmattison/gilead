/**
 * 
 */
package net.sf.gilead.gwt.client.parameters;

import java.io.Serializable;

/**
 * String parameter.
 * @author bruno.marchesson
 *
 */
public class StringParameter implements IRequestParameter, Serializable
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
	private String value;

	//----
	// Getter and setter
	//----
	/**
	 * Change value.
	 */
	public void setValue(String value)
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
	public StringParameter(String value)
	{
		this.value = value;
	}
	
	/**
	 * Empty constructor (needed by GWT)
	 */
	public StringParameter()
	{
	}
}
