/**
 * 
 */
package net.sf.gilead.loading.session;

import java.io.Serializable;
import java.sql.Connection;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sf.gilead.loading.IQuery;
import net.sf.gilead.loading.QueryManager;
import net.sf.gilead.loading.annotations.LoadingInterface;
import net.sf.gilead.loading.hql.HQLQueryGenerator;
import net.sf.gilead.loading.proxy.LoadingProxyManager;
import net.sf.gilead.loading.proxy.LoadingWrapper;

import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.EntityMode;
import org.hibernate.Filter;
import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.ReplicationMode;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import org.hibernate.stat.SessionStatistics;
import org.hibernate.type.Type;

/**
 * Wrapper for Hibernate _wrappedSession
 * 
 * @author bruno.marchesson
 * 
 */
public class LoadingSession implements Session {
	// -----
	// Attribute
	// ----
	/**
	 * The wrapped _wrappedSession
	 */
	private Session _wrappedSession;

	/**
	 * The query manager
	 */
	private QueryManager _queryManager;

	// -------------------------------------------------------------------------
	//
	// Constructor
	//
	// -------------------------------------------------------------------------
	/**
	 * Constructor
	 */
	public LoadingSession(Session session) {
		_wrappedSession = session;

		_queryManager = new QueryManager();
		_queryManager.setSessionFactory(session.getSessionFactory());
		_queryManager.setQueryGenerator(new HQLQueryGenerator());
	}

	// -------------------------------------------------------------------------
	//
	// Methods overrides
	//
	// -------------------------------------------------------------------------
	public Object get(Class clazz, Serializable id) throws HibernateException {
		if (clazz.getAnnotation(LoadingInterface.class) == null) {
			return _wrappedSession.get(clazz, id);
		} else {
			// Loading interface : generate dedicated query
			//
			IQuery loadingQuery = _queryManager.generateQuery(clazz);

			// Create complete query
			String hqlQuery = loadingQuery.toString();
			hqlQuery += " where item.id=:id";

			// Fill query
			Query query = createQuery(hqlQuery);
			query.setParameter("id", id);

			// Execute query
			return LoadingProxyManager.getInstance().wrapAs(query.uniqueResult(),
														    clazz);
		}
	}

	// ------------------------------------------------------------------------
	//
	// Loading class handling
	//
	// ------------------------------------------------------------------------
	public void delete(Object object) throws HibernateException
	{
		if (object instanceof LoadingWrapper)
		{
			object = ((LoadingWrapper)object).getData();
		}
		_wrappedSession.delete(object);
	}

	public void delete(String entityName, Object object)
			throws HibernateException
	{
		if (object instanceof LoadingWrapper)
		{
			object = ((LoadingWrapper)object).getData();
		}
		_wrappedSession.delete(entityName, object);
	}

	public void evict(Object object) throws HibernateException
	{
		if (object instanceof LoadingWrapper)
		{
			object = ((LoadingWrapper)object).getData();
		}
		_wrappedSession.evict(object);
	}

	public LockMode getCurrentLockMode(Object object) throws HibernateException
	{
		if (object instanceof LoadingWrapper)
		{
			object = ((LoadingWrapper)object).getData();
		}
		return _wrappedSession.getCurrentLockMode(object);
	}

	public String getEntityName(Object object) throws HibernateException
	{
		if (object instanceof LoadingWrapper)
		{
			object = ((LoadingWrapper)object).getData();
		}
		return _wrappedSession.getEntityName(object);
	}

	public Serializable getIdentifier(Object object) throws HibernateException
	{
		if (object instanceof LoadingWrapper)
		{
			object = ((LoadingWrapper)object).getData();
		}
		return _wrappedSession.getIdentifier(object);
	}

	public void lock(Object object, LockMode lockMode)
			throws HibernateException
	{
		if (object instanceof LoadingWrapper)
		{
			object = ((LoadingWrapper)object).getData();
		}
		_wrappedSession.lock(object, lockMode);
	}

	public void lock(String entityName, Object object, LockMode lockMode)
			throws HibernateException
	{
		if (object instanceof LoadingWrapper)
		{
			object = ((LoadingWrapper)object).getData();
		}
		_wrappedSession.lock(entityName, object, lockMode);
	}

	public Object merge(Object object) throws HibernateException
	{
		if (object instanceof LoadingWrapper)
		{
			object = ((LoadingWrapper)object).getData();
		}
		return LoadingProxyManager.getInstance().wrapAs(_wrappedSession.merge(object),
														object.getClass().getInterfaces()[0]);
	}

	public Object merge(String entityName, Object object)
			throws HibernateException
	{
		if (object instanceof LoadingWrapper)
		{
			object = ((LoadingWrapper)object).getData();
		}
		return LoadingProxyManager.getInstance().wrapAs(_wrappedSession.merge(entityName, object),
														object.getClass().getInterfaces()[0]);
	}

	public void persist(Object object) throws HibernateException
	{
		if (object instanceof LoadingWrapper)
		{
			object = ((LoadingWrapper)object).getData();
		}
		_wrappedSession.persist(object);
	}

	public void persist(String entityName, Object object)
			throws HibernateException
	{
		if (object instanceof LoadingWrapper)
		{
			object = ((LoadingWrapper)object).getData();
		}
		_wrappedSession.persist(entityName, object);
	}

	public void refresh(Object object, LockMode lockMode)
			throws HibernateException
	{
		if (object instanceof LoadingWrapper)
		{
			object = ((LoadingWrapper)object).getData();
		}
		_wrappedSession.refresh(object, lockMode);
	}

	public void refresh(Object object) throws HibernateException
	{
		if (object instanceof LoadingWrapper)
		{
			object = ((LoadingWrapper)object).getData();
		}
		_wrappedSession.refresh(object);
	}

	public void replicate(Object object, ReplicationMode replicationMode)
			throws HibernateException
	{
		if (object instanceof LoadingWrapper)
		{
			object = ((LoadingWrapper)object).getData();
		}
		_wrappedSession.replicate(object, replicationMode);
	}

	public void replicate(String entityName, Object object,
			ReplicationMode replicationMode) throws HibernateException
	{
		if (object instanceof LoadingWrapper)
		{
			object = ((LoadingWrapper)object).getData();
		}
		_wrappedSession.replicate(entityName, object, replicationMode);
	}

	public Serializable save(Object object) throws HibernateException
	{
		if (object instanceof LoadingWrapper)
		{
			object = ((LoadingWrapper)object).getData();
		}
		return _wrappedSession.save(object);
	}

	public Serializable save(String entityName, Object object)
			throws HibernateException
	{
		if (object instanceof LoadingWrapper)
		{
			object = ((LoadingWrapper)object).getData();
		}
		return _wrappedSession.save(entityName, object);
	}

	public void saveOrUpdate(Object object) throws HibernateException
	{
		if (object instanceof LoadingWrapper)
		{
			object = ((LoadingWrapper)object).getData();
		}
		_wrappedSession.saveOrUpdate(object);
	}

	public void saveOrUpdate(String entityName, Object object)
			throws HibernateException
	{
		if (object instanceof LoadingWrapper)
		{
			object = ((LoadingWrapper)object).getData();
		}
		_wrappedSession.saveOrUpdate(entityName, object);
	}

	public void setReadOnly(Object entity, boolean readOnly)
	{
		if (entity instanceof LoadingWrapper)
		{
			entity = ((LoadingWrapper)entity).getData();
		}
		_wrappedSession.setReadOnly(entity, readOnly);
	}

	public void update(Object object) throws HibernateException
	{
		if (object instanceof LoadingWrapper)
		{
			object = ((LoadingWrapper)object).getData();
		}
		_wrappedSession.update(object);
	}

	public void update(String entityName, Object object)
			throws HibernateException 
	{
		if (object instanceof LoadingWrapper)
		{
			object = ((LoadingWrapper)object).getData();
		}
		_wrappedSession.update(entityName, object);
	}

	// -------------------------------------------------------------------------
	//
	// Session implementation
	//
	// -------------------------------------------------------------------------
	public Transaction beginTransaction() throws HibernateException {
		return _wrappedSession.beginTransaction();
	}

	public void cancelQuery() throws HibernateException {
		_wrappedSession.cancelQuery();
	}

	public void clear() {
		_wrappedSession.clear();
	}

	public Connection close() throws HibernateException {
		return _wrappedSession.close();
	}

	public Connection connection() throws HibernateException {
		return _wrappedSession.connection();
	}

	public boolean contains(Object object) {
		return _wrappedSession.contains(object);
	}

	public Criteria createCriteria(Class persistentClass, String alias) {
		return _wrappedSession.createCriteria(persistentClass, alias);
	}

	public Criteria createCriteria(Class persistentClass) {
		return _wrappedSession.createCriteria(persistentClass);
	}

	public Criteria createCriteria(String entityName, String alias) {
		return _wrappedSession.createCriteria(entityName, alias);
	}

	public Criteria createCriteria(String entityName) {
		return _wrappedSession.createCriteria(entityName);
	}

	public Query createFilter(Object collection, String queryString)
			throws HibernateException {
		return _wrappedSession.createFilter(collection, queryString);
	}

	public Query createQuery(String queryString) throws HibernateException {
		return _wrappedSession.createQuery(queryString);
	}

	public SQLQuery createSQLQuery(String queryString)
			throws HibernateException {
		return _wrappedSession.createSQLQuery(queryString);
	}

	public void disableFilter(String filterName) {
		_wrappedSession.disableFilter(filterName);
	}

	public Connection disconnect() throws HibernateException {
		return _wrappedSession.disconnect();
	}

	public Filter enableFilter(String filterName) {
		return _wrappedSession.enableFilter(filterName);
	}

	public void flush() throws HibernateException {
		_wrappedSession.flush();
	}

	public Object get(Class clazz, Serializable id, LockMode lockMode)
			throws HibernateException {
		return _wrappedSession.get(clazz, id, lockMode);
	}

	public Object get(String entityName, Serializable id, LockMode lockMode)
			throws HibernateException {
		return _wrappedSession.get(entityName, id, lockMode);
	}

	public Object get(String entityName, Serializable id)
			throws HibernateException {
		return _wrappedSession.get(entityName, id);
	}

	public CacheMode getCacheMode() {
		return _wrappedSession.getCacheMode();
	}

	public Filter getEnabledFilter(String filterName) {
		return _wrappedSession.getEnabledFilter(filterName);
	}

	public EntityMode getEntityMode() {
		return _wrappedSession.getEntityMode();
	}

	public FlushMode getFlushMode() {
		return _wrappedSession.getFlushMode();
	}

	public Query getNamedQuery(String queryName) throws HibernateException {
		return _wrappedSession.getNamedQuery(queryName);
	}

	public org.hibernate.Session getSession(EntityMode entityMode) {
		return _wrappedSession.getSession(entityMode);
	}

	public SessionFactory getSessionFactory() {
		return _wrappedSession.getSessionFactory();
	}

	public SessionStatistics getStatistics() {
		return _wrappedSession.getStatistics();
	}

	public Transaction getTransaction() {
		return _wrappedSession.getTransaction();
	}

	public boolean isConnected() {
		return _wrappedSession.isConnected();
	}

	public boolean isDirty() throws HibernateException {
		return _wrappedSession.isDirty();
	}

	public boolean isOpen() {
		return _wrappedSession.isOpen();
	}

	public Object load(Class theClass, Serializable id, LockMode lockMode)
			throws HibernateException {
		return _wrappedSession.load(theClass, id, lockMode);
	}

	public Object load(Class theClass, Serializable id)
			throws HibernateException {
		return _wrappedSession.load(theClass, id);
	}

	public void load(Object object, Serializable id) throws HibernateException {
		_wrappedSession.load(object, id);
	}

	public Object load(String entityName, Serializable id, LockMode lockMode)
			throws HibernateException {
		return _wrappedSession.load(entityName, id, lockMode);
	}

	public Object load(String entityName, Serializable id)
			throws HibernateException {
		return _wrappedSession.load(entityName, id);
	}
	
	public void reconnect(Connection connection) throws HibernateException {
		_wrappedSession.reconnect(connection);
	}

	public void setCacheMode(CacheMode cacheMode) {
		_wrappedSession.setCacheMode(cacheMode);
	}

	public void setFlushMode(FlushMode flushMode) {
		_wrappedSession.setFlushMode(flushMode);
	}

	// -------------------------------------------------------------------------
	//
	// Deprecated (Hibernate2) methods
	//
	// -------------------------------------------------------------------------
	public void reconnect() throws HibernateException {
		_wrappedSession.reconnect();
	}

	public Query createSQLQuery(String sql, String returnAlias,
			Class returnClass) {
		return _wrappedSession.createSQLQuery(sql, returnAlias, returnClass);
	}

	public Query createSQLQuery(String sql, String[] returnAliases,
			Class[] returnClasses) {
		return _wrappedSession
				.createSQLQuery(sql, returnAliases, returnClasses);
	}

	public int delete(String query, Object value, Type type)
			throws HibernateException {
		return _wrappedSession.delete(query, value, type);
	}

	public int delete(String query, Object[] values, Type[] types)
			throws HibernateException {
		return _wrappedSession.delete(query, values, types);
	}

	public int delete(String query) throws HibernateException {
		return _wrappedSession.delete(query);
	}

	public Collection filter(Object collection, String filter, Object value,
			Type type) throws HibernateException {
		return _wrappedSession.filter(collection, filter, value, type);
	}

	public Collection filter(Object collection, String filter, Object[] values,
			Type[] types) throws HibernateException {
		return _wrappedSession.filter(collection, filter, values, types);
	}

	public Collection filter(Object collection, String filter)
			throws HibernateException {
		return _wrappedSession.filter(collection, filter);
	}

	public List find(String query, Object value, Type type)
			throws HibernateException {
		return _wrappedSession.find(query, value, type);
	}

	public List find(String query, Object[] values, Type[] types)
			throws HibernateException {
		return _wrappedSession.find(query, values, types);
	}

	public List find(String query) throws HibernateException {
		return _wrappedSession.find(query);
	}

	public Iterator iterate(String query, Object value, Type type)
			throws HibernateException {
		return _wrappedSession.iterate(query, value, type);
	}

	public Iterator iterate(String query, Object[] values, Type[] types)
			throws HibernateException {
		return _wrappedSession.iterate(query, values, types);
	}

	public Iterator iterate(String query) throws HibernateException {
		return _wrappedSession.iterate(query);
	}

	public void save(Object object, Serializable id) throws HibernateException {
		_wrappedSession.save(object, id);
	}

	public void save(String entityName, Object object, Serializable id)
			throws HibernateException {
		_wrappedSession.save(entityName, object, id);
	}

	public Object saveOrUpdateCopy(Object object, Serializable id)
			throws HibernateException {
		return _wrappedSession.saveOrUpdateCopy(object, id);
	}

	public Object saveOrUpdateCopy(Object object) throws HibernateException {
		return _wrappedSession.saveOrUpdateCopy(object);
	}

	public Object saveOrUpdateCopy(String entityName, Object object,
			Serializable id) throws HibernateException {
		return _wrappedSession.saveOrUpdateCopy(entityName, object, id);
	}

	public Object saveOrUpdateCopy(String entityName, Object object)
			throws HibernateException {
		return _wrappedSession.saveOrUpdateCopy(entityName, object);
	}

	public void update(Object object, Serializable id)
			throws HibernateException {
		_wrappedSession.update(object, id);
	}

	public void update(String entityName, Object object, Serializable id)
			throws HibernateException {
		_wrappedSession.update(entityName, object, id);
	}
}
