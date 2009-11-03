/**
 * 
 */
package net.sf.gilead.gwt;

import org.hibernate.SessionFactory;

import net.sf.gilead.core.PersistentBeanManager;
import net.sf.gilead.core.hibernate.HibernateUtil;
import net.sf.gilead.core.serialization.GwtProxySerialization;
import net.sf.gilead.core.store.stateless.StatelessProxyStore;

/**
 * @author bruno.marchesson
 *
 */
public class GwtConfigurationHelper
{
	/**
	 * Init bean manager for stateless mode for GWT
	 */
	public static PersistentBeanManager initGwtStatelessBeanManager(SessionFactory sessionFactory)
	{
		HibernateUtil persistenceUtil = new HibernateUtil(); 
		persistenceUtil.setSessionFactory(sessionFactory);
		
		PersistentBeanManager beanManager = PersistentBeanManager.getInstance();
		beanManager.setPersistenceUtil(persistenceUtil);
		
		StatelessProxyStore proxyStore = new StatelessProxyStore();
		proxyStore.setProxySerializer(new GwtProxySerialization());
		beanManager.setProxyStore(proxyStore);
		
		beanManager.setClassMapper(null);
		
		return beanManager;
	}
}
