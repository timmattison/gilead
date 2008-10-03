package net.sf.gilead.core.hibernate;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.beanlib.hibernate.UnEnhancer;
import net.sf.gilead.core.IPersistenceUtil;
import net.sf.gilead.exception.NotPersistentObjectException;
import net.sf.gilead.exception.TransientObjectException;
import net.sf.gilead.pojo.base.IUserType;

import org.hibernate.EntityMode;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.collection.PersistentBag;
import org.hibernate.collection.PersistentCollection;
import org.hibernate.collection.PersistentList;
import org.hibernate.collection.PersistentMap;
import org.hibernate.collection.PersistentSet;
import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.impl.SessionFactoryImpl;
import org.hibernate.impl.SessionImpl;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.persister.collection.CollectionPersister;
import org.hibernate.type.CollectionType;
import org.hibernate.type.Type;

/**
 * Persistent helper for Hibernate JPA implementation
 * Centralizes the SessionFactory and add some needed methods.
 * Not really a singleton, since there can be as many HibernateUtil instance as different sessionFactories
 * @author BMARCHESSON
 */
public class HibernateJpaUtil implements IPersistenceUtil
{
	//----
	// Serialized proxy informations map constants
	//----
	/**
	 * Proxy id
	 */
	private static final String ID="id";
	
	/**
	 * Persistent collection class name
	 */
	private static final String CLASS_NAME="class";
	
	/**
	 * Persistent collection role
	 */
	private static final String ROLE="role";
	
	/**
	 * Persistent collection PK ids
	 */
	private static final String KEY="key";
	
	//----
	// Attributes
	//----
	/**
	 * The pseudo unique instance of the singleton
	 */
	private static HibernateJpaUtil _instance = null;
	
	/**
	 * The Hibernate session factory
	 */
	private SessionFactory _sessionFactory;	
	
	/**
	 * The persistance map, with persistance status of all classes
	 * including persistent component classes
	 */
	private Map<Class<?>, Boolean> _persistenceMap;
	
	/**
	 * The current opened session
	 */
	private ThreadLocal<Session> _session;
	
	//----
	// Properties
	//----
	/**
	 * @return the unique instance of the singleton
	 */
	public static HibernateJpaUtil getInstance()
	{
		if (_instance == null)
		{
			_instance = new HibernateJpaUtil();
		}
		return _instance;
	}
	
	/**
	 * @return the hibernate session Factory
	 */
	public SessionFactory getSessionFactory()
	{
		return _sessionFactory;
	}

	/**
	 * Sets the JPA entity manager factory
	 * @param entityManagerFactory
	 */
	public void setEntityManagerFactory(Object entityManagerFactory)
	{
		_sessionFactory = ((org.hibernate.ejb.HibernateEntityManagerFactory) entityManagerFactory).getSessionFactory(); 
	}
	
	
	//-------------------------------------------------------------------------
	//
	// Constructor
	//
	//-------------------------------------------------------------------------
	/**
	 * Default constructor
	 */
	public HibernateJpaUtil()
	{
		_session = new ThreadLocal<Session>();
		_persistenceMap = new HashMap<Class<?>, Boolean>();
		
		// Filling persistence map with primitive types
		_persistenceMap.put(Byte.class, false);
		_persistenceMap.put(Short.class, false);
		_persistenceMap.put(Integer.class, false);
		_persistenceMap.put(Long.class, false);
		_persistenceMap.put(Float.class, false);
		_persistenceMap.put(Double.class, false);
		_persistenceMap.put(Boolean.class, false);
		_persistenceMap.put(String.class, false);
	}
	

	//-------------------------------------------------------------------------
	//
	// Public interface
	//
	//-------------------------------------------------------------------------
	/* (non-Javadoc)
	 * @see net.sf.gilead.core.hibernate.IPersistenceUtil#getId(java.lang.Object)
	 */
	public Serializable getId(Object pojo)
	{
		return getId(pojo, pojo.getClass());
	}
	
	/* (non-Javadoc)
	 * @see net.sf.gilead.core.hibernate.IPersistenceUtil#getId(java.lang.Object, java.lang.Class)
	 */
	public Serializable getId(Object pojo, Class<?> hibernateClass)
	{
	//	Precondition checking
	//
		if (_sessionFactory == null)
		{
			throw new NullPointerException("No Hibernate Session Factory defined !");
		}
		
	//	Unenhance Class<?> if needed
	//
		hibernateClass = UnEnhancer.unenhanceClass(hibernateClass);
		
	//	Persistence checking
	//
		if (isPersistentClass(hibernateClass) == false)
		{
		//	Not an hibernate Class !
		//
			throw new NotPersistentObjectException(hibernateClass);			
		}
		
	//	Retrieve Class<?> hibernate metadata
	//
		ClassMetadata hibernateMetadata = _sessionFactory.getClassMetadata(hibernateClass);
		if (hibernateMetadata == null)
		{
		//	Component class (persistent but not metadata) : no associated id
		//	So must be considered as transient
		//
			throw new TransientObjectException(pojo);
		}
		
	//	Retrieve ID
	//
		Serializable id = null;
		
		if (hibernateClass.equals(pojo.getClass()))
		{
		//	the pojo has the same class, simple use metadata
		//
			id = hibernateMetadata.getIdentifier(pojo, EntityMode.POJO);	
		}
		else
		{
		//	DTO case : invoke the method with the same name
		//
			String property = hibernateMetadata.getIdentifierPropertyName();
			
			try
			{
				// compute getter method name
				property = property.substring(0,1).toUpperCase() + 
						   property.substring(1);
				String getter = "get" + property;
				
				// Find getter method
				Class<?> pojoClass = pojo.getClass();
				Method method = pojoClass.getMethod(getter, (Class[])null);
				if (method == null)
				{
					throw new RuntimeException("Cannot find method " + getter + " for Class<?> " + pojoClass);
				}
				id = (Serializable) method.invoke(pojo,(Object[]) null);
			}
			catch (Exception ex)
			{
				throw new RuntimeException("Invocation exception ", ex);
			}
		}
		
	//	Post condition checking
	//
		if ((id == null) ||
			(id.toString().equals("0") == true))
		{
			throw new TransientObjectException(pojo);
		}
		return id;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.gilead.core.hibernate.IPersistenceUtil#isHibernatePojo(java.lang.Object)
	 */
	public boolean isPersistentPojo(Object pojo)
	{
	//	Precondition checking
	//
		if (pojo == null)
		{
			return false;
		}
		
	//	Try to get the ID : if an exception is thrown
	//	the pojo is not persistent...
	//
		try
		{
			getId(pojo);
			return true;
		}
		catch(TransientObjectException ex)
		{
			return false;
		}
		catch(NotPersistentObjectException ex)
		{
			return false;
		}
	}
	
	/* (non-Javadoc)
	 * @see net.sf.gilead.core.hibernate.IPersistenceUtil#isHibernateClass(java.lang.Class)
	 */
	public boolean isPersistentClass(Class<?> clazz)
	{
	//	Precondition checking
	//
		if (_sessionFactory == null)
		{
			throw new NullPointerException("No Hibernate Session Factory defined !");
		}
		
	//	Check proxy (based on beanlib Unenhancer class)
	//
		clazz = getUnenhancedClass(clazz);
		
	//	Look into the persistence map
	//
		synchronized (_persistenceMap)
		{
			Boolean persistent = _persistenceMap.get(clazz);
			if (persistent != null)
			{
				return persistent.booleanValue();
			}
		}
		
	//	First clall for this Class<?> : compute persistence class
	//
		computePersistenceForClass(clazz);
		return _persistenceMap.get(clazz).booleanValue();
	}
	
	/* (non-Javadoc)
	 * @see net.sf.gilead.core.hibernate.IPersistenceUtil#getPersistentClass(java.lang.Class)
	 */
	public Class<?> getUnenhancedClass(Class<?> clazz)
	{
	//	Precondition checking
	//
		if (_sessionFactory == null)
		{
			throw new NullPointerException("No Hibernate Session Factory defined !");
		}
		
	//	Check proxy (based on beanlib Enhancer class)
	//
		if (isEnhanced(clazz))
		{
			clazz = clazz.getSuperclass();
		}
		
		return clazz;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.gilead.core.hibernate.IPersistenceUtil#isEnhanced(java.lang.Class)
	 */
	public boolean isEnhanced(Class<?> clazz)
	{
	//	Check proxy (based on beanlib unEnhancer class)
	//
		return (clazz != UnEnhancer.unenhanceClass(clazz));
	}
	
	/* (non-Javadoc)
	 * @see net.sf.gilead.core.hibernate.IPersistenceUtil#openSession()
	 */
	public void openSession()
	{
	//	Precondition checking
	//
		if (_sessionFactory == null)
		{
			throw new NullPointerException("No Hibernate Session Factory defined !");
		}
		
	//	Open a new session
	//
		Session session = _sessionFactory.openSession();
		
	//	Store the session in ThreadLocal
	//
		_session.set(session);
	}
	
	/* (non-Javadoc)
	 * @see net.sf.gilead.core.hibernate.IPersistenceUtil#closeSession(java.lang.Object)
	 */
	public void closeCurrentSession()
	{
		Session session = _session.get();
		if (session != null)
		{
			session.close();
			_session.remove();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.gilead.core.IPersistenceUtil#load(java.io.Serializable, java.lang.Class)
	 */
	public Object load(Serializable id, Class<?> persistentClass)
	{
	//	Get current opened session
	//
		Session session = _session.get();
		if (session == null)
		{
			throw new NullPointerException("Cannot load : no session opened !");
		}
		
	//	Unenhance persistent class if needed
	//
		persistentClass = getUnenhancedClass(persistentClass);
		
	//	Load the entity
	//
		return session.get(persistentClass, id);
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.gilead.core.IPersistenceUtil#serializeEntityProxy(java.lang.Object)
	 */
	public Map<String, Serializable> serializeEntityProxy(Object proxy)
	{
	//	Precondition checking
	//
		if (proxy == null)
		{
			return null;
		}
		
	//	Serialize needed proxy informations
	//
		Map<String, Serializable> result = new HashMap<String, Serializable>();
		result.put(CLASS_NAME, getUnenhancedClass(proxy.getClass()).getName());
		result.put(ID, getId(proxy));
		
		return result;
	}
	
	/**
	 * Create a proxy for the argument class and id
	 */
	public Object createEntityProxy(Map<String, Serializable> proxyInformations)
	{
	//	Get current opened session
	//
		Session session = _session.get();
		if (session == null)
		{
			throw new NullPointerException("Cannot load : no session opened !");
		}
	
	//	Get needed proxy inforamtions
	//
		Serializable id = proxyInformations.get(ID);
		String className = (String) proxyInformations.get(CLASS_NAME);
		Class<?> persistentClass = null;
		try
		{
			persistentClass = Thread.currentThread().getContextClassLoader().loadClass(className);
		}
		catch(Exception e)
		{
		// Should not happen !
		//
			throw new RuntimeException("Cannot find persistent class : " + className, e);
		}
		
	//	Create the associated proxy
	//
		return session.load(persistentClass, id);
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.gilead.core.IPersistenceUtil#serializePersistentCollection(java.lang.Object)
	 */
	public Map<String, Serializable> serializePersistentCollection(Object persistentCollection)
	{
	//	Create serialization map
	//
		Map<String, Serializable> result = new HashMap<String, Serializable>();
		
	//	Get parameters
	//
		PersistentCollection collection = (PersistentCollection) persistentCollection;
		result.put(CLASS_NAME, collection.getClass().getName());
		result.put(ROLE, collection.getRole());
		result.put(KEY, collection.getKey());
		
		return result;
	}
	
	/**
	 * Create a persistent collection
	 * @param proxyInformations serialized proxy informations 
	 * @param underlyingCollection the filled underlying collection
	 * @return
	 */
	public Object createPersistentCollection(Map<String, Serializable> proxyInformations,
											 Object underlyingCollection)
	{
	//	Get current opened session
	//
		Session session = _session.get();
		if (session == null)
		{
			throw new NullPointerException("Cannot load : no session opened !");
		}
		
	//	Create collection for the class name
	//
		String className = (String) proxyInformations.get(CLASS_NAME);

		PersistentCollection collection = null;
		if (PersistentBag.class.getName().equals(className))
		{
		//	Persistent bag creation
		//
			if (underlyingCollection == null)
			{
				collection = new PersistentBag((SessionImpl) session);
			}
			else
			{
				collection =  new PersistentBag((SessionImpl) session,
										 		(Collection<?>) underlyingCollection);
			}
		}
		else if (PersistentList.class.getName().equals(className))
		{
		//	Persistent list creation
		//
			if (underlyingCollection == null)
			{
				collection = new PersistentList((SessionImpl) session);
			}
			else
			{
				collection = new PersistentList((SessionImpl) session,
										  		(List<?>) underlyingCollection);
			}
		}
		else if (PersistentSet.class.getName().equals(className))
		{
		//	Persistent set creation
		//
			if (underlyingCollection == null)
			{
				collection = new PersistentSet((SessionImpl) session);
			}
			else
			{
				collection = new PersistentSet((SessionImpl) session,
						 				 	   (Set<?>) underlyingCollection);
			}
		}
		else if (PersistentMap.class.getName().equals(className))
		{
		//	Persistent map creation
		//
			if (underlyingCollection == null)
			{
				collection = new PersistentMap((SessionImpl) session);
			}
			else
			{
				collection = new PersistentMap((SessionImpl) session,
						 				 	   (Map<?, ?>) underlyingCollection);
			}
		}
		else
		{
			throw new RuntimeException("Unknown persistent collection class name : " + className);
		}
		
	//	Fill with serialized parameters
	//
		String role = (String) proxyInformations.get(ROLE);
		Serializable snapshot = null;
		if (underlyingCollection != null)
		{
		//	Create snpashot
		//
			CollectionPersister collectionPersister = ((SessionFactoryImpl)_sessionFactory).getCollectionPersister(role);
			snapshot = collection.getSnapshot(collectionPersister);
		}
		
		collection.setSnapshot(proxyInformations.get(KEY), 
							   role, snapshot);
		
		return collection;
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.gilead.core.IPersistenceUtil#isPersistentCollection(java.lang.Class)
	 */
	public boolean isPersistentCollection(Class<?> collectionClass)
	{
		return (PersistentCollection.class.isAssignableFrom(collectionClass));
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.gilead.core.IPersistenceUtil#isProxy(java.lang.Object)
	 */
	public boolean isInitialized(Object proxy)
	{
		return Hibernate.isInitialized(proxy);
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.gilead.core.IPersistenceUtil#initialize(java.lang.Object)
	 */
	public void initialize(Object proxy)
	{
		Hibernate.initialize(proxy);
	}

	//-------------------------------------------------------------------------
	//
	// Internal methods
	//
	//-------------------------------------------------------------------------
	/**
	 * Compute embedded persistence (Component, UserType) for argument class
	 */
	private void computePersistenceForClass(Class<?> clazz)
	{
		ClassMetadata metadata = _sessionFactory.getClassMetadata(clazz);
		if (metadata == null)
		{
		//	Not persistent !
		//
			synchronized (_persistenceMap) {
				_persistenceMap.put(clazz, false);
			}
			return;
		}

	//	Persistent class
	//
		synchronized (_persistenceMap) {
			_persistenceMap.put(clazz, true);
		}
		
	//	Look for component classes
	//
		Type[] types = metadata.getPropertyTypes();
		for (int index = 0; index < types.length; index++)
		{
			Type type = types[index];
			if ((type.isComponentType()) ||
				(IUserType.class.isAssignableFrom(type.getReturnedClass())))
			{
			//	Add the Class to the persistent map
			//
				synchronized (_persistenceMap)
				{
					_persistenceMap.put(type.getReturnedClass(), true);
				}
			}
			else if(type.isCollectionType()) 
			{
			//	Check collection element type
			//
				Type elementType = ((CollectionType)type).getElementType((SessionFactoryImplementor)_sessionFactory); 
				if(elementType.isComponentType()) 
				{
					synchronized (_persistenceMap)
					{
						_persistenceMap.put(elementType.getReturnedClass(), true);
					}
				} 
			} 
		}
		
	}
}