//$Id$
package net.sf.gilead.emul.java5.ejb3.org.hibernate.annotations;

/**
 * Represent a discriminator value associated to a given entity type
 * @author Emmanuel Bernard
 */
public @interface MetaValue {
	/**
	 * entity type
	 */
	Class targetEntity();

	/**
	 * discriminator value stored in database
	 */
	String value();
}
