/**
 * 
 */
package net.sf.gilead.annotations;

/**
 * Interface of transport annotation access manager
 * @author bruno.marchesson
 *
 */
public interface IAccessManager 
{
	/**
	 * Indicated is the annotation must be applied on the property 
	 * of the target entity
	 * @param annotationClass the Gilead transport annotation
	 * @param entity
	 * @param propertyName
	 * @return
	 */
	public boolean isAnnotationActive(Class<?> annotationClass, 
									  Object entity, 
									  String propertyName);
}
