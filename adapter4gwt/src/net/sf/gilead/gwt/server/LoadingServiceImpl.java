package net.sf.gilead.gwt.server;

import java.io.Serializable;

import net.sf.gilead.core.IPersistenceUtil;
import net.sf.gilead.core.PersistentBeanManager;
import net.sf.gilead.gwt.PersistentRemoteService;
import net.sf.gilead.gwt.client.LoadingService;
import net.sf.gilead.pojo.base.ILightEntity;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * GWT remote loading service implementation
 * @author bruno.marchesson
 *
 */
public class LoadingServiceImpl<T extends ILightEntity> extends PersistentRemoteService
								implements LoadingService<T>
{
	//----
	// Attributes
	//----
	/**
	 * Serialization ID
	 */
	private static final long serialVersionUID = 5714428833885668669L;
	
	/**
	 * Log channel
	 */
	private static Log _log = LogFactory.getLog(LoadingServiceImpl.class);
	
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
	// Service implementation
	//
	//-------------------------------------------------------------------------
	/**
	 * Load an association from the parent entity
	 * @param parent the entity
	 * @param property the name of the property to load
	 * @return the loaded entity
	 */
	public <K extends ILightEntity> K loadEntityAssociation(T parent, String propertyName)
	{
	//	Precondition checking
	//
		if (parent == null)
		{
			throw new NullPointerException("Null entity !");
		}
		if ((propertyName == null) ||
			(propertyName.length() == 0))
		{
			throw new NullPointerException("Null or empty property name!");
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
		
		if (_log.isDebugEnabled())
		{
			_log.debug("Loading property " + propertyName + " for entity " + parent);
		}
		
	//	Get Id
	//
		Serializable id = persistenceUtil.getId(parent);
		
	//	Load assocation
	//
		return (K) persistenceUtil.loadAssociation(parent.getClass(), id, propertyName);
	}
}
