/**
 * 
 */
package net.sf.gilead.loading.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Loading interface annotation.
 * Each loading interface must hold this annotation.
 * @author bruno.marchesson
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface LoadingInterface
{
	//----
	// Property
	//----
	/**
	 * The underlying persistent class
	 */
	Class<?> persistentClass();
}
