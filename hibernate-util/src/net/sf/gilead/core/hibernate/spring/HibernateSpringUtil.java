/**
 * 
 */
package net.sf.gilead.core.hibernate.spring;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import net.sf.gilead.core.hibernate.HibernateUtil;
import net.sf.gilead.util.IntrospectionHelper;

import org.hibernate.SessionFactory;
import org.hibernate.ejb.HibernateEntityManagerFactory;
import org.hibernate.impl.SessionFactoryImpl;
import org.springframework.aop.framework.AopProxy;
import org.springframework.aop.support.AopUtils;

/**
 * Spring handling of injected session factories
 * @author bruno.marchesson
 *	
 */
public class HibernateSpringUtil extends HibernateUtil
{
	/**
	 * Set entity manager factory from JBoss
	 * @param entityManagerFactory
	 */
	public void setEntityManagerFactory(Object entityManagerFactory)
	{
	//	Manage Injected EntityManagerFactory
	//
		if (entityManagerFactory instanceof AopProxy)
		{
		//	Need to call 'getProxy' method
		//
			entityManagerFactory = ((AopProxy) entityManagerFactory).getProxy();
		}
		else if (AopUtils.isJdkDynamicProxy(entityManagerFactory))
		{
		//	JDK dynamic proxy
		//
			InvocationHandler invocationHandler = Proxy.getInvocationHandler(entityManagerFactory); 
			entityManagerFactory = (SessionFactory) IntrospectionHelper.searchMember(HibernateEntityManagerFactory.class, 
																					invocationHandler); 
		} 
		

	//	Just get session factory for Hibernate emf implementation
	//
		if (entityManagerFactory instanceof HibernateEntityManagerFactory)
		{
		//	Base class
		//
			setSessionFactory(((HibernateEntityManagerFactory) entityManagerFactory).getSessionFactory());
		}
		else
		{
			throw new IllegalArgumentException("Cannot find Hibernate entity manager factory implementation for " + entityManagerFactory);
		}	 
	}
	
	@Override
	public void setSessionFactory(SessionFactory sessionFactory)
	{
	//	Manage injected SessionFactory
	//
		if (sessionFactory instanceof AopProxy)
		{
		//	Need to call 'getProxy' method
		//
			sessionFactory = (SessionFactory) ((AopProxy) sessionFactory).getProxy();
		}
		else if (AopUtils.isJdkDynamicProxy(sessionFactory))
		{
		//	JDK dynamic proxy
		//
			InvocationHandler invocationHandler = Proxy.getInvocationHandler(sessionFactory); 
			sessionFactory = (SessionFactory) IntrospectionHelper.searchMember(SessionFactoryImpl.class, invocationHandler); 
		} 
		
		// Call base class
		super.setSessionFactory(sessionFactory);
	}
}
