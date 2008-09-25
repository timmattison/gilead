/**
 * 
 */
package net.sf.gilead.loading.hql;

import net.sf.gilead.loading.IQuery;

/**
 * HQL query wrapper
 * @author bruno.marchesson
 *
 */
public class HQLQuery implements IQuery
{
	//----
	// Attributes
	//----
	/**
	 * The associated request.
	 */
	private StringBuilder _request = new StringBuilder();

	//----
	// Properties
	//----
	/**
	 * @return the request
	 */
	public StringBuilder getRequest()
	{
		return _request;
	}

	/**
	 * @param request the request to set
	 */
	public void setRequest(StringBuilder request)
	{
		_request = request;
	}
	
	//----
	// Public interface
	//----
	@Override
	public String toString()
	{
		return _request.toString();
	}
}
