/**
 * 
 */
package net.sf.gilead.loading.exception;

/**
 * Exception thrown when the class does not denote a loading interface.
 * @author bruno.marchesson
 *
 */
public class NoLoadingInterfaceException extends RuntimeException
{
	//----
	// Attributes
	//----
	/**
	 * The error class
	 */
	private Class<?> _class;
	
	//----
	// Constructor
	//----
	/**
	 * Constructor
	 */
	public NoLoadingInterfaceException(Class<?> clazz)
	{
		_class = clazz;
	}
	
	//----
	// Public interface
	//----
	@Override
	public String getMessage()
	{
		return "Not a loading interface : " + _class;
	}
}
