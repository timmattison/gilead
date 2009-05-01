package net.sf.gilead.adapter4appengine.datanucleus;

import javax.jdo.spi.PersistenceCapable;
import javax.servlet.http.HttpSession;

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
	
	//	Get ID (TODO Key handling)
	//	
		Object id = entity.jdoGetObjectId();
		if (id != null)
		{
		//	Store JDO in HTTP session
		//
			log.info("Storing entity " + entity + " with ID " + id);
			httpSession.setAttribute(id.toString(), entity);
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
		return (PersistenceCapable) httpSession.getAttribute(id.toString());
	}
}
