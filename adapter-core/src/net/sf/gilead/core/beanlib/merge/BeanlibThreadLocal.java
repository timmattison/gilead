/**
 * 
 */
package net.sf.gilead.core.beanlib.merge;

import java.io.Serializable;
import java.util.Map;

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
}
