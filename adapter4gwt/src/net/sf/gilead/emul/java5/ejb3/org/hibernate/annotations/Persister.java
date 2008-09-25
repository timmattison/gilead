//$Id: $
package net.sf.gilead.emul.java5.ejb3.org.hibernate.annotations;

import java.lang.annotation.*;

/**
 * Specify a custom persister.
 *
 * @author Shawn Clowater
 */
@java.lang.annotation.Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Retention( RetentionPolicy.RUNTIME )
public @interface Persister {
	/** Custom persister */
	Class impl();
}
