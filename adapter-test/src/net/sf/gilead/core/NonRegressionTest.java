package net.sf.gilead.core;

import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.List;

import junit.framework.TestCase;
import net.sf.gilead.test.HibernateContext;
import net.sf.gilead.test.domain.misc.Client;
import net.sf.gilead.test.domain.misc.Page;
import net.sf.gilead.test.domain.misc.PersistentException;
import net.sf.gilead.test.domain.misc.Photo;
import net.sf.gilead.test.domain.misc.Preference;
import net.sf.gilead.test.domain.misc.Project;
import net.sf.gilead.test.domain.misc.Utente;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Non regression test based on user feedback
 * @author bruno.marchesson
 *
 */
public class NonRegressionTest extends TestCase
{
	//-------------------------------------------------------------------------
	//
	// Test methods
	//
	//-------------------------------------------------------------------------
	/**
	 * Test preference replacement
	 * @throws FileNotFoundException 
	 */
	public void testChildReplacement() throws FileNotFoundException
	{
	//	Init bean manager
	//
		PersistentBeanManager beanManager = TestHelper.initJava5SupportBeanManager();
		
	//	Create test user and reload it
	//
		Utente utente = createTestUtente();
		utente = loadUser(utente.getId());
		
	//	Clone utente
	//
		utente = (Utente) beanManager.clone(utente);
		
	//	Update preferences
	//
		List<Preference> preferences = utente.getPreferences(); 
		for(Preference p:preferences)
		{ 
			p.setUser(null);  
		} 
		preferences.clear(); 
		
		Preference p1 = new Preference(); 
		p1.setUser(utente); 
		p1.setValue(6); 
		preferences.add(p1); 
		
	//	Merge and save user
	//
		utente = (Utente) beanManager.merge(utente);
		saveUser(utente);
		
	//	Reload it
	//
		Utente loaded = loadUser(utente.getId());
		assertNotNull(loaded.getPreferences());
		assertEquals(1, loaded.getPreferences().size());
	}
	
	/**
	 * Clone an exception that contains persistent attribute 
	 */
	public void testCloneException()
	{
		PersistentBeanManager beanManager = TestHelper.initStatelessBeanManager();
		
	//	Create exception
	//
		Page page = createTestPage();
		page = loadPage(page.getName());
		PersistentException exception = new PersistentException(page);
		
	//	Clone exception
	//
		PersistentException cloneException = (PersistentException) beanManager.clone(exception);
		assertNotNull(cloneException);
		assertNotNull(cloneException.getPage());
	}
	
	/**
	 * Test merge on many to many
	 */
	public void testMergeManyToMany() throws FileNotFoundException
	{
	//	Init bean manager
	//
		PersistentBeanManager beanManager = TestHelper.initJava5SupportBeanManager();
		
	//	Create test client
	//
		Client client = createTestClient();
		
	//	Clone client
	//
		client = (Client) beanManager.clone(client);
		
	//	Update projects
	//
		Collection<Project> projects = client.getProjects(); 
		for(Project p:projects)
		{ 
			p.getClients().remove(client);  
		} 
		projects.clear(); 
		
		Project p1 = new Project(); 
		p1.getClients().add(client); 
		projects.add(p1); 
		
	//	Merge client
	//
		client = (Client) beanManager.merge(client);
		assertEquals(1, client.getProjects().size());
	}
	
	//-------------------------------------------------------------------------
	//
	// Internal methods
	//
	//-------------------------------------------------------------------------
	/**
	 * Create a test entity
	 */
	private Utente createTestUtente()
	{
		// User 
		Utente user = new Utente();
		
		// Preferences
		Preference preference = new Preference();
		preference.setUser(user);
		preference.setValue(1);
		user.getPreferences().add(preference);
		
		// Save preference
		saveUser(user);
		
		return user;
	}
	
	/**
	 * Save User
	 */
	private void saveUser(Utente user)
	{
		Session session = null;
		Transaction transaction = null;
		try
		{
		//	Get session
		//
			session = HibernateContext.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();

		//	Save user
		//
			session.saveOrUpdate(user);
			transaction.commit();
		}
		catch (RuntimeException e)
		{
		//	Rollback
		//
			transaction.rollback();
			throw e;
		}
	}
	
	/**
     * Load the user with the argument id
     */
    public Utente loadUser(Integer id)
	{
    	Session session = null;
		Transaction transaction = null;
		try
		{
		//	Get session
		//
			session = HibernateContext.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();
	
			Utente user = (Utente) session.get(Utente.class, id);
			user.getPreferences().size();
			transaction.commit();
			
			return user;
		}
		catch (RuntimeException e)
		{
		//	Rollback
		//
			transaction.rollback();
			throw e;
		}
	}
    
    /**
	 * Create a test page
	 */
	private Page createTestPage()
	{
		// Page
		Page page = new Page();
		page.setName("Test page");
		
		// Photos
		Photo photo1 = new Photo();
		photo1.setUrl("Photo1");
		page.addPhoto(photo1);
		
		Photo photo2 = new Photo();
		photo2.setUrl("Photo2");
		page.addPhoto(photo2);
		
		Photo photo3 = new Photo();
		photo3.setUrl("Photo3");
		page.addPhoto(photo3);
		
		// Save page
		savePage(page);
		
		return page;
	}
	
	/**
	 * Save Page
	 */
	private void savePage(Page page)
	{
		Session session = null;
		Transaction transaction = null;
		try
		{
		//	Get session
		//
			session = HibernateContext.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();

		//	Save user
		//
			session.saveOrUpdate(page);
			transaction.commit();
			
		}
		catch (RuntimeException e)
		{
		//	Rollback
		//
			transaction.rollback();
			throw e;
		}
	}
	
	/**
     * Load the page with the argument name
     */
    public Page loadPage(String pageName)
	{
    	Session session = null;
		Transaction transaction = null;
		try
		{
		//	Get session
		//
			session = HibernateContext.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();
	
		//	Create query
		//
	    	StringBuffer hqlQuery = new StringBuffer();
	    	hqlQuery.append("select distinct page");
	    	hqlQuery.append(" from Page page");
	    	hqlQuery.append(" left outer join page.photoList");
	    	hqlQuery.append(" where page.name=:name");
	    	
	    //	Fill query
	    //
			Query query = session.createQuery(hqlQuery.toString());
			query.setString("name", pageName);
			
		//	Execute query
		//
			Page page = (Page) query.uniqueResult();
			page.getPhotoList().size();
			transaction.commit();
			
			return page;
		}
		catch (RuntimeException e)
		{
		//	Rollback
		//
			transaction.rollback();
			throw e;
		}
	}
    
    /**
	 * Create a test entity
	 */
	private Client createTestClient()
	{
		// Client
		Client client = new Client();
		
		// Projects
		Project project1 = new Project();
		project1.getClients().add(client);
		client.getProjects().add(project1);
		
		Project project2 = new Project();
		project2.getClients().add(client);
		client.getProjects().add(project2);
		
		// Save client
		saveClient(client);
		
		return client;
	}
	
	/**
	 * Save Client
	 */
	private void saveClient(Client client)
	{
		Session session = null;
		Transaction transaction = null;
		try
		{
		//	Get session
		//
			session = HibernateContext.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();

		//	Save user
		//
			session.saveOrUpdate(client);
			transaction.commit();
		}
		catch (RuntimeException e)
		{
		//	Rollback
		//
			transaction.rollback();
			throw e;
		}
	}
}
