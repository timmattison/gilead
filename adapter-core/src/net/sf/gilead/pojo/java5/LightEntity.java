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

package net.sf.gilead.pojo.java5;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import net.sf.gilead.pojo.base.ILightEntity;

/**
 * Abstract POJO with minimal proxy informations handling
 * @author bruno.marchesson
 *
 */
public abstract class LightEntity implements ILightEntity, Serializable
{
	//-----
	// Attributes
	//-----
	/**
	 * Serialization ID
	 */
	private static final long serialVersionUID = 1061336746068017740L;

	/**
	 * Map of persistence proxy informations.
	 * The key is the property name, the value is a map with
	 * persistence informations filled by the persistence util
	 * implementation
	 */
	protected Map<String, String> _proxyInformations;
	
	/**
	 * Map of initialization state
	 * The key is the property name, the value is the initialization state.
	 */
	protected Map<String, Boolean> _initializationMap;

	//----
	// Properties
	//----
	/**
	 * @return the proxy informations
	 */
	public Map<String, String> getProxyInformations()
	{
		return _proxyInformations;
	}

	/**
	 * @param informations the proxy Informations to set
	 */
	@SuppressWarnings("unchecked")
	public void setProxyInformations(Map informations)
	{
		_proxyInformations = informations;
	}
	
	/**
	 * @return the initializationMap
	 */
	public Map<String, Boolean> getInitializationMap()
	{
		return _initializationMap;
	}

	/**
	 * @param initializationMap the initializationMap to set
	 */
	@SuppressWarnings("unchecked")
	public void setInitializationMap(Map initializationMap)
	{
		this._initializationMap = initializationMap;
	}

	//-------------------------------------------------------------------------
	//
	// Constructor
	//
	//-------------------------------------------------------------------------
	/**
	 * Constructor
	 */
	public LightEntity()
	{
		super();
	}
	
	//-------------------------------------------------------------------------
	//
	// Public interface
	//
	//-------------------------------------------------------------------------
	/**
	 * Indicates if the argument property is initialized or not
	 */
	public boolean isInitialized(String property)
	{
	//	Precondition checking
	//
		if ((_initializationMap == null) ||
			(_initializationMap.containsKey(property) == false))
		{
		//	not filled : initialized
		//
			return true;
		}
		
	//	Return initialization map value
	//
		return ((Boolean)_initializationMap.get(property)).booleanValue();
	}
	
	/**
	 * Indicates if the argument property is initialized or not
	 */
	public void setInitialized(String property, boolean initialized)
	{
		if (_initializationMap != null)
		{
			_initializationMap = new HashMap();
		}
		_initializationMap.put(property, Boolean.valueOf(initialized));
	}
	/**
	 * Add proxy information
	 */
	public void addProxyInformation(String property,
									String proxyInfo)
	{
		if (_proxyInformations == null)
		{
			_proxyInformations = new HashMap<String, String>();
		}
		_proxyInformations.put(property, proxyInfo);
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.gilead.pojo.base.ILightEntity#removeProxyInformation(java.lang.String)
	 */
	public void removeProxyInformation(String property)
	{
		if (_proxyInformations != null)
		{
			_proxyInformations.remove(property);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.gilead.pojo.base.ILightEntity#getProxyInformation(java.lang.String)
	 */
	public String getProxyInformation(String property)
	{
		if (_proxyInformations != null)
		{
			return _proxyInformations.get(property);
		}
		else
		{
			return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see org.dotnetguru.lazykiller.pojo.ILazyPojo#getLazyString()
	 */
	public String getDebugString()
	{
		if (_proxyInformations != null)
		{
			return _proxyInformations.toString();
		}
		else
		{
			return null;
		}
	}
}