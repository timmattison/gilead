/**
 * 
 */
package net.sf.gilead.pojo.gwt;

import java.io.Serializable;

/**
 * Marker interface for valid request parameters.
 * It is needed because GWT does not support a list of Object instance, even if each element
 * in the list is serializable
 * @author bruno.marchesson
 *
 */
public interface IRequestParameter extends Serializable 
{
	/**
	 * @return the parameter class.
	 */
	public Class<?> getParameterClass();
	
	/**
	 * @return the underlying value.
	 */
	public Object getValue();
}
