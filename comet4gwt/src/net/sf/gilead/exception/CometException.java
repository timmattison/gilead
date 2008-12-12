/**
 * 
 */
package net.sf.gilead.exception;

/**
 * Exception sent by Comet
 * @author bruno.marchesson
 *
 */
public class CometException extends RuntimeException
{

	/**
	 * Serialisation id
	 */
	private static final long serialVersionUID = -4190772828377272521L;

	//-------------------------------------------------------------------------
	//
	// Constructors
	//
	//-------------------------------------------------------------------------
	/**
	 * @param arg0
	 * @param arg1
	 */
	public CometException(String arg0, Throwable arg1)
	{
		super(arg0, arg1);
	}

	/**
	 * @param arg0
	 */
	public CometException(String arg0)
	{
		super(arg0);
	}
}
