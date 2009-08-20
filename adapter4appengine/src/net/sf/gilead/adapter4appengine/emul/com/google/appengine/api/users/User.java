package com.google.appengine.api.users;

import java.io.Serializable;


/**
 * JSNI emulation of DataNucleus {@link com.google.appengine.api.users.User} class
 * @author Alex Dobjanschi
 *
 */
public class User implements Serializable, Comparable {
	
	/**
	 * Email of the user (required)
	 */
	private String email;
	
	/**
	 * The authentication domain (required)
	 */
	private String authDomain;

	
	/**
	 * Default constructor (required by serialization API, hidden from usage)
	 */
	private User () {}
	
	/**
	 * Constructor
	 * @param email User's email
	 * @param authDomain User's authentication domain
	 * @throws IllegalArgumentException Thrown if either specified <b>email</b> or <b>authDomain</b> is <code>null</code>.
	 * This exception is <b>unchecked</b> (no need to intercept it with <code>try { ... } catch ( ...) { ... } </code> blocks)
	 */
	public User (String email, String authDomain) {
		
		if (email == null)
			throw new IllegalArgumentException ("email must be specified");
		if (authDomain == null)
			throw new IllegalArgumentException ("authDomain must be specified");
		
		this.email = email;
		this.authDomain = authDomain;
	}
	
	/**
	 * Returns the nickname of the user. The {@link #email} comes in 2 flavours:
	 * <lu>
	 *   <li><b>name@domain</b> - The <b>domain</b> ending is actually {@link #authDomain}. If this is found, 
	 *   just return a substring containing only the <b>name</b></li>
	 *   <li><b>name</b> - No domain endings were found, simply return the <b>name</b></li>
	 * </lu>
	 * @return
	 * 		The username for this User
	 */
	public String getNickname () {
		
		int indexOfDomain = email.indexOf("@" + authDomain);
		if (indexOfDomain == -1) {
			return email;
		}
		
		return email.substring(0, indexOfDomain);
	}
	

	/**
	 * Returns complete email (may contain {@link #authDomain})
	 * @return
	 */
	public String getEmail () {
		return email;
	}

	/**
	 * Returns the authentication domain
	 * @return
	 */
	public String getAuthDomain() {
		return authDomain;
	}

	@Override
	public int hashCode() {		
		return 17 * email.hashCode() + authDomain.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		
		if (obj instanceof User) {
		
			User other = (User) obj;
			return (email.equals(other.getEmail()) && 
					authDomain.equals(other.getAuthDomain()));
			
		} else
			return false;
	}

	@Override
	public String toString() {		
		return email;
	}
	
	/**
	 * The private comparator for this class
	 * @param user The other user to be compared with
	 * @return
	 */
	private int compareTo (User user) {
		return email.compareTo(user.email);
	}
	
	/**
	 * Implementation of {@link Comparable#compareTo(Object)}.
	 * <br/>
	 * <b>The behaviour of this implementation is dangerous because <i>generics</i> are not used!</b>
	 * Since the other instance is {@link Object}, there is no way to make sure a {@link #ClassCastException} will not
	 * be thrown (unchecked exception, disrupting thread's execution context)
	 */
	@Override
	public int compareTo (Object o) {
		return compareTo ((User) o);
	}
	
}
