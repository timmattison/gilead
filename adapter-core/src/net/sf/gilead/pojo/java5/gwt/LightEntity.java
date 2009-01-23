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

package net.sf.gilead.pojo.java5.gwt;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import net.sf.gilead.pojo.base.IConvertProxyMap;
import net.sf.gilead.pojo.base.ILightEntity;

/**
 * Abstract POJO with minimal proxy informations handling
 * @author bruno.marchesson
 *
 */
public abstract class LightEntity implements ILightEntity, IConvertProxyMap, Serializable
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
	protected Map<String, Map<String, String>> _proxyInformations;
	
	//----
	// Properties
	//----
	/**
	 * @return the proxy informations
	 */
	public Map<String, Map<String, String>> getProxyInformations()
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
	/*
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void addProxyInformation(String property,
									Map	proxyInfo)
	{
		if (_proxyInformations == null)
		{
			_proxyInformations = new HashMap<String, Map<String, String>>();
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
	@SuppressWarnings("unchecked")
	public Map getProxyInformation(String property)
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