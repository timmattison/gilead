/**
 * 
 */
package net.sf.gilead.loading.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Join association on interface
 * @author bruno.marchesson
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Join
{
	//----
	// Enumeration
	//----
	public enum JoinType { INNER, LEFT_OUTER, RIGHT_OUTER, FULL };

	//----
	// Properties
	//----
	/**
	 * The value of Join strategy
	 */
	JoinType value() default JoinType.INNER;
}
