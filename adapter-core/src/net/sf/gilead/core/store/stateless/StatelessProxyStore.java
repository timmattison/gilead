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
import java.util.Map;

import net.sf.gilead.core.serialization.IProxySerialization;
import net.sf.gilead.core.serialization.JBossProxySerialization;
import net.sf.gilead.core.store.IProxyStore;
import net.sf.gilead.exception.ProxyStoreException;
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
		//_proxySerializer = new XStreamProxySerialization();
		// _proxySerializer = new ByteStringProxySerialization();
		_proxySerializer = new JBossProxySerialization();
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
		((ILightEntity)cloneBean).addProxyInformation(property, 
			  		  								  convertMap(proxyInformations));
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
	public Map<String, Serializable> getProxyInformations(Object pojo, String property)
	{
	//	ILightEntity checking
	//
		if (pojo instanceof ILightEntity == false)
		{
			return null;
		}
		
		return convertToSerializable(((ILightEntity)pojo).getProxyInformation(property));
	}
	
	//-------------------------------------------------------------------------
	//
	// Internal methods
	//
	//-------------------------------------------------------------------------
	/**
	 * Convert Map<String,Serializable> to Map<String, Object>
	 */
	protected String convertMap(Map<String, Serializable> map)
	{
	//	Precondition checking
	//
		if (map == null)
		{
			return null;
		}
		
	//	Convert map
	//
		return (String) _proxySerializer.serialize((Serializable)map);
	}
	

	/**
	 * Convert Map<String,bytes> to Map<String, Serializable>
	 */
	protected Map<String, Serializable> convertToSerializable(String serialized)
	{
	//	Precondition checking
	//
		if (serialized == null)
		{
			return null;
		}
		
	//	Convert map
	//
		return (Map<String, Serializable>) _proxySerializer.unserialize(serialized);
	}
}
