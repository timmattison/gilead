/**
 * 
 */
package net.sf.gilead.pojo.gwt.basic;

import java.io.Serializable;

import net.sf.gilead.pojo.gwt.IGwtSerializableParameter;

/**
 * Integer parameter.
 * @author bruno.marchesson
 *
 */
public class IntegerParameter implements IGwtSerializableParameter, Serializable
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
