/**
 * 
 */
package net.sf.gilead.core;

import java.util.Date;

import junit.framework.TestCase;
import net.sf.gilead.test.DAOFactory;
import net.sf.gilead.test.dao.IMessageDAO;
import net.sf.gilead.test.dao.IUserDAO;
import net.sf.gilead.test.domain.annotated.Message;
import net.sf.gilead.test.domain.annotated.User;

/**
 * Test case for ReadOnly and ServerOnly annotations 
 * @author bruno.marchesson
 *
 */
public class AnnotationsTest extends TestCase
{	
	//----
	// Attributes
	//----
	/**
	 * Hibernate lazy manager
	 */
	protected PersistentBeanManager _beanManager;
	
	//-------------------------------------------------------------------------
	//
	// Test initialisation
	//
	//-------------------------------------------------------------------------
	/**
	 * Test init
	 */
	@Override
	protected void setUp() throws Exception
	{
		_beanManager = TestHelper.initJava5AnnotatedBeanManager();
		
	//	Init db if needed
	//
		if (TestHelper.isInitialized() == false)
		{
			TestHelper.initializeDB();
		}
	}
	
	//--------------------------------------------------------------------------
	//
	// Test methods
	//
	//---------------------------------------------------------------------------
	/**
	 * Test clone and merge of ReadOnly attributes
	 */
	public void testReadOnlyCloneAndMergeUser()
	{
	//	Get UserDAO
	//
		IUserDAO userDAO = DAOFactory.getUserDAO();
		assertNotNull(userDAO);
		
	//	Load user
	//
		User user = (User) userDAO.loadUser(userDAO.searchUserAndMessagesByLogin("junit").getId());
		assertNotNull(user);
		assertNotNull(user.getPassword());
		
	//	Clone user
	//
		User cloneUser = (User) _beanManager.clone(user);
		
	//	Test cloned user
	//
		assertNotNull(cloneUser);
		assertNull(cloneUser.getPassword());
		
	//	Merge user
	//
		User mergeUser = (User) _beanManager.merge(cloneUser);
		
	//	Test merged user
	//
		assertNotNull(mergeUser);
		assertNotNull(mergeUser.getPassword());
		assertEquals(user.getPassword(), mergeUser.getPassword());
	}
	
	/**
	 * Test clone and merge of ReadOnly attributes
	 */
	public void testReadOnlyAndServerOnlyCloneAndMergeMessage()
	{
	//	Get MessageDAO
	//
		IMessageDAO messageDAO = DAOFactory.getMessageDAO();
		assertNotNull(messageDAO);
		
	//	Load message
	//
		Message message = (Message) messageDAO.loadDetailedMessage((Integer)TestHelper.getExistingMessageId());
		assertNotNull(message);
		assertNotNull(message.getVersion()); // serverOnly
		assertTrue(message.countKeywords() > 0); // readOnly
		
	//	Clone message
	//
		Message cloneMessage = (Message) _beanManager.clone(message);
		
	//	Test cloned Message
	//
		assertNotNull(cloneMessage);
		assertNull(cloneMessage.getVersion()); // serverOnly
		assertTrue(cloneMessage.countKeywords() > 0); // readOnly
		
		cloneMessage.addKeyword("readOnly", 8);
		
		
	//	Merge Message
	//
		Message mergeMessage = (Message) _beanManager.merge(cloneMessage);
		
	//	Test merged Message
	//
		assertNotNull(mergeMessage);
		assertNotNull(mergeMessage.getVersion()); // serverOnly
		assertTrue(mergeMessage.countKeywords() > 0); // readOnly
		assertEquals(message.countKeywords(), mergeMessage.countKeywords());
	}
	
	/**
	 * Test clone and merge of ReadOnly attributes
	 */
	public void testAnnotationsOnTransientMessage()
	{
	//	Create message
	//
		Message message = new Message();
		message.setMessage("test");
		message.setDate(new Date());
		message.setVersion(1);
		message.addKeyword("test", 4);
		
	//	Clone message
	//
		Message cloneMessage = (Message) _beanManager.clone(message);
		
	//	Test cloned message
	//
		assertNotNull(cloneMessage);
		assertNull(cloneMessage.getVersion()); // serverOnly
		assertTrue(cloneMessage.countKeywords() > 0); // readOnly
		
		cloneMessage.addKeyword("readOnly", 8);		
		
	//	Merge Message
	//
		Message mergeMessage = (Message) _beanManager.merge(cloneMessage);
		
	//	Test merged Message
	//
		assertNotNull(mergeMessage);
		assertNull(mergeMessage.getVersion()); // serverOnly
		assertTrue(mergeMessage.countKeywords() == 0); // readOnly
	}
}
