package net.sf.gilead.loading;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import org.hibernate.type.Type;

/**
 * Structure containing all fetching context
 * (the current hierarchy stack, fetch associations, etc...
 * @author bruno.marchesson
 *
 */
public class FetchContext
{
	//----
	// Attributes
	//----
	/**
	 * The hierarchy stack
	 */
	private Stack<PropertyDescriptor> _hierarchyStack;
	
	/**
	 * Fetched type list.
	 * A type must be fetched only once to prevent loop.
	 */
	private Set<Type> _fetchTypeList;
	
	//----
	// Properties
	//----
	/**
	 * @return the hierarchyStack
	 */
	public Stack<PropertyDescriptor> getHierarchyStack() {
		return _hierarchyStack;
	}

	/**
	 * @param hierarchyStack the hierarchyStack to set
	 */
	public void setHierarchyStack(Stack<PropertyDescriptor> hierarchyStack) {
		this._hierarchyStack = hierarchyStack;
	}
	
	//-------------------------------------------------------------------------
	//
	// Constructor
	//
	//-------------------------------------------------------------------------
	/**
	 * Constructor
	 */
	public FetchContext()
	{
		_fetchTypeList = new HashSet<Type>();
		_hierarchyStack = new Stack<PropertyDescriptor>();
	}
	
	//------------------------------------------------------------------------
	//
	// Public interface
	//
	//------------------------------------------------------------------------
	/**
	 * Indicates if the type has already be fetched
	 */
	public boolean hasFetchedType(Type type)
	{
		return _fetchTypeList.contains(type);
	}
	
	/**
	 * Add a fetch type to the fetched list.
	 * @param type
	 */
	public void addFetchType(Type type)
	{
		_fetchTypeList.add(type);
	}
}
