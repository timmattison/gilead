/**
 * 
 */
package net.sf.gilead.blazeds.remoting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.gilead.core.IPersistenceUtil;
import net.sf.gilead.core.PersistentBeanManager;
import net.sf.gilead.pojo.base.ILightEntity;
import net.sf.gilead.pojo.gwt.IRequestParameter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Request service implementation.
 * @author bruno.marchesson
 *
 */
public class RequestService<T extends ILightEntity> implements IBeanManagerService
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
	private static Logger _log = LoggerFactory.getLogger(RequestService.class);
	
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
	/**
	 * @see net.sf.gilead.gwt.client.RequestService#executeRequest(java.lang.String, java.util.List)
	 */
	@SuppressWarnings("unchecked")
	public List<T> executeRequest(String query,
								  List<IRequestParameter> parameters)
	{
	//	Precondition checking
	//
		if (query == null)
		{
			throw new RuntimeException("Missing query !");
		}
		
		if (_log.isDebugEnabled())
		{
			_log.debug("Executing request " + query);
		}
		
		if (beanManager == null)
		{
			throw new NullPointerException("Bean manager not set !");
		}
		
	//	Get Persistence util
	//
		IPersistenceUtil persistenceUtil = beanManager.getPersistenceUtil();
		if (persistenceUtil == null)
		{
			throw new NullPointerException("Persistence util not set on beanManager field !");
		}
		
	//	Convert parameters if needed
	//
		List<Object> queryParameters = null;
		if ((parameters != null) &&
			(parameters.isEmpty() == false))
		{
			queryParameters = new ArrayList<Object>(parameters.size());
			for (IRequestParameter parameter : parameters)
			{
				queryParameters.add(parameter.getValue());
			}
		}
		
	//	Execute query
	// 	Note : double case is mandatory due to Java 6 compiler issue 6548436
	//
		return (List<T>)(Object) persistenceUtil.executeQuery(query, queryParameters);
	}

	/**
	 * @see net.sf.gilead.gwt.client.RequestService#executeRequest(java.lang.String, java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	public List<T> executeRequest(String query,
								  Map<String, IRequestParameter> parameters) 
	{
	//	Precondition checking
	//
		if (query == null)
		{
			throw new RuntimeException("Missing query !");
		}
		
		if (_log.isDebugEnabled())
		{
			_log.debug("Executing request " + query);
		}
		
		if (beanManager == null)
		{
			throw new NullPointerException("Bean manager not set !");
		}
		
	//	Get Persistence util
	//
		IPersistenceUtil persistenceUtil = beanManager.getPersistenceUtil();
		if (persistenceUtil == null)
		{
			throw new NullPointerException("Persistence util not set on beanManager field !");
		}
		
	//	Convert parameters if needed
	//
		Map<String,Object> queryParameters = null;
		if ((parameters != null) &&
			(parameters.isEmpty() == false))
		{
			queryParameters = new HashMap<String, Object>(parameters.size());
			for (Map.Entry<String, IRequestParameter> parameter : parameters.entrySet())
			{
				queryParameters.put(parameter.getKey(), parameter.getValue().getValue());
			}
		}
		
	//	Execute query
	//	Note : double case is mandatory due to Java 6 compiler issue 6548436
	//
		return (List<T>)(Object) persistenceUtil.executeQuery(query, queryParameters);
	}
}
