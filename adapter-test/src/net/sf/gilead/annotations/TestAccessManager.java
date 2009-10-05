/**
 * 
 */
package net.sf.gilead.annotations;

/**
 * Role based test access manager.
 * @author bruno.marchesson
 *
 */
public class TestAccessManager implements IAccessManager 
{
	//----
	// Enum
	//----
	/**
	 * The role enum
	 * @author bruno.marchesson
	 *
	 */
	public enum Role
	{
		user,
		admin
	}
	
	//----
	// Attribute
	//----
	/**
	 * The current role.
	 */
	protected Role role;
	
	//----
	// Properties
	//----
	/**
	 * @return the role
	 */
	public Role getRole() {
		return role;
	}

	/**
	 * @param role the role to set
	 */
	public void setRole(Role roletoSet) {
		role = roletoSet;
	}

	//-------------------------------------------------------------------------
	//
	// IAccessManager implementation
	//
	//-------------------------------------------------------------------------
	/* (non-Javadoc)
	 * @see net.sf.gilead.annotations.IAccessManager#isAnnotationActive(java.lang.Class, java.lang.Object, java.lang.String)
	 */
	public boolean isAnnotationActive(Class<?> annotationClass, Object entity,
									  String propertyName)
	{
		if (role == Role.admin)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
}
