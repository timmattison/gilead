/**
 * 
 */
package net.sf.gilead.core.beanlib.merge;

import java.io.Serializable;
import java.util.Map;
import java.util.Stack;

/**
 * Thread local to store BeanLib additional parameters
 * @author bruno.marchesson
 *
 */
public class BeanlibThreadLocal
{
	//----
	// Attributes
	//----
	/**
	 * Target merge persistent collection class
	 */
	private static ThreadLocal<Map<String, Serializable>> proxyInformations = 
															new ThreadLocal<Map<String,Serializable>>();
	
	/**
	 * Current clone and merge bean stack.
	 * It is used to get embedded entities (component type) parent to determine unique ID in stateful
	 * mode.
	 */
	private static ThreadLocal<Stack<Object>> beanStack = new ThreadLocal<Stack<Object>>();

	//----
	// Properties
	//----
	/**
	 * @return the proxy informations
	 */
	public static Map<String, Serializable> getProxyInformations()
	{
		return proxyInformations.get();
	}

	/**
	 * @param proxyInfo the proxy informations to set
	 */
	public static void setProxyInformations(Map<String, Serializable> proxyInfo)
	{
		BeanlibThreadLocal.proxyInformations.set(proxyInfo);
	}
	
	/**
	 * @return the bean stack
	 */
	public static Stack<Object> getBeanStack()
	{
		Stack<Object> stack = beanStack.get();
		if (stack == null)
		{
			stack = new Stack<Object>();
			beanStack.set(stack);
		}
		return stack;
	}
}
