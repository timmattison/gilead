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

package net.sf.gilead.core.store.stateless;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import net.sf.gilead.core.serialization.IProxySerialization;
import net.sf.gilead.core.serialization.XStreamProxySerialization;
import net.sf.gilead.core.store.IProxyStore;
import net.sf.gilead.exception.ProxyStoreException;
import net.sf.gilead.pojo.base.IConvertProxyMap;
import net.sf.gilead.pojo.base.ILightEntity;

/**
 * Stateless proxy store.
 * The proxy informations is stored on the pojo, by implementing the ILightEntity
 * interface.
 * @see ILightEntity
 * @author bruno.marchesson
 */
public class StatelessProxyStore implements IProxyStore
{
	//----
	// Attribute
	//-----
	/**
	 * Serializer for proxy informations
	 */
	private IProxySerialization _proxySerializer;
	
	/**
	 * @return the proxy serializer
	 */
	public IProxySerialization getProxySerializer()
	{
		return _proxySerializer;
	}

	/**
	 * @param serializer the serializer to set
	 */
	public void setProxySerializer(IProxySerialization serializer)
	{
		_proxySerializer = serializer;
	}
	
	//-------------------------------------------------------------------------
	//
	// Constructor
	//
	//-------------------------------------------------------------------------
	/**
	 * Constructor
	 */
	public StatelessProxyStore()
	{
		// default value
		_proxySerializer = new XStreamProxySerialization();
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
	public void storeProxyInformations(Object cloneBean, Object persistentBean, 
									   String property,
									   Map<String, Serializable> proxyInformations)
	{
	//	ILightEntity checking
	//
		if (cloneBean instanceof ILightEntity == false)
		{
			throw new ProxyStoreException("Class " + cloneBean.getClass() + " must implements ILightEntity interface !", cloneBean);
		}
		
	//	Store information in the POJO
	//
		if (cloneBean instanceof IConvertProxyMap)
		{
		//	Needs to convert Serializable map
		//
			((ILightEntity)cloneBean).addProxyInformation(property, 
			  		  									  convertSerializable(proxyInformations));
		}
		else
		{
			((ILightEntity)cloneBean).addProxyInformation(property, proxyInformations);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.gilead.core.store.IProxyStore#removeProxyInformations(java.lang.Object, java.lang.String)
	 */
	public void removeProxyInformations(Object pojo, String property)
	{
	//	ILightEntity checking
	//
		if (pojo instanceof ILightEntity)
		{	
		//	Remove information from the POJO
		//
			((ILightEntity)pojo).removeProxyInformation(property);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.gilead.core.store.IProxyStore#getProxyInformations(java.lang.Object, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Serializable> getProxyInformations(Object pojo, String property)
	{
	//	ILightEntity checking
	//
		if (pojo instanceof ILightEntity == false)
		{
			return null;
		}
		
	//	Store information in the POJO
	//
		if (pojo instanceof IConvertProxyMap)
		{
		//	Needs to convert back to Serializable
		//
			return convertToSerializable(((ILightEntity)pojo).getProxyInformation(property));
		}
		else
		{
			return ((ILightEntity)pojo).getProxyInformation(property);
		}
	}
	
	//-------------------------------------------------------------------------
	//
	// Internal methods
	//
	//-------------------------------------------------------------------------
	/**
	 * Convert Map<String,Serializable> to Map<String, Object>
	 */
	protected Map<String,Object> convertSerializable(Map<String, Serializable> map)
	{
	//	Precondition checking
	//
		if (map == null)
		{
			return null;
		}
		
	//	Convert map
	//
		Map<String,Object> result = new HashMap<String,Object>();
		
		for (Map.Entry<String, Serializable> entry : map.entrySet())
		{
			result.put(entry.getKey(),
					   _proxySerializer.serialize(entry.getValue()));
		}
		
		return result;
	}
	

	/**
	 * Convert Map<String,bytes> to Map<String, Serializable>
	 */
	protected Map<String, Serializable> convertToSerializable(Map<String, Object> map)
	{
	//	Precondition checking
	//
		if (map == null)
		{
			return null;
		}
		
	//	Convert map
	//
		Map<String, Serializable> result = new HashMap<String, Serializable>();
		
		for (Map.Entry<String, Object> entry : map.entrySet())
		{
			result.put(entry.getKey(),
					   _proxySerializer.unserialize(entry.getValue()));
		}
		
		return result;
	}
}
