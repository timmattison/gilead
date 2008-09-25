/**
 * 
 */
package net.sf.gilead.exception;

/**
 * Exception when trying to clone or merge an transient object
 * @author bruno.marchesson
 *
 */
public class TransientHibernateObjectException extends RuntimeException
{
	//----
	// Attribute
	//----
	/**
	 * Serialisation ID
	 */
	private static final long serialVersionUID = -3916689195006928705L;
	
	/**
	 * The exception object
	 */
	private Object _object;
	
	//----
	// Property
	//----
	/**
	 * @return the object
	 */
	public final Object getObject()
	{
		return _object;
	}

	//----
	// Constructor
	//----
	/**
	 * Base constructor
	 */
	public TransientHibernateObjectException(Object object)
	{
		_object = object;
	}
}
