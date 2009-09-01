/**
 * 
 */
package net.sf.gilead.gwt.client.parameters;

import java.io.Serializable;

import net.sf.gilead.pojo.base.ILightEntity;

/**
 * Light Entity parameter.
 * @author bruno.marchesson
 *
 */
public class LightEntityParameter implements IRequestParameter, Serializable
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
	private ILightEntity value;

	//----
	// Getter and setter
	//----
	/**
	 * Change value.
	 */
	public void setValue(ILightEntity value)
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
	public LightEntityParameter(ILightEntity value)
	{
		this.value = value;
	}
	
	/**
	 * Empty constructor (needed by GWT)
	 */
	public LightEntityParameter()
	{
	}
}
