package net.sf.gilead.adapter4appengine.datanucleus;

import javax.jdo.identity.ObjectIdentity;
import javax.jdo.spi.PersistenceCapable;
import javax.servlet.http.HttpSession;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.repackaged.org.apache.commons.logging.Log;
import com.google.appengine.repackaged.org.apache.commons.logging.LogFactory;

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
	private static JdoEntityStore instance = null;

	/**
	 * @return the unique instance of the singleton
	 */
	public static JdoEntityStore getInstance() 
	{
		if (instance == null)
		{
			instance = new JdoEntityStore();
		}
		return instance;
	}
	
	/**
	 * Private constructor.
	 */
	private JdoEntityStore()
	{
		
	}
	
	//----
	// Attributes
	//----
	/**
	 * Log channel
	 */
	private static Log log = LogFactory.getLog(JdoEntityStore.class);
	
	/**
	 * The current HTTP session
	 */
	private HttpSession httpSession;

	//----
	// Properties
	//----
	/**
	 * @return the httpSession
	 */
	public HttpSession getHttpSession()
	{
		return httpSession;
	}

	/**
	 * @param httpSession the httpSession to set
	 */
	public void setHttpSession(HttpSession httpSession)
	{
		this.httpSession = httpSession;
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
			log.info("Storing entity " + entity + " with ID " + id);
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
		log.info("Search entity with ID " + id);
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
