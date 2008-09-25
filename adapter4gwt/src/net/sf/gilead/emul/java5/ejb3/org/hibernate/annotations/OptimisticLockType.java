//$Id: OptimisticLockType.java 11282 2007-03-14 22:05:59Z epbernard $
package net.sf.gilead.emul.java5.ejb3.org.hibernate.annotations;

/**
 * Optimistic locking strategy
 * VERSION is the default and recommanded one
 *
 * @author Emmanuel Bernard
 */
public enum OptimisticLockType {
	/**
	 * no optimistic locking
	 */
	NONE,
	/**
	 * use a column version
	 */
	VERSION,
	/**
	 * dirty columns are compared
	 */
	DIRTY,
	/**
	 * all columns are compared
	 */
	ALL
}
