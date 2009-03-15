package net.sf.gilead.loading.proxy.wrapper;

import java.lang.reflect.ParameterizedType;
import java.util.Iterator;

import net.sf.gilead.loading.proxy.LoadingProxyManager;

/**
 * Wrapper for iterator
 * @author bruno.marchesson
 *
 * @param <PROXY>
 * @param <PERSISTENT>
 */
public class LoadingIterator<PROXY, PERSISTENT> implements Iterator<PROXY>
{
	//----
	// Attributes
	//----
	/**
	 * Wrapped iterator
	 */
	private Iterator<PERSISTENT> _wrapped;
	
	/**
	 * The proxy class
	 */
	private Class<?> _proxyClass;
	
	//-------------------------------------------------------------------------
	//
	// Constructor
	//
	//-------------------------------------------------------------------------
	/**
	 * Constructor
	 */
	public LoadingIterator(Iterator<PERSISTENT> wrapped)
	{
		_wrapped = wrapped;
		_proxyClass = (Class<?>) ((ParameterizedType) 
				 getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
	//--------------------------------------------------------------------------
	//
	// List Iterator implementation
	//
	//--------------------------------------------------------------------------
	public boolean hasNext()
	{
		return _wrapped.hasNext();
	}

	public PROXY next()
	{
		return wrap(_wrapped.next());
	}

	public void remove()
	{
		_wrapped.remove();
	}

	//-------------------------------------------------------------------------
	//
	// Internal method
	//
	//-------------------------------------------------------------------------
	/**
	 * Wrap persistent item in a proxy
	 */
	@SuppressWarnings("unchecked")
	private PROXY wrap(PERSISTENT item)
	{
		return (PROXY) LoadingProxyManager.getInstance().wrapAs(item, _proxyClass);
	}
}
