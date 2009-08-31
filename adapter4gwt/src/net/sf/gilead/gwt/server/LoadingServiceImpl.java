package net.sf.gilead.gwt.server;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Set;

import net.sf.gilead.core.IPersistenceUtil;
import net.sf.gilead.core.PersistentBeanManager;
import net.sf.gilead.gwt.PersistentRemoteService;
import net.sf.gilead.gwt.client.LoadingService;
import net.sf.gilead.pojo.base.ILightEntity;
import net.sf.gilead.util.IntrospectionHelper;

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
	
	/**
	 * The associated persistent class
	 */
	private Class<T> persistentClass;
	
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
	
	//-----------------------------------------------------------------------
	//
	// Constructors
	//
	//-----------------------------------------------------------------------
	/**
	 * Constructor
	 */
	@SuppressWarnings("unchedked")
	public LoadingServiceImpl()
	{
		persistentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	//-------------------------------------------------------------------------
	//
	// Association loading implementation
	//
	//-------------------------------------------------------------------------
	/**
	 * Load an association from the parent entity
	 * @param parent the entity
	 * @param property the name of the property to load
	 * @return the loaded entity
	 */
	@SuppressWarnings("unchecked")
	public <K extends ILightEntity> K loadEntityAssociation(T parent, String propertyName)
	{
		return (K) loadAssociation(parent, propertyName);
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.gilead.gwt.client.LoadingService#loadAssociationList(net.sf.gilead.pojo.base.ILightEntity, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public <K extends ILightEntity> List<K> loadListAssociation(T parent,
																String propertyName)
	{
		return (List<K>) loadAssociation(parent, propertyName);
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.gilead.gwt.client.LoadingService#loadSetAssociation(net.sf.gilead.pojo.base.ILightEntity, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public <K extends ILightEntity> Set<K> loadSetAssociation(T parent,
															  String propertyName)
	{
	return (Set<K>) loadAssociation(parent, propertyName);
	}
	
	//-------------------------------------------------------------------------
	//
	// Entity loading methods
	//
	//-------------------------------------------------------------------------

	/*
	 * (non-Javadoc)
	 * @see net.sf.gilead.gwt.client.LoadingService#loadEntity(java.lang.Integer)
	 */
	public T loadEntity(Integer id)
	{
		return loadEntity((Serializable)id);
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.gilead.gwt.client.LoadingService#loadEntity(java.lang.Long)
	 */
	public T loadEntity(Long id)
	{
		return loadEntity((Serializable)id);
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.gilead.gwt.client.LoadingService#loadEntity(java.lang.String)
	 */
	public T loadEntity(String id)
	{
		return loadEntity((Serializable)id);
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.gilead.gwt.client.LoadingService#loadEntity(net.sf.gilead.pojo.base.ILightEntity)
	 */
	public <K extends ILightEntity> T loadEntity(K compositeId)
	{
		return loadEntity((Serializable)compositeId);
	}
	
	//-------------------------------------------------------------------------
	//
	// Internal methods
	//
	//-------------------------------------------------------------------------
	/**
	 * Loads an association 
	 */
	protected Object loadAssociation(T parent, String propertyName)
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
		
	//	Load entity and assocation
	//
		Object entity = persistenceUtil.loadAssociation(parent.getClass(), id, propertyName);
		
	//	Get getter for the property
	//
		Object association = null;
		try
		{
			Method reader = IntrospectionHelper.getReaderMethodForProperty(entity.getClass(), propertyName);
			association = reader.invoke(entity, (Object[]) null);
		}
		catch(Exception ex)
		{
			throw new RuntimeException("Error during lazy loading invocation !", ex);
		}
		
		return beanManager.clone(association);
	}
	
	/**
	 * Loads an entity
	 */
	@SuppressWarnings("unchecked")
	protected T loadEntity(Serializable id)
	{
	//	Precondition checking
	//
		if (id == null)
		{
			throw new NullPointerException("Missing id!");
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
			_log.debug("Loading entity " + persistentClass + " with ID" + id);
		}
	
	//	Load entity and clone it
	//
		Object entity = persistenceUtil.load(id, persistentClass);
		return (T) beanManager.clone(entity);
	
	}
}
