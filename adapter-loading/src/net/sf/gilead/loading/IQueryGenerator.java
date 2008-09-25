/**
 * 
 */
package net.sf.gilead.loading;

import java.beans.PropertyDescriptor;
import java.util.Stack;

import org.hibernate.SessionFactory;

/**
 * Interface of query generator service.
 * @author bruno.marchesson
 *
 */
public interface IQueryGenerator
{
	//-------------------------------------------------------------------------
	//
	// Public interface
	//
	//-------------------------------------------------------------------------
	/**
	 * Create the needed query.
	 */
	public abstract IQuery createQuery(SessionFactory sessionFactory,
									   Class<?> loadingInterface,
									   String root);
	
	/**
	 * Add a fetch instruction
	 * @param the hierarchy stack.
	 * @param descriptor the property descriptor
	 * @param query the query to update
	 */
	public abstract void addFetchInstruction(Stack<PropertyDescriptor> hierarchyStack,
											 PropertyDescriptor descriptor, 
											 IQuery query);
}
