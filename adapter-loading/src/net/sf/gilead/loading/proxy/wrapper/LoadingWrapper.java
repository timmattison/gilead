/**
 * 
 */
package net.sf.gilead.loading.proxy.wrapper;

import net.sf.gilead.loading.proxy.LoadingProxyManager;

/**
 * Abstract class for all loading wrapper
 * @author bruno.marchesson
 *
 */
public abstract class LoadingWrapper
{
	//----
	// Attribute
	//----
	/**
	 * The underlyng persistent object
	 */
	protected Object _data;
	
	//----
	// Properties
	//----
	/**
	 * @return the underliyng persistent data
	 */
	public Object getData()
	{
		return _data;
	}
	
	//-------------------------------------------------------------------------
	//
	// Internal methods
	//
	//-------------------------------------------------------------------------
	/**
	 * Wrap a persistent object with the associated loading interface
	 */
	protected Object wrapAs(Class<?> loadingInterface, Object persistentData)
	{
		return LoadingProxyManager.getInstance().wrapAs(persistentData, loadingInterface);
	}
	
	/**
	 * Wrap a list of persistent object with the associated loading interface
	 */
	protected Object wrapCollectionAs(Class<?> loadingInterface, Object list)
	{
		return LoadingProxyManager.getInstance().wrapCollectionAs(list, loadingInterface);
	}
	
	/**
	 * Unwrap a wrapper to return the underlying persistent data
	 * @param wrapper
	 * @return
	 */
	protected Object unwrap(Object wrapper)
	{
		if (wrapper instanceof LoadingWrapper)
		{
			return ((LoadingWrapper)wrapper).getData();
		}
		else
		{
		//	Not a wrapper
		//
			return wrapper;
		}
	}
	
}
