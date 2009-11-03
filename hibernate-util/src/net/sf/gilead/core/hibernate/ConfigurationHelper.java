/**
 * 
 */
package net.sf.gilead.core.hibernate;

import java.io.FileNotFoundException;

import net.sf.gilead.core.PersistentBeanManager;
import net.sf.gilead.core.beanlib.mapper.ProxyClassMapper;
import net.sf.gilead.core.serialization.JBossProxySerialization;
import net.sf.gilead.core.store.stateful.HttpSessionProxyStore;
import net.sf.gilead.core.store.stateless.StatelessProxyStore;

import org.hibernate.SessionFactory;

/**
 * Configuration helper to simplify Gilead configuration.
 * @author bruno.marchesson
 *
 */
public class ConfigurationHelper 
{
	//-------------------------------------------------------------------------
	//
	// Static helpers
	//
	//-------------------------------------------------------------------------
	/**
	 * Init bean manager for stateless mode
	 */
	public static PersistentBeanManager initStatelessBeanManager(SessionFactory sessionFactory)
	{
		HibernateUtil persistenceUtil = new HibernateUtil(); 
		persistenceUtil.setSessionFactory(sessionFactory);
		
		PersistentBeanManager beanManager = PersistentBeanManager.getInstance();
		beanManager.setPersistenceUtil(persistenceUtil);
		beanManager.setProxyStore(new StatelessProxyStore());
		beanManager.setClassMapper(null);
		
		return beanManager;
	}
	
	/**
	 * Init bean manager for stateless mode for GWT
	 */
//	public static PersistentBeanManager initGwtStatelessBeanManager(SessionFactory sessionFactory))
//	{
//		HibernateUtil persistenceUtil = new HibernateUtil(); 
//		persistenceUtil.setSessionFactory(sessionFactory);
//		
//		PersistentBeanManager beanManager = PersistentBeanManager.getInstance();
//		beanManager.setPersistenceUtil(persistenceUtil);
//		
//		StatelessProxyStore proxyStore = new StatelessProxyStore();
//		proxyStore.setProxySerializer(new GwtProxySerialization());
//		beanManager.setProxyStore(proxyStore);
//		
//		beanManager.setClassMapper(null);
//		
//		return beanManager;
//	}
	
	/**
	 * Init bean manager for stateless mode for legacy Gilead 1.2 (encoded proxy info)
	 */
	public static PersistentBeanManager initLegacyStatelessBeanManager(SessionFactory sessionFactory)
	{
		HibernateUtil persistenceUtil = new HibernateUtil(); 
		persistenceUtil.setSessionFactory(sessionFactory);
		
		PersistentBeanManager beanManager = PersistentBeanManager.getInstance(); //new PersistentBeanManager();
		beanManager.setPersistenceUtil(persistenceUtil);
		
		StatelessProxyStore proxyStore = new StatelessProxyStore();
		proxyStore.setProxySerializer(new JBossProxySerialization());
		beanManager.setProxyStore(proxyStore);
		
		beanManager.setClassMapper(null);
		
		return beanManager;
	}
	
	/**
	 * Init bean manager for stateful mode
	 */
	public static PersistentBeanManager initStatefulBeanManager(SessionFactory sessionFactory)
	{
		HibernateUtil persistenceUtil = new HibernateUtil(); 
		persistenceUtil.setSessionFactory(sessionFactory);
		
		HttpSessionProxyStore proxyStore = new HttpSessionProxyStore();
		proxyStore.setPersistenceUtil(persistenceUtil);
		
		PersistentBeanManager beanManager = PersistentBeanManager.getInstance(); //new PersistentBeanManager();
		beanManager.setPersistenceUtil(persistenceUtil);
		beanManager.setProxyStore(proxyStore);
		beanManager.setClassMapper(null);
		
		return beanManager;
	}
	
	/**
	 * Init bean manager for dynamic proxy mode
	 * @throws FileNotFoundException 
	 */
	public static PersistentBeanManager initProxyBeanManager(SessionFactory sessionFactory)
	{
		HibernateUtil persistenceUtil = new HibernateUtil(); 
		persistenceUtil.setSessionFactory(sessionFactory);
		
		PersistentBeanManager beanManager = PersistentBeanManager.getInstance(); //new PersistentBeanManager();
		beanManager.setPersistenceUtil(persistenceUtil);
		
		StatelessProxyStore proxyStore = new StatelessProxyStore();
		proxyStore.setProxySerializer(new JBossProxySerialization());
		beanManager.setProxyStore(proxyStore);
		
		ProxyClassMapper classMapper = new ProxyClassMapper();
		classMapper.setPersistenceUtil(persistenceUtil);
		beanManager.setClassMapper(classMapper);
		
		return beanManager;
	}
	
}
