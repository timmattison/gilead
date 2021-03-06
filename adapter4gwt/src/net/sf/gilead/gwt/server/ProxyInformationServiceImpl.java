/**
 * 
 */
package net.sf.gilead.gwt.server;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import net.sf.gilead.core.PersistentBeanManager;
import net.sf.gilead.core.store.IProxyStore;
import net.sf.gilead.core.store.stateful.AbstractStatefulProxyStore;
import net.sf.gilead.gwt.client.ProxyInformationService;
import net.sf.gilead.pojo.base.ILightEntity;
import net.sf.gilead.pojo.gwt.IGwtSerializableParameter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Request service implementation.
 * @author bruno.marchesson
 *
 */
public class ProxyInformationServiceImpl extends RemoteServiceServlet
										 implements ProxyInformationService
{
	//----
	// Attributes
	//----
	/**
	 * Serialization ID
	 */
	private static final long serialVersionUID = 814725549964949202L;

	/**
	 * Logger channel
	 */
	private static Logger _log = LoggerFactory.getLogger(ProxyInformationServiceImpl.class);
	
	/**
	 * The associated bean manager.
	 * Default value is defined by the unique instance of the singleton.
	 */
	private PersistentBeanManager beanManager = PersistentBeanManager.getInstance();
	
	//----
	// Properties
	//---
	/**
	 * @return the beanManager
	 */
	public PersistentBeanManager getBeanManager()
	{
		return beanManager;
	}

	/**
	 * @param beanManager the beanManager to set
	 */
	public void setBeanManager(PersistentBeanManager beanManager)
	{
		this.beanManager = beanManager;
	}

	//-------------------------------------------------------------------------
	//
	// Request service implementation
	//
	//-------------------------------------------------------------------------
	/*
	 * (non-Javadoc)
	 * @see net.sf.gilead.gwt.client.ProxyInformationService#isLazyLoaded(java.lang.String, java.lang.String)
	 */
	public boolean isInitialized(String className, IGwtSerializableParameter idParam, String propertyName)
	{
	//	Precondition checking
	//
		if ((propertyName == null) ||
			(propertyName.length() == 0))
		{
			throw new NullPointerException("Null or empty property name!");
		}
		
		if (beanManager == null)
		{
			throw new NullPointerException("Bean manager not set !");
		}
		
		Serializable id = (Serializable) idParam.getUnderlyingValue();
		if (_log.isDebugEnabled())
		{
			_log.debug("Getting initialized state for " + className + "[" + id+ "]." + propertyName);
		}
		
	//	Get Proxy store and serialization
	//
		IProxyStore proxyStore = beanManager.getProxyStore();
		if (proxyStore == null)
		{
			throw new NullPointerException("Proxy store not set on beanManager field !");
		}
		if (proxyStore instanceof AbstractStatefulProxyStore == false)
		{
			throw new RuntimeException("This service can only be called with Stateful proxy store !");
		}
		
	//	Compute key and get proxy information
	//
		AbstractStatefulProxyStore statefulProxyStore = (AbstractStatefulProxyStore) proxyStore;
		Class<?> pojoClass = null;
		try
		{
			pojoClass = Thread.currentThread().getContextClassLoader().loadClass(className);
		}
		catch(Exception ex)
		{
			throw new RuntimeException("Unknown class " + className, ex);
		}
		String key = statefulProxyStore.computeKey(pojoClass, id, propertyName);
		
		Map<String, Serializable> proxyInformations = statefulProxyStore.get(key);
		
	//	Analyze proxyInformations
	//
		if (proxyInformations == null)
		{
			return true;
		}
		Boolean initialized = (Boolean) proxyInformations.get(ILightEntity.INITIALISED);
		if (initialized == null)
		{
			return true;
		}
		else
		{
			return initialized.booleanValue();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.gilead.gwt.client.ProxyInformationService#setInitialized(java.lang.String, net.sf.gilead.gwt.client.parameters.IRequestParameter, java.lang.String)
	 */
	public void setInitialized(String className, IGwtSerializableParameter idParam,
							   String propertyName)
	{
	//	Precondition checking
	//
		if ((propertyName == null) ||
			(propertyName.length() == 0))
		{
			throw new NullPointerException("Null or empty property name!");
		}
		
		if (beanManager == null)
		{
			throw new NullPointerException("Bean manager not set !");
		}
		
		Serializable id = (Serializable) idParam.getUnderlyingValue();
		if (_log.isDebugEnabled())
		{
			_log.debug("Changing initialized state to 'true' for " + className + "[" + id+ "]." + propertyName);
		}

		
	//	Get Proxy store and serialization
	//
		IProxyStore proxyStore = beanManager.getProxyStore();
		if (proxyStore == null)
		{
			throw new NullPointerException("Proxy store not set on beanManager field !");
		}
		if (proxyStore instanceof AbstractStatefulProxyStore == false)
		{
			throw new RuntimeException("This service can only be called with Stateful proxy store !");
		}
		
	//	Compute key and get proxy information
	//
		AbstractStatefulProxyStore statefulProxyStore = (AbstractStatefulProxyStore) proxyStore;
		Class<?> pojoClass = null;
		try
		{
			pojoClass = Thread.currentThread().getContextClassLoader().loadClass(className);
		}
		catch(Exception ex)
		{
			throw new RuntimeException("Unknown class " + className, ex);
		}
		String key = statefulProxyStore.computeKey(pojoClass, id, propertyName);
		
		Map<String, Serializable> proxyInformations = statefulProxyStore.get(key);
		
	//	Update proxyInformations
	//
		if (proxyInformations == null)
		{
			proxyInformations = new HashMap<String, Serializable>();
		}
		proxyInformations.put(ILightEntity.INITIALISED, true);
		
	//	Store proxy infos
	//
		statefulProxyStore.store(key, proxyInformations);
	}
}
