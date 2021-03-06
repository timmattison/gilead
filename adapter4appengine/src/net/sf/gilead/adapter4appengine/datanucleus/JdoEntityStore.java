package net.sf.gilead.adapter4appengine.datanucleus;

import java.util.logging.Logger;

import javax.jdo.identity.ObjectIdentity;
import javax.jdo.spi.PersistenceCapable;
import javax.servlet.http.HttpSession;

import com.google.appengine.api.datastore.Key;

/**
 * Store for persistent entity, based on HTTP session
 * @author bruno.marchesson
 *
 */
public class JdoEntityStore 
{
	//----
	// Singleton
	//----
	/**
	 * The unique instance of the singleton
	 */
	private static JdoEntityStore _instance = null;

	/**
	 * @return the unique instance of the singleton
	 */
	public static JdoEntityStore getInstance() 
	{
		if (_instance == null)
		{
			_instance = new JdoEntityStore();
		}
		return _instance;
	}
	
	/**
	 * Private constructor.
	 */
	private JdoEntityStore()
	{
		_httpSession = new ThreadLocal<HttpSession>();
	}
	
	//----
	// Attributes
	//----
	/**
	 * Logger channel
	 */
	private static final Logger _log = Logger.getLogger(JdoEntityStore.class.getName());
	
	/**
	 * The current HTTP session
	 */
	private ThreadLocal<HttpSession> _httpSession;

	//----
	// Properties
	//----
	/**
	 * @return the httpSession
	 */
	public HttpSession getHttpSession()
	{
		return _httpSession.get();
	}

	/**
	 * @param httpSession the httpSession to set
	 */
	public void setHttpSession(HttpSession httpSession)
	{
		_httpSession.set(httpSession);
	}
	
	//-------------------------------------------------------------------------
	//
	// Public interface
	//
	//-------------------------------------------------------------------------
	/**
	 * Store the entity in HTTP session
	 */
	public void storeEntity(PersistenceCapable entity)
	{
	//	Precondition checking
	//
		HttpSession httpSession = getHttpSession();
		if (httpSession == null)
		{
			throw new NullPointerException("HTTP session not set !");
		}
	
	//	Get ID
	//	
		Object id = entity.jdoGetObjectId();
		if (id != null)
		{
		//	Store JDO in HTTP session
		//
			_log.info("Storing entity " + entity + " with ID " + id);
			String idKey = computeIdKey(id);
			httpSession.setAttribute(idKey, entity);
		}
	}
	
	/**
	 * @return the entity if found, null otherwise
	 */
	public PersistenceCapable getEntity(Object id)
	{
	//	Precondition checking
	//
		HttpSession httpSession = getHttpSession();
		if (httpSession == null)
		{
			throw new NullPointerException("HTTP session not set !");
		}
		if (id == null)
		{
			return null;
		}
		
	//	Get entity from HTTP session
	//
		_log.info("Search entity with ID " + id);
		String idKey = computeIdKey(id);
		return (PersistenceCapable) httpSession.getAttribute(idKey);
	}
	
	//-------------------------------------------------------------------------
	//
	// Internal methods
	//
	//-------------------------------------------------------------------------
	/**
	 * Compute ID key
	 * @param id
	 * @return
	 */
	protected String computeIdKey(Object id)
	{
	//	Special handling for ObjectEntity
	//
		if (id instanceof ObjectIdentity)
		{
			id = ((ObjectIdentity)id).getKeyAsObject();
		}
		
	//	Key handling
	//
		if (id instanceof Key)
		{
		//	Use underlying id
		//
			return Long.toString(((Key) id).getId());
		}
		else
		{
		//	Use toString
		//
			return id.toString();
		}
	}
}
