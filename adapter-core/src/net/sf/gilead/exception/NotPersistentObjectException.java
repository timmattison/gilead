/**
 * 
 */
package net.sf.gilead.exception;

/**
 * Exception when trying to clone or merge an object not persisted with Persistence engine
 * @author bruno.marchesson
 *
 */
public class NotPersistentObjectException extends RuntimeException
{
	//----
	// Attribute
	//----
	/**
	 * Serialisation ID
	 */
	private static final long serialVersionUID = 3274347637647294793L;

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
	public NotPersistentObjectException(Object object)
	{
		_object = object;
	}
}
