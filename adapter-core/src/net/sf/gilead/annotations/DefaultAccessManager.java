/**
 * 
 */
package net.sf.gilead.annotations;

/**
 * Default access manager.
 * All properties are read-only and server-only.
 * @author bruno.marchesson
 *
 */
public class DefaultAccessManager implements IAccessManager 
{
	//-------------------------------------------------------------------------
	//
	// IAccessManager implementation
	//
	//-------------------------------------------------------------------------
	public boolean isAnnotationActive(Class<?> annotationClass, Object entity,
									  String propertyName)
	{
		return true;
	}

}
