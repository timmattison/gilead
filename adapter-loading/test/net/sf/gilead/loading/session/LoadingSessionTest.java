package net.sf.gilead.loading.session;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Test;

import net.sf.gilead.loading.TestHelper;
import net.sf.gilead.loading.domain.interfaces.ISimpleUser;
import net.sf.gilead.loading.domain.interfaces.IUserAndAccount;
import net.sf.gilead.loading.proxy.wrapper.LoadingWrapper;

/**
 * Loading session test 
 * @author bruno.marchesson
 *
 */
public class LoadingSessionTest
{
	//-------------------------------------------------------------------------
	//
	// Test methods
	//
	//-------------------------------------------------------------------------
	@Test
	public void testLoadAndSaveSimpleInterface()
	{
	//	Create loading session factory
	//
		SessionFactory sessionFactory = new LoadingSessionFactory(TestHelper.createSessionFactory());
		
		
	//	Load simple user
	//
		ISimpleUser user = (ISimpleUser) TestHelper.load(sessionFactory, ISimpleUser.class, 1);
		Assert.assertNotNull(user);
		Assert.assertTrue(user instanceof LoadingWrapper);
		int oldVersion = user.getVersion();
		
	//	Change and save user
	//
		user.setFirstName("test2");
		TestHelper.save(sessionFactory, user);
		
	//	Save check
	//
		int newVersion = user.getVersion();
		Assert.assertTrue(oldVersion < newVersion);
	}
	
	@Test
	public void testLoadAndSaveUserAndAccount()
	{
	//	Create loading session factory
	//
		SessionFactory sessionFactory = new LoadingSessionFactory(TestHelper.createSessionFactory());
		
		
	//	Load simple user
	//
		IUserAndAccount user = (IUserAndAccount) TestHelper.load(sessionFactory, IUserAndAccount.class, 1);
		Assert.assertNotNull(user);
		Assert.assertTrue(user instanceof LoadingWrapper);
		int oldVersion = user.getVersion();
		
	//	Change and save user
	//
		user.setFirstName("test3");
		user.getAccount().setDomain("testDomain");
		TestHelper.save(sessionFactory, user);
		
	//	Save check
	//
		int newVersion = user.getVersion();
		Assert.assertTrue(oldVersion < newVersion);
	}
}
