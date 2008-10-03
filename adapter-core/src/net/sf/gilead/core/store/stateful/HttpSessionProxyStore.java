/*
 * Copyright 2007 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.sf.gilead.core.store.stateful;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import net.sf.gilead.core.IPersistenceUtil;
import net.sf.gilead.core.store.IProxyStore;
import net.sf.gilead.exception.NotPersistentObjectException;
import net.sf.gilead.exception.ProxyStoreException;
import net.sf.gilead.exception.TransientObjectException;

/**
 * Proxy store for stateful web application
 * @author bruno.marchesson
 *
 */
public class HttpSessionProxyStore implements IProxyStore
{
	//----
	// Attributes
	//----
	/**
	 * The storage thread local
	 */
	private static ThreadLocal<HttpSession> _httpSession = new ThreadLocal<HttpSession>();
	
	/**
	 * The associated persistence util
	 */
	protected IPersistenceUtil _persistenceUtil;
	
	//----
	// Properties
	//----
	/**
	 * Store the current HTTP session in the thread local
	 */
	public static void setHttpSession(HttpSession session)
	{
		_httpSession.set(session);
	}
	
	/**
	 * @return the persistence Util implementation
	 */
	public IPersistenceUtil getPersistenceUtil() {
		return _persistenceUtil;
	}

	/**
	 * @param persistenceUtil the persistence Util to set
	 */
	public void setPersistenceUtil(IPersistenceUtil persistenceUtil) {
		this._persistenceUtil = persistenceUtil;
	}

	//-------------------------------------------------------------------------
	//
	// IProxyStore implementation
	//
	//-------------------------------------------------------------------------
	/*
	 * (non-Javadoc)
	 * @see net.sf.gilead.core.store.IProxyStore#storeProxyInformations(java.lang.Object, java.lang.String, java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	public void storeProxyInformations(Object pojo, Serializable pojoId, String property,
									   Map<String, Serializable> proxyInformations)
	{
	//	Compute pojo name
	//
		String id = UniqueNameGenerator.generateUniqueName(pojoId, _persistenceUtil.getUnenhancedClass(pojo.getClass()));
		
	//	Create POJO map if needed
	//
		HttpSession httpSession = getSession();
		Map<String, Map<String,Serializable>> pojoMap = 
			(Map<String, Map<String,Serializable>>) httpSession.getAttribute(id);
		
		if (pojoMap == null)
		{
			pojoMap = new HashMap<String, Map<String,Serializable>>();
		}
		
	//	Store proxy information in pojo map and the latter in HTTP session
	//
		pojoMap.put(property, proxyInformations);
		httpSession.setAttribute(id, pojoMap);
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.gilead.core.store.IProxyStore#getProxyInformations(java.lang.Object, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Serializable> getProxyInformations(Object pojo,
														  String property)
    {
	//	Compute pojo name
	//
		String id = null;
		try
		{
			id = UniqueNameGenerator.generateUniqueName(_persistenceUtil, pojo);
		}
		catch (TransientObjectException e)
		{
			return null;
		}
		catch (NotPersistentObjectException e)
		{
			return null;
		}
		
	//	Create POJO map if needed
	//
		HttpSession httpSession = getSession();
		Map<String, Map<String,Serializable>> pojoMap = 
			(Map<String, Map<String,Serializable>>) httpSession.getAttribute(id);
		
		if (pojoMap == null)
		{
			return null;
		}
		else
		{
			return pojoMap.get(property);
		}
	}


	
	/*
	 * (non-Javadoc)
	 * @see net.sf.gilead.core.store.IProxyStore#removeProxyInformations(java.lang.Object, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public void removeProxyInformations(Object pojo, String property)
	{
	//	Compute pojo name
	//
		String id = UniqueNameGenerator.generateUniqueName(_persistenceUtil, pojo);
		
	//	Create POJO map if needed
	//
		HttpSession httpSession = getSession();
		Map<String, Map<String,Serializable>> pojoMap = 
			(Map<String, Map<String,Serializable>>) httpSession.getAttribute(id);
		
		if (pojoMap != null)
		{
			pojoMap.remove(property);
			if (pojoMap.isEmpty())
			{
			//	Remove map from HTTP session
			//
				httpSession.removeAttribute(id);
			}
			else
			{
			//	Store updated map in HTTP session
			//
				httpSession.setAttribute(id, pojoMap);
			}
		}
	}
	
	//-------------------------------------------------------------------------
	//
	// Internal methods
	//
	//------------------------------------------------------------------------
	/**
	 * @return the HTTP session stored in thread local
	 */
	private HttpSession getSession()
	{
		HttpSession session = (HttpSession) _httpSession.get();
		if (session == null)
		{
			throw new ProxyStoreException("No HTTP session stored", null);
		}
		return session;
	}
}
