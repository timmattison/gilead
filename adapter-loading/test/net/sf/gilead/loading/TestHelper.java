package net.sf.gilead.loading;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.gilead.loading.domain.Account;
import net.sf.gilead.loading.domain.Message;
import net.sf.gilead.loading.domain.Right;
import net.sf.gilead.loading.domain.User;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;

/**
 * Test helper static class.
 * @author bruno.marchesson
 *
 */
public class TestHelper
{
	//----
	// Constants
	//----
	/**
	 * Configuration file
	 */
	private static final String CONFIGURATION_FILE = "net/sf/gilead/loading/resource/hibernate.cfg.xml";
	
	//-------------------------------------------------------------------------
	//
	// Static helper
	//
	//-------------------------------------------------------------------------
	/**
	 * Create Hibernate session factory
	 */
	public static SessionFactory createSessionFactory()
	{
	//	Init Hibernate session factory
	//
		SessionFactory sessionFactory = new AnnotationConfiguration().configure(CONFIGURATION_FILE).buildSessionFactory();
		
		// init DB if needed
		initDB(sessionFactory);
		
		return sessionFactory;
	}
	
	/**
	 * Query execution
	 */
	@SuppressWarnings("unchecked")
	public static List executeQuery(String query)
	{
		return executeQuery(createSessionFactory(), query);
	}
	
	/**
	 * Save the argument entity.
	 * @param sessionFactory
	 * @param user
	 */
	public static void save(SessionFactory sessionFactory, Object entity)
	{
	//	Init session and transaction
	//
		Session session = null;
		Transaction transaction = null;
		try
		{
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			
		//	Save user
		//
			session.saveOrUpdate(entity);
			
			transaction.commit();
		}
		catch(Exception e)
		{
			transaction.rollback();
			throw new RuntimeException(e);
		}
		finally
		{
			if (session != null)
			{
				session.close();
			}
		}
		
	}
	
	/**
	 * Load an entity.
	 * @param sessionFactory
	 * @param user
	 */
	public static Object load(SessionFactory sessionFactory, 
							  Class<?> entityClass,
							  Serializable id)
	{
	//	Init session
	//
		Session session = null;
		try
		{
			session = sessionFactory.openSession();
			
		//	load user
		//
			return session.get(entityClass, id);
		}
		finally
		{
			if (session != null)
			{
				session.close();
			}
		}
	}

	//-------------------------------------------------------------------------
	//
	// Internal methods
	//
	//-------------------------------------------------------------------------
	/**
	 * Init database if needed
	 */
	private static void initDB(SessionFactory sessionFactory)
	{
		if (countUsers(sessionFactory) == 0)
		{
		//	Create user
		//
			User user = new User();
			user.setFirstName("firstName");
			user.setLastName("lastName");
			
			// Create account
			Account account = new Account();
			account.setLogin("login");
			account.setDomain("domain");
			account.setUser(user);
			
			// create right
			Right right = new Right();
			right.setName("right");
			right.setValue(1);
			
			Set<Account> accountList = new HashSet<Account>();
			accountList.add(account);
			right.setAccountList(accountList);
			
			Set<Right> rightList = new HashSet<Right>();
			rightList.add(right);
			account.setRightList(rightList);
			
			user.setAccount(account);
			
			// Create messages
			Message message1 = new Message();
			message1.setAuthor(user);
			message1.setDate(new Date());
			message1.setText("message1");
			
			Message message2 = new Message();
			message2.setAuthor(user);
			message2.setDate(new Date());
			message2.setText("message2");
			
			Set<Message> messageList = new HashSet<Message>();
			messageList.add(message1);
			messageList.add(message2);
			user.setMessageList(messageList);
			
			// Save user
			save(sessionFactory, user);
		}
	}
	
	/**
	 * Init database if needed
	 */
	private static int countUsers(SessionFactory sessionFactory)
	{
	//	Init session and transaction
	//
		Session session = null;
		try
		{
			session = sessionFactory.openSession();
	
		//	Is there any User in DB ?
		//
			Query query = session.createQuery("select count(id) from User");
			return ((Long)query.uniqueResult()).intValue();
		}
		finally
		{
			if (session != null)
			{
				session.close();
			}
		}
	}
	
	/**
	 * Execute a request
	 * @param sessionFactory
	 * @param query
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static List executeQuery(SessionFactory sessionFactory, String query)
	{
		Session session = null;
		try
		{
			session = sessionFactory.openSession();
			return session.createQuery(query).list();
		}
		finally
		{
			if (session != null)
			{
				session.close();
			}
		}
	}

}
