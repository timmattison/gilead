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
package net.sf.gilead.core.beanlib.mapper;

import net.sf.gilead.core.beanlib.IClassMapper;
import net.sf.gilead.proxy.ProxyManager;

/**
 * Class mapper for domain and proxy
 * @author bruno.marchesson
 *
 */
public class ProxyClassMapper implements IClassMapper 
{
	//-------------------------------------------------------------------------
	//
	// IClassMapper implementation
	//
	//-------------------------------------------------------------------------
	/* (non-Javadoc)
	 * @see net.sf.gilead.core.beanlib.IClassMapper#getSourceClass(java.lang.Class)
	 */
	public Class<?> getSourceClass(Class<?> targetClass)
	{
		return ProxyManager.getInstance().getSourceClass(targetClass);
	}

	/* (non-Javadoc)
	 * @see net.sf.gilead.core.beanlib.IClassMapper#getTargetClass(java.lang.Class<?>)
	 */
	public Class<?> getTargetClass(Class<?> sourceClass)
	{
		return ProxyManager.getInstance().getProxyClass(sourceClass);
	}

}