package net.sf.gilead.core;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import net.sf.gilead.core.beanlib.mapper.DirectoryClassMapper;
import net.sf.gilead.core.beanlib.mapper.ProxyClassMapper;
import net.sf.gilead.core.hibernate.HibernateUtil;
import net.sf.gilead.core.store.stateful.InMemoryProxyStore;
import net.sf.gilead.core.store.stateless.StatelessProxyStore;
import net.sf.gilead.test.DAOFactory;
import net.sf.gilead.test.HibernateContext;
import net.sf.gilead.test.HibernateContext.Context;
import net.sf.gilead.test.domain.IEmployee;
import net.sf.gilead.test.domain.IMessage;
import net.sf.gilead.test.domain.IUser;

/**
 * Static helper class for test
 * @author bruno.marchesson
 *
 */
public class TestHelper
{
	//----
	// Constant
	//----
	/**
	 * The guest login
	 */
	public final static String GUEST_LOGIN = "guest";
	
	/**
	 * The JUnit login
	 */
	public final static String JUNIT_LOGIN = "junit";
	
	/**
	 * The employee login
	 */
	public final static String EMPLOYEE_LOGIN = "employee";
	

	//-------------------------------------------------------------------------
	//
	// Static helper
	//
	//-------------------------------------------------------------------------
	/**
	 * Check that the database is initialized
	 * @return
	 */
	public static boolean isInitialized()
	{
		return (DAOFactory.getUserDAO().countAll() > 0);
	}
	
	/**
	 * Initialise DB
	 */
	public static void initializeDB()
	{
	//	Create guest user (no password)
	//
		IUser guestUser = createUser();
		guestUser.setLogin(GUEST_LOGIN);
		guestUser.setFirstName("No");
		guestUser.setLastName("name");
		
		// create welcome message
		IMessage guestMessage = createMessage();
		guestMessage.setMessage("Welcome in hibernate4gwt sample application");
		guestMessage.setDate(new Date());
		computeKeywords(guestMessage);
		guestUser.addMessage(guestMessage);
		
		// save user (message is cascaded)
		DAOFactory.getUserDAO().saveUser(guestUser);
		
	//	Create JUnit user
	//
		IUser junitUser = createUser();
		junitUser.setLogin(JUNIT_LOGIN);
		junitUser.setPassword("junit");
		junitUser.setFirstName("Unit");
		junitUser.setLastName("Test");
		
		// create message
		IMessage junitMessage = createMessage();
		junitMessage.setMessage("JUnit first message");
		junitMessage.setDate(new Date());
		computeKeywords(junitMessage);
		junitUser.addMessage(junitMessage);
		
		// save user (message is cascaded)
		DAOFactory.getUserDAO().saveUser(junitUser);
		
	//	Create Employee user
	//
		IEmployee employee = createEmployee();
		employee.setLogin(EMPLOYEE_LOGIN);
		employee.setPassword("employee");
		employee.setFirstName("John");
		employee.setLastName("Doe");
		employee.setEmail("john.doe@gilead.com");
		
		// create message
		IMessage employeeMessage = createMessage();
		employeeMessage.setMessage("John Doe's message");
		employeeMessage.setDate(new Date());
		computeKeywords(employeeMessage);
		employee.addMessage(employeeMessage);
		
		// save user (message is cascaded)
		DAOFactory.getUserDAO().saveUser(employee);
	}
	
	/**
	 * @return an existing user ID
	 */
	public static Serializable getExistingUserId()
	{
	//	Load user list
	//
		List<IUser> userList = DAOFactory.getUserDAO().loadAll();
		
		if ((userList == null) ||
			(userList.isEmpty() == true))
		{
			return null;
		}
		else
		{
		//	Return first element
		//
			return userList.get(0).getId();
		}
	}
	
	/**
	 * @return an existing message ID
	 */
	public static Integer getExistingMessageId()
	{
	//	Load user list
	//
		List<IMessage> messageList = DAOFactory.getMessageDAO().loadAllMessage(0, 1);
		
		if ((messageList == null) ||
			(messageList.isEmpty() == true))
		{
			return null;
		}
		else
		{
		//	Return first element
		//
			return messageList.get(0).getId();
		}
	}
	
	/**
	 * Compute keywords for message
	 * @param message the message to save
	 */
	public static void computeKeywords(IMessage message)
	{
		message.clearKeywords();
		
	//	Computation of keywords (fake, of course)
	//
		String text = message.getMessage();
		
		// Keywords update
		StringTokenizer tokenizer = new StringTokenizer(text);
		while (tokenizer.hasMoreTokens())
		{
			String token = tokenizer.nextToken();
			if (token.length() > 3)
			{
				message.addKeyword(token, token.length());
			}
		}
	}
	
	//-------------------------------------------------------------------------
	//
	// Bean manager initialisation
	//
	//-------------------------------------------------------------------------
	/**
	 * Init bean manager for stateless mode
	 */
	public static PersistentBeanManager initStatelessBeanManager()
	{
		HibernateContext.setContext(HibernateContext.Context.stateless);
		
		HibernateUtil persistenceUtil = new HibernateUtil(); 
		persistenceUtil.setSessionFactory(HibernateContext.getSessionFactory());
		
		PersistentBeanManager beanManager = new PersistentBeanManager();
		beanManager.setPersistenceUtil(persistenceUtil);
		beanManager.setProxyStore(new StatelessProxyStore());
		
		return beanManager;
	}
	
	/**
	 * Init bean manager for stateless mode
	 */
	public static PersistentBeanManager initStatefulBeanManager()
	{
		HibernateContext.setContext(HibernateContext.Context.stateful);
		
		HibernateUtil persistenceUtil = new HibernateUtil(); 
		persistenceUtil.setSessionFactory(HibernateContext.getSessionFactory());
		
		InMemoryProxyStore proxyStore = new InMemoryProxyStore();
		proxyStore.setPersistenceUtil(persistenceUtil);
		
		PersistentBeanManager beanManager = new PersistentBeanManager();
		beanManager.setPersistenceUtil(persistenceUtil);
		beanManager.setProxyStore(proxyStore);
		
		return beanManager;
	}
	
	/**
	 * Init bean manager for dynamic proxy mode
	 * @throws FileNotFoundException 
	 */
	public static PersistentBeanManager initProxyBeanManager() throws FileNotFoundException
	{
		HibernateContext.setContext(HibernateContext.Context.proxy);
		
		HibernateUtil persistenceUtil = new HibernateUtil(); 
		persistenceUtil.setSessionFactory(HibernateContext.getSessionFactory());
		
		PersistentBeanManager beanManager = new PersistentBeanManager();
		beanManager.setPersistenceUtil(persistenceUtil);
		beanManager.setProxyStore(new StatelessProxyStore());
		
		beanManager.setClassMapper(new ProxyClassMapper());
		
		return beanManager;
	}
	
	/**
	 * Init bean manager for Java5 support mode
	 */
	public static PersistentBeanManager initJava5SupportBeanManager() throws FileNotFoundException
	{
		HibernateContext.setContext(HibernateContext.Context.java5);
		
		HibernateUtil persistenceUtil = new HibernateUtil(); 
		persistenceUtil.setSessionFactory(HibernateContext.getSessionFactory());
		
		PersistentBeanManager beanManager = new PersistentBeanManager();
		beanManager.setPersistenceUtil(persistenceUtil);
		beanManager.setProxyStore(new StatelessProxyStore());
		
		DirectoryClassMapper classMapper = new DirectoryClassMapper();
		classMapper.setRootDomainPackage("net.sf.gilead.test.domain.java5");
		classMapper.setRootClonePackage("net.sf.gilead.test.domain.dto");
		classMapper.setCloneSuffix("DTO");
		beanManager.setClassMapper(classMapper);
		
		return beanManager;
	}
	
	/**
	 * Init bean manager for annotated Java5 mode
	 */
	public static PersistentBeanManager initJava5AnnotatedBeanManager() throws FileNotFoundException
	{
		HibernateContext.setContext(HibernateContext.Context.annotated);
		
		HibernateUtil persistenceUtil = new HibernateUtil(); 
		persistenceUtil.setSessionFactory(HibernateContext.getSessionFactory());
		
		PersistentBeanManager beanManager = new PersistentBeanManager();
		beanManager.setPersistenceUtil(persistenceUtil);
		beanManager.setProxyStore(new StatelessProxyStore());
		
		return beanManager;
	}
	
	//--------------------------------------------------------------------------
	//
	// Internal methods
	//
	//--------------------------------------------------------------------------
	/**
	 * Create a new user (depends on the server configuration)
	 */
	private static IUser createUser()
	{
		Context context = HibernateContext.getContext();
		
		if (context == Context.stateless) 
		{
			// stateless
			return new net.sf.gilead.test.domain.stateless.User();
		}
		else if (context == Context.stateful) 
		{
			// stateful
			return new net.sf.gilead.test.domain.stateful.User();
		}
		else if (context == Context.proxy) 
		{
			// dynamic proxy
			return new net.sf.gilead.test.domain.proxy.User();
		}
		else if (context == Context.java5)
		{
			// Java5
			return new net.sf.gilead.test.domain.java5.User();
		}
		else
		{
			// Annotated Java5
			return new net.sf.gilead.test.domain.annotated.User();
		}
	}
	
	/**
	 * Create a new employee (depends on the server configuration)
	 */
	private static IEmployee createEmployee()
	{
		Context context = HibernateContext.getContext();
		
		if (context == Context.stateless) 
		{
			// stateless
			return new net.sf.gilead.test.domain.stateless.Employee();
		}
		else if (context == Context.stateful) 
		{
			// stateful
			return new net.sf.gilead.test.domain.stateful.Employee();
		}
		else if (context == Context.proxy) 
		{
			// dynamic proxy
			return new net.sf.gilead.test.domain.proxy.Employee();
		}
		else if (context == Context.java5)
		{
			// Java5
			return new net.sf.gilead.test.domain.java5.Employee();
		}
		else
		{
			// Annotated Java5
			return new net.sf.gilead.test.domain.annotated.Employee();
		}
	}
	
	/**
	 * Create a new message (depends on the server configuration)
	 */
	private static IMessage createMessage()
	{
		Context context = HibernateContext.getContext();
		
		if (context == Context.stateless) 
		{
			// stateless
			return new net.sf.gilead.test.domain.stateless.Message();
		}
		else if (context == Context.stateful) 
		{
			// stateful
			return new net.sf.gilead.test.domain.stateful.Message();
		}
		else if (context == Context.proxy) 
		{
			// dynamic proxy
			return new net.sf.gilead.test.domain.proxy.Message();
		}
		else if (context == Context.java5) 
		{
			// Java5
			return new net.sf.gilead.test.domain.java5.Message();
		}
		else
		{
			// Annotated Java5
			return new net.sf.gilead.test.domain.annotated.Message();
		}
	}
}
