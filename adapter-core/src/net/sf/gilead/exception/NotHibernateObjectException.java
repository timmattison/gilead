/**
 * 
 */
package net.sf.gilead.exception;

/**
 * Exception when trying to clone or merge an object not persisted with Hibernate
 * @author bruno.marchesson
 *
 */
public class NotHibernateObjectException extends RuntimeException
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
	public NotHibernateObjectException(Object object)
	{
		_object = object;
	}
}
