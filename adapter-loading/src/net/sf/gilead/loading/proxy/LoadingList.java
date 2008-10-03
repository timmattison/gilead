/**
 * 
 */
package net.sf.gilead.loading.proxy;

import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Wrapper for List implementation
 * @author bruno.marchesson
 *
 */
public class LoadingList<PROXY, PERSISTENT> implements List<PROXY>
{
	//----
	// Attribute
	//----
	/**
	 * The wrapped list
	 */
	private List<PERSISTENT> _wrapped;
	
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
	public LoadingList(List<PERSISTENT> wrapped)
	{
		_wrapped = wrapped;
		
		_proxyClass = (Class<?>) ((ParameterizedType) 
								 getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	//-------------------------------------------------------------------------
	//
	// List public interface
	//
	//-------------------------------------------------------------------------
	public void add(int arg0, PROXY arg1)
	{
		_wrapped.add(arg0, unwrapLoadingInterface(arg1));
	}

	public boolean add(PROXY arg0)
	{
		return _wrapped.add(unwrapLoadingInterface(arg0));
	}

	public boolean addAll(Collection<? extends PROXY> arg0)
	{
		boolean changed = false;
		
		for (PROXY proxy : arg0)
		{
			if (_wrapped.add(unwrapLoadingInterface(proxy)))
			{
				changed = true;
			}
		}
		
		return changed;
	}

	public boolean addAll(int index, Collection<? extends PROXY> arg1)
	{
		for (PROXY proxy : arg1)
		{
			_wrapped.add(index++, unwrapLoadingInterface(proxy));
		}
		
		return true;
	}

	public void clear()
	{
		_wrapped.clear();
	}

	public boolean contains(Object arg0)
	{
		return _wrapped.contains(unwrap(arg0));
	}

	public boolean containsAll(Collection<?> arg0)
	{
		// TODO
		return _wrapped.containsAll(arg0);
	}

	public boolean equals(Object arg0)
	{
		return _wrapped.equals(arg0);
	}

	public PROXY get(int arg0)
	{
		return wrap(_wrapped.get(arg0));
	}

	public int hashCode()
	{
		return _wrapped.hashCode();
	}

	public int indexOf(Object arg0)
	{
		return _wrapped.indexOf(unwrap(arg0));
	}

	public boolean isEmpty()
	{	
		return _wrapped.isEmpty();
	}

	public Iterator<PROXY> iterator()
	{
		return new LoadingIterator<PROXY, PERSISTENT>(_wrapped.iterator());
	}

	public int lastIndexOf(Object arg0)
	{
		return _wrapped.lastIndexOf(unwrap(arg0));
	}

	public ListIterator<PROXY> listIterator()
	{
		return new LoadingListIterator<PROXY, PERSISTENT>(_wrapped.listIterator());
	}

	public ListIterator<PROXY> listIterator(int arg0)
	{
		return new LoadingListIterator<PROXY, PERSISTENT>(_wrapped.listIterator(arg0));
	}

	public PROXY remove(int arg0)
	{
		return wrap(_wrapped.remove(arg0));
	}

	public boolean remove(Object arg0)
	{
		return _wrapped.remove(unwrap(arg0));
	}

	public boolean removeAll(Collection<?> arg0)
	{
		// TODO
		return _wrapped.removeAll(arg0);
	}

	public boolean retainAll(Collection<?> arg0)
	{
		// TODO
		return _wrapped.retainAll(arg0);
	}

	public PROXY set(int arg0, PROXY arg1)
	{
		return wrap(_wrapped.set(arg0, unwrapLoadingInterface(arg1)));
	}

	public int size()
	{
		return _wrapped.size();
	}

	public List<PROXY> subList(int arg0, int arg1)
	{
		return new LoadingList<PROXY, PERSISTENT>(_wrapped.subList(arg0, arg1));
	}

	public Object[] toArray()
	{
	//	Create copy array
	//
		Object[] copy = (Object[])java.lang.reflect.Array.newInstance(_proxyClass, _wrapped.size());
		
	//	Wrap every element
	//
		int index = 0;
		for (PERSISTENT persistent : _wrapped)
		{
			copy[index++] = wrap(persistent);
		}
		
	//	Return copy
	//
		return copy;
	}

	public <T> T[] toArray(T[] arg0)
	{
		// TODO ?
		return _wrapped.toArray(arg0);
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
	private PERSISTENT unwrapLoadingInterface(Object item)
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
	
	/**
	 * Get the underlying persistent item from a proxy
	 */
	@SuppressWarnings("unchecked")
	private Object unwrap(Object item)
	{
		if (item instanceof LoadingWrapper)
		{
			return ((LoadingWrapper)item).getData();
		}
		else
		{
			return item;
		}
	}
}
