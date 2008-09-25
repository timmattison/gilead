/**
 * 
 */
package net.sf.gilead.loading;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import net.sf.gilead.loading.domain.Account;
import net.sf.gilead.loading.domain.User;
import net.sf.gilead.loading.domain.interfaces.IAccountAndUser;
import net.sf.gilead.loading.domain.interfaces.ICompleteUser;
import net.sf.gilead.loading.domain.interfaces.ISimpleUser;
import net.sf.gilead.loading.domain.interfaces.IUserAndAccount;
import net.sf.gilead.loading.hql.HQLQueryGenerator;

import org.hibernate.Hibernate;
import org.junit.Before;
import org.junit.Test;

/**
 * Loading manager test
 * @author bruno.marchesson
 *
 */
public class QueryManagerTest
{
	//----
	// Attributes
	//----
	/**
	 * The loading manager to test
	 */
	private QueryManager _loadingManager;

	//-------------------------------------------------------------------------
	//
	// Test init
	//
	//-------------------------------------------------------------------------
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
	//	Create the loading manager
	//
		_loadingManager = new QueryManager();
		_loadingManager.setSessionFactory(TestHelper.createSessionFactory());
		_loadingManager.setQueryGenerator(new HQLQueryGenerator());
	}

	//-------------------------------------------------------------------------
	//
	// Test method
	//
	//-------------------------------------------------------------------------
	/**
	 * Test method for {@link net.sf.gilead.loading.QueryManager#generateQuery(java.lang.Class)}.
	 */
	@Test
	public final void testGenerateQueryWithSimplestInterface()
	{
	//	Generate query
	//
		IQuery query = _loadingManager.generateQuery(ISimpleUser.class);
		
	//	Post verification
	//
		assertNotNull(query);
		
		String hqlQuery = query.toString();
		assertNotNull(hqlQuery);
		assertEquals("from User item", hqlQuery);
		
	//	Test Hibernate loading
	//
		List<User> result = TestHelper.executeQuery(hqlQuery);
		assertNotNull(result);
		assertFalse(result.isEmpty());
		for (User user : result)
		{
		// 	One to one association is eager fetched :-(
		//	assertFalse(Hibernate.isInitialized(user.getAccount()));
			assertFalse(Hibernate.isInitialized(user.getMessageList()));
		}
	}
	
	/**
	 * Test one to one query generation with bidirectional association
	 */
	@Test
	public final void testGenerateQueryWithOneToOne_User()
	{
	//	Generate query
	//
		IQuery query = _loadingManager.generateQuery(IUserAndAccount.class);
		
	//	Post verification
	//
		assertNotNull(query);
		
		String hqlQuery = query.toString();
		assertNotNull(hqlQuery);
//		assertEquals("from User item inner join fetch item.account", hqlQuery);
		
	//	Test Hibernate loading
	//
		List<User> result = TestHelper.executeQuery(hqlQuery);
		assertNotNull(result);
		assertFalse(result.isEmpty());
		for (User user : result)
		{
			assertTrue(Hibernate.isInitialized(user.getAccount()));
			assertTrue(Hibernate.isInitialized(user.getAccount().getUser()));
			assertFalse(Hibernate.isInitialized(user.getMessageList()));
		}
	}
	
	/**
	 * Test one to one query generation with bidirectional association
	 */
	@Test
	public final void testGenerateQueryWithOneToOne_Account()
	{
	//	Generate query
	//
		IQuery query = _loadingManager.generateQuery(IAccountAndUser.class);
		
	//	Post verification
	//
		assertNotNull(query);
		
		String hqlQuery = query.toString();
		assertNotNull(hqlQuery);
//		assertEquals("from Account item inner join fetch item.user", hqlQuery);
		
	//	Test Hibernate loading
	//
		List<Account> result = TestHelper.executeQuery(hqlQuery);
		assertNotNull(result);
		assertFalse(result.isEmpty());
		for (Account account : result)
		{
			assertTrue(Hibernate.isInitialized(account.getUser()));
			assertTrue(Hibernate.isInitialized(account.getUser().getAccount()));
			assertFalse(Hibernate.isInitialized(account.getRightList()));
		}
	}
	
	/**
	 * Test complete tree query generation (without bidirectional association)
	 */
	@Test
	public final void testGenerateQueryWithCompleteTree()
	{
	//	Generate query
	//
		IQuery query = _loadingManager.generateQuery(ICompleteUser.class);
		
	//	Post verification
	//
		assertNotNull(query);
		
		String hqlQuery = query.toString();
		assertNotNull(hqlQuery);
		assertEquals("from User item inner join fetch item.account inner join fetch item.account.rightList inner join fetch item.messageList", hqlQuery);
		
	//	Test Hibernate loading
	//
		List<User> result = TestHelper.executeQuery(hqlQuery);
		assertNotNull(result);
		assertFalse(result.isEmpty());
		for (User user : result)
		{
			assertTrue(Hibernate.isInitialized(user.getAccount()));
			
			assertNotNull(user.getAccount().getRightList());
			assertTrue(Hibernate.isInitialized(user.getAccount().getRightList()));
			
			assertTrue(Hibernate.isInitialized(user.getMessageList()));
		}
	}
}
