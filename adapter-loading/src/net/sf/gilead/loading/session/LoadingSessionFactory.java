/**
 * 
 */
package net.sf.gilead.loading.session;

import java.io.Serializable;
import java.sql.Connection;
import java.util.Map;
import java.util.Set;

import javax.naming.NamingException;
import javax.naming.Reference;

import net.sf.gilead.loading.LoadingHelper;
import net.sf.gilead.loading.exception.NoLoadingInterfaceException;

import org.hibernate.HibernateException;
import org.hibernate.Interceptor;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.classic.Session;
import org.hibernate.engine.FilterDefinition;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.metadata.CollectionMetadata;
import org.hibernate.stat.Statistics;

/**
 * @author bruno.marchesson
 *
 */
public class LoadingSessionFactory implements SessionFactory
{
	//----
	// Attributes
	//----
	/**
	 * The wrapped session factory
	 */
	private SessionFactory sessionFactory;
	
	//-------------------------------------------------------------------------
	//
	// Constructor
	//
	//-------------------------------------------------------------------------
	/**
	 * Constructor
	 */
	public LoadingSessionFactory(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}
	
	//-------------------------------------------------------------------------
	//
	// Session overrides
	//
	//-------------------------------------------------------------------------
	public Session openSession() throws HibernateException
	{
		return new LoadingSession(sessionFactory.openSession());
	}

	public Session openSession(Connection connection, Interceptor interceptor)
	{
		return new LoadingSession(sessionFactory.openSession(connection, interceptor));
	}

	public Session openSession(Connection connection)
	{
		return new LoadingSession(sessionFactory.openSession(connection));
	}

	public Session openSession(Interceptor interceptor) throws HibernateException
	{
		return new LoadingSession(sessionFactory.openSession(interceptor));
	}

	// TODO Stateless sessions
	public StatelessSession openStatelessSession()
	{
		return sessionFactory.openStatelessSession();
	}

	// TODO Stateless sessions
	public StatelessSession openStatelessSession(Connection connection)
	{
		return sessionFactory.openStatelessSession(connection);
	}

	//-------------------------------------------------------------------------
	//
	// Class overrides
	//
	//-------------------------------------------------------------------------
	public void evict(Class persistentClass, Serializable id) throws HibernateException
	{
		try
		{
		//	Get underlying persistent class if any
		//
			sessionFactory.evict(LoadingHelper.getPersistentClass(persistentClass), id);
		}
		catch(NoLoadingInterfaceException ex)
		{
		//	Base operation
		//
			sessionFactory.evict(persistentClass, id);
		}
	}
	
	public void evict(Class persistentClass) throws HibernateException
	{
		try
		{
		//	Get underlying persistent class if any
		//
			sessionFactory.evict(LoadingHelper.getPersistentClass(persistentClass));
		}
		catch(NoLoadingInterfaceException ex)
		{
		//	Base operation
		//
			sessionFactory.evict(persistentClass);
		}
	}
	
	public ClassMetadata getClassMetadata(Class persistentClass) throws HibernateException
	{
		try
		{
		//	Get underlying persistent class if any
		//
			return sessionFactory.getClassMetadata(LoadingHelper.getPersistentClass(persistentClass));
		}
		catch(NoLoadingInterfaceException ex)
		{
		//	Base operation
		//	
			return sessionFactory.getClassMetadata(persistentClass);
		}
	}

	//-------------------------------------------------------------------------
	//
	// Session Factory delegates
	//
	//-------------------------------------------------------------------------
	public void close() throws HibernateException {
		sessionFactory.close();
	}
	
	public void evictCollection(String roleName, Serializable id)
			throws HibernateException {
		sessionFactory.evictCollection(roleName, id);
	}

	public void evictCollection(String roleName) throws HibernateException {
		sessionFactory.evictCollection(roleName);
	}

	public void evictEntity(String entityName, Serializable id)
			throws HibernateException {
		sessionFactory.evictEntity(entityName, id);
	}

	public void evictEntity(String entityName) throws HibernateException {
		sessionFactory.evictEntity(entityName);
	}

	public void evictQueries() throws HibernateException {
		sessionFactory.evictQueries();
	}

	public void evictQueries(String cacheRegion) throws HibernateException {
		sessionFactory.evictQueries(cacheRegion);
	}

	public Map getAllClassMetadata() throws HibernateException {
		return sessionFactory.getAllClassMetadata();
	}

	public Map getAllCollectionMetadata() throws HibernateException {
		return sessionFactory.getAllCollectionMetadata();
	}

	public ClassMetadata getClassMetadata(String entityName)
			throws HibernateException {
		return sessionFactory.getClassMetadata(entityName);
	}

	public CollectionMetadata getCollectionMetadata(String roleName)
			throws HibernateException {
		return sessionFactory.getCollectionMetadata(roleName);
	}

	public Session getCurrentSession() throws HibernateException {
		return sessionFactory.getCurrentSession();
	}

	public Set getDefinedFilterNames() {
		return sessionFactory.getDefinedFilterNames();
	}

	public FilterDefinition getFilterDefinition(String filterName)
			throws HibernateException {
		return sessionFactory.getFilterDefinition(filterName);
	}

	public Reference getReference() throws NamingException {
		return sessionFactory.getReference();
	}

	public Statistics getStatistics() {
		return sessionFactory.getStatistics();
	}

	public boolean isClosed() {
		return sessionFactory.isClosed();
	}
}
