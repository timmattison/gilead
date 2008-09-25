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

package net.sf.gilead.gwt;

import java.lang.reflect.InvocationTargetException;

import javax.servlet.ServletException;

import net.sf.gilead.core.HibernateBeanManager;
import net.sf.gilead.core.beanlib.mapper.ProxyClassMapper;

import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RPCCopy;
import com.google.gwt.user.server.rpc.RPCRequest;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Abstract class for GWT remote service using Hibernate POJO 
 * @author bruno.marchesson
 *
 */
public abstract class HibernateRemoteService extends RemoteServiceServlet
{
	//----
	// Attribute
	//----
	/**
	 * The Hibernate lazy manager
	 */
	protected HibernateBeanManager _beanManager;
	
	//----
	// Properties
	//----
	/**
	 * @return the Hibernate Bean Manager
	 */
	public HibernateBeanManager getBeanManager()
	{
		return _beanManager;
	}

	/**
	 * @param manager the Hibernate Bean Manager to set
	 */
	public void setBeanManager(HibernateBeanManager manager)
	{
		_beanManager = manager;
	}
	
	//-------------------------------------------------------------------------
	//
	// Constructor
	//
	//-------------------------------------------------------------------------
	/**
	 * Empty constructor
	 */
	public HibernateRemoteService()
	{
	//	Default Hibernate Lazy Manager
	//
		_beanManager = HibernateBeanManager.getInstance();
	}
	
	/**
	 * Base constructor
	 */
	public HibernateRemoteService(HibernateBeanManager lazyManager)
	{
		_beanManager = lazyManager;
	}
	
	//-------------------------------------------------------------------------
	//
	// Hibernate Java 1.4 POJO methods
	//
	//-------------------------------------------------------------------------
	/**
	 * Clone and store (if needed) the hibernate POJO
	 */
	public Object clone(Object hibernatePojo)
	{
		return _beanManager.clone(hibernatePojo);
	}
	
	/**
	 * Retrieve and populate Hibernate pojo with gwt pojo values 
	 */
	public Object merge(Object gwtPojo)
	{
		return _beanManager.merge(gwtPojo);
	}
	
	//-------------------------------------------------------------------------
	//
	// Remote service servlet override
	//
	//-------------------------------------------------------------------------
	/**
	 * Servlet initialisation
	 */
	public void init() throws ServletException
	{
		super.init();
		
	//	Init proxy class loader if in proxy mode 
	//
		if ((_beanManager != null) &&
			(_beanManager.getClassMapper() instanceof ProxyClassMapper))
		{
			HibernateRPCHelper.initClassLoader();
		}
	}
	
	/**
	 * Override of the RemoteServletService main method
	 */
	public String processCall(String payload) throws SerializationException
	{		
	//	Normal processing
	//
		RPCRequest rpcRequest = null;
		try
		{
		// Decode request
		//
	       rpcRequest = RPCCopy.getInstance().decodeRequest(payload, this.getClass(), this);
	      
	    //	Invoke method
	    //
	       HibernateRPCHelper.parseInputParameters(rpcRequest, _beanManager, 
	    		   								   getThreadLocalRequest().getSession());
	       Object returnValue = RPCCopy.getInstance().invoke(this, rpcRequest.getMethod(), 
	    		  							   rpcRequest.getParameters(),
	    		  							   rpcRequest.getSerializationPolicy());
	      
	       returnValue = HibernateRPCHelper.parseReturnValue(returnValue, _beanManager);
	      
	    //	Encode response
	    //  
	      return RPCCopy.getInstance().encodeResponseForSuccess(rpcRequest.getMethod(), 
	    		  								  				returnValue,
	    		  								  				rpcRequest.getSerializationPolicy());
	      
	    } 
		catch (InvocationTargetException e)
		{
			return RPCCopy.getInstance().encodeResponseForFailure(rpcRequest.getMethod(), e.getCause());
		}
		catch (IncompatibleRemoteServiceException ex)
		{
	        return RPCCopy.getInstance().encodeResponseForFailure(null, ex);
	    } 
	}
}
