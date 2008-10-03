package net.sf.gilead.loading.proxy;

import java.lang.reflect.ParameterizedType;
import java.util.ListIterator;

/**
 * Wrapper for list iterator
 * @author bruno.marchesson
 *
 * @param <PROXY>
 * @param <PERSISTENT>
 */
public class LoadingListIterator<PROXY, PERSISTENT> implements ListIterator<PROXY>
{
	//----
	// Attributes
	//----
	/**
	 * Wrapped iterator
	 */
	private ListIterator<PERSISTENT> _wrapped;
	
	/**
	 * The proxy class
	 */
	private Class<?> _proxyClass;
	
	//-------------------------------------------------------------------------
	//
	// Constructor
	//
	//-------------------------------------------------------------------------
	public LoadingListIterator(ListIterator<PERSISTENT> wrapped)
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

	public void add(PROXY e)
	{
		_wrapped.add(unwrap(e));
	}

	public boolean hasPrevious()
	{
		return _wrapped.hasPrevious();
	}

	public int nextIndex()
	{
		return _wrapped.nextIndex();
	}

	public PROXY previous()
	{
		return wrap(_wrapped.previous());
	}

	public int previousIndex()
	{
		return _wrapped.previousIndex();
	}

	public void set(PROXY e)
	{
		_wrapped.set(unwrap(e));
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
	
	/**
	 * Get the underlying persistent item from a proxy
	 */
	@SuppressWarnings("unchecked")
	private PERSISTENT unwrap(Object item)
	{
		if (item instanceof LoadingWrapper)
		{
			return (PERSISTENT) ((LoadingWrapper)item).getData();
		}
		else
		{
			return (PERSISTENT) item;
		}
	}
}
