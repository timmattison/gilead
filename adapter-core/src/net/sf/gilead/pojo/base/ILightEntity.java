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

package net.sf.gilead.pojo.base;

import java.util.Map;

/**
 * Persistence info handler interface.
 * All the persistent POJO must implements this interface in stateless mode.
 * @author bruno.marchesson
 */
public interface ILightEntity
{
	public static final String INITIALISED = "initialized";

	//-------------------------------------------------------------------------
	//
	// Public interface
	//
	//-------------------------------------------------------------------------
	/**
	 * Indicates if the argument property is initialized or not
	 */
	public boolean isInitialized(String property);
	
	/**
	 * Indicates if the argument property is initialized or not
	 */
	public void setInitialized(String property, boolean initialized);
	
	/**
	 * @return the initialization map
	 */
	public Map getInitializationMap();

	/**
	 * @param initializationMap the initialization map to set
	 */
	public void setInitializationMap(Map initializationMap);
	
	/**
	 * Add proxy information for the argument property.
	 * @param property the property name
	 * @param proxyInformation map of proxy informations (string serialized form)
	 */
	public abstract void addProxyInformation(String property, 
										 	 String proxyInfo);
	
	/**
	 * Remove a property proxy information
	 */
	public abstract void removeProxyInformation(String property);

	/**
	 * Get proxy information for the argument property
	 * @param property the property name
	 * @return the proxy informations for the property _ string serialized form(can be null)
	 */
	public String getProxyInformation(String property);

	/**
	 * Debug method : write the declared proxy information
	 * @return a human readable description of proxy information
	 */
	public abstract String getDebugString();

	/**
	 * @return the proxy informations
	 */
	public Map getProxyInformations();

	/**
	 * @param proxyInformations the proxy informations to set
	 */
	public void setProxyInformations(Map proxyInformations);
}