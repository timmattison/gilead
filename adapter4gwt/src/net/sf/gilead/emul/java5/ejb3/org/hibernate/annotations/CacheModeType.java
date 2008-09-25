package net.sf.gilead.emul.java5.ejb3.org.hibernate.annotations;

/**
 * Enumeration for the different interaction modes between the session and
 * the Level 2 Cache.
 *
 * @author Emmanuel Bernard
 * @author Carlos Gonz�lez-Cadenas
 */

public enum CacheModeType {
	GET,
	IGNORE,
	NORMAL,
	PUT,
	REFRESH
} 