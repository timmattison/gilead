/**
 * 
 */
package net.sf.gilead.pojo.gwt.collection;

import java.io.Serializable;
import java.util.Set;

import net.sf.gilead.pojo.gwt.IRequestParameter;

/**
 * Set parameter.
 * @author bruno.marchesson
 *
 */
public class SetParameter implements IRequestParameter, Serializable
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
	private Set<IRequestParameter> value;

	//----
	// Getter and setter
	//----
	/**
	 * Change value.
	 */
	public void setValue(Set<IRequestParameter> value)
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
	public SetParameter(Set<IRequestParameter> value)
	{
		this.value = value;
	}
	
	/**
	 * Empty constructor (needed by GWT)
	 */
	public SetParameter()
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
		return Set.class;
	}
}
