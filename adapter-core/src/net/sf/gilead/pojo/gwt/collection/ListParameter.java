/**
 * 
 */
package net.sf.gilead.pojo.gwt.collection;

import java.io.Serializable;
import java.util.List;

import net.sf.gilead.pojo.gwt.IRequestParameter;

/**
 * List parameter.
 * @author bruno.marchesson
 *
 */
public class ListParameter implements IRequestParameter, Serializable
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
	private List<IRequestParameter> value;

	//----
	// Getter and setter
	//----
	/**
	 * Change value.
	 */
	public void setValue(List<IRequestParameter> value)
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
	public ListParameter(List<IRequestParameter> value)
	{
		this.value = value;
	}
	
	/**
	 * Empty constructor (needed by GWT)
	 */
	public ListParameter()
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
		return List.class;
	}
}
