//$Id: $
package net.sf.gilead.emul.java5.ejb3.org.hibernate.annotations;

import java.lang.annotation.*;

/**
 * Mark an Entity or a Collection as immutable
 * No annotation means the element is mutable
 *
 * @author Emmanuel Bernard
 */
@java.lang.annotation.Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Retention( RetentionPolicy.RUNTIME )
public @interface Immutable {
}
