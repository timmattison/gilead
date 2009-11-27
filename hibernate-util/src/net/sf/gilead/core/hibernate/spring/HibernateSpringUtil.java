/**
 * 
 */
package net.sf.gilead.core.hibernate.spring;

import javax.persistence.EntityManagerFactory;

import net.sf.gilead.core.hibernate.HibernateUtil;

import org.hibernate.SessionFactory;
import org.hibernate.ejb.HibernateEntityManagerFactory;
import org.springframework.aop.framework.AopProxy;

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
		
		// Call base class
		super.setSessionFactory(sessionFactory);
	}
	
	//-------------------------------------------------------------------------
	//
	// Constructors
	//
	//-------------------------------------------------------------------------
	/**
	 * Empty constructor.
	 */
	public HibernateSpringUtil()
	{
		super();
	}
	
	/**
	 * Session factory constructor.
	 */
	public HibernateSpringUtil(SessionFactory sessionFactory)
	{
		super();
		setSessionFactory(sessionFactory);
	}
	
	/**
	 * Entity Manager factory constructor.
	 */
	public HibernateSpringUtil(EntityManagerFactory entityManagerFactory)
	{
		super();
		setEntityManagerFactory(entityManagerFactory);
	}
	
	
}
