/**
 * 
 */
package net.sf.gilead.gwt.client.parameters;

import java.io.Serializable;

/**
 * Long parameter.
 * @author bruno.marchesson
 *
 */
public class LongParameter implements IRequestParameter, Serializable
{
	//----
	// Attributes
	//----
	/**
	 * Serialization ID.
	 */
	private static final long serialVersionUID = 2165631776081297492L;

	/**
	 * The underlying value.
	 */
	private Long value;

	//----
	// Getter and setter
	//----
	/**
	 * Change value.
	 */
	public void setValue(Long value)
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
	public LongParameter(Long value)
	{
		this.value = value;
	}
	
	/**
	 * Empty constructor (needed by GWT)
	 */
	public LongParameter()
	{
	}
}
