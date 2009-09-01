/**
 * 
 */
package net.sf.gilead.gwt.client.parameters;

import java.io.Serializable;
import java.util.Date;

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
}
