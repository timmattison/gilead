/**
 * 
 */
package net.sf.gilead.core.store.stateful;

import java.io.Serializable;

import net.sf.gilead.core.IPersistenceUtil;

/**
 * Static class for unique name generation
 * @author BMARCHESSON
 *
 */
public class UniqueNameGenerator
{
	
	//-------------------------------------------------------------------------
	//
	// Public method
	//
	//-------------------------------------------------------------------------
	/**
	 * Generates a unique name for the argument Hibenrate pojo
	 */
	public static String generateUniqueName(IPersistenceUtil persistenceUtil, Object hibernatePojo)
	{
		return generateUniqueName(persistenceUtil, hibernatePojo, hibernatePojo.getClass());
	}
	
	/**
	 * Generates a unique name for the argument DTO associated with the hibernateClass
	 */
	public static String generateUniqueName(IPersistenceUtil persistenceUtil, 
											Object pojo, Class<?> hibernateClass)
	{
	//	Get ID
	//
		Serializable id = persistenceUtil.getId(pojo, hibernateClass);
		
	//	Format unique name
	//
		return generateUniqueName(id, hibernateClass);
	}
	
	/**
	 * Generates a unique name for the argument DTO associated with the hibernateClass
	 */
	public static String generateUniqueName(Serializable id, Class<?> hibernateClass)
	{
	//	Format unique name
	//
		StringBuffer buffer = new StringBuffer();
		buffer.append(hibernateClass.getName());
		buffer.append('@');
		buffer.append(id.toString());
		
		return buffer.toString();
	}
}
