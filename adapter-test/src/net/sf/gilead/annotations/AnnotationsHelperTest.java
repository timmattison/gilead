/**
 * 
 */
package net.sf.gilead.annotations;

import junit.framework.TestCase;
import net.sf.gilead.test.domain.annotated.Message;
import net.sf.gilead.test.domain.annotated.User;
import net.sf.gilead.test.domain.misc.Configuration;

/**
 * Test case for annotation helper.
 * @author bruno.marchesson
 *
 */
public class AnnotationsHelperTest extends TestCase
{
	//-------------------------------------------------------------------------
	//
	// Test methods
	//
	//-------------------------------------------------------------------------
	/**
	 * Test method for {@link net.sf.gilead.annotations.AnnotationsHelper#hasServerOnlyAnnotations(java.lang.Class)}.
	 */
	public final void testHasServerOnlyAnnotations()
	{
	//	Annotation on property
	//
		assertTrue(AnnotationsHelper.hasServerOnlyAnnotations(Message.class));
		
	//	Annotation on getter
	//
		assertTrue(AnnotationsHelper.hasServerOnlyAnnotations(User.class));
		
	//	No ServerOnly annotation
	//
		assertFalse(AnnotationsHelper.hasServerOnlyAnnotations(Configuration.class));
	}

	/**
	 * Test method for {@link net.sf.gilead.annotations.AnnotationsHelper#isServerOnly(java.lang.Class, java.lang.String)}.
	 */
	public final void testIsServerOnly()
	{
	//	Annotation on property
	//
		Message message = new Message();
		assertTrue(AnnotationsHelper.isServerOnly(message, "version"));
		assertFalse(AnnotationsHelper.isServerOnly(message, "author"));
		assertFalse(AnnotationsHelper.isServerOnly(message, "doesNotExist"));
		
	//	Annotation on getter
	//
		User user = new User();
		assertTrue(AnnotationsHelper.isServerOnly(user, "password"));
		assertFalse(AnnotationsHelper.isServerOnly(user, "login"));
		assertFalse(AnnotationsHelper.isServerOnly(user, "doesNotExist"));
	}

	/**
	 * Test method for {@link net.sf.gilead.annotations.AnnotationsHelper#hasReadOnlyAnnotations(java.lang.Class)}.
	 */
	public final void testHasReadOnlyAnnotations()
	{
	//	Annotation on property
	//
		assertTrue(AnnotationsHelper.hasReadOnlyAnnotations(Message.class));
		
	//	Annotation on getter
	//
		assertTrue(AnnotationsHelper.hasReadOnlyAnnotations(User.class));
		
	//	No ServerOnly annotation
	//
		assertFalse(AnnotationsHelper.hasReadOnlyAnnotations(Configuration.class));
	}

	/**
	 * Test method for {@link net.sf.gilead.annotations.AnnotationsHelper#isReadOnly(java.lang.Class, java.lang.String)}.
	 */
	public final void testIsReadOnly()
	{
	//	Annotation on property
	//
		Message message = new Message();
		assertTrue(AnnotationsHelper.isReadOnly(message, "keywords"));
		assertFalse(AnnotationsHelper.isReadOnly(message, "message"));
		assertFalse(AnnotationsHelper.isReadOnly(message, "doesNotExist"));
		
	//	Annotation on getter
	//
		User user = new User();
		assertTrue(AnnotationsHelper.isReadOnly(user, "login"));
		assertFalse(AnnotationsHelper.isReadOnly(user, "password"));
		assertFalse(AnnotationsHelper.isReadOnly(user, "doesNotExist"));
		
	//	Test on private getter
	//
//		assertTrue(AnnotationsHelper.isReadOnly(Address.class, "zipCode"));
	}
	
	/**
	 * Test access manager handling
	 */
	public final void testAccessManager()
	{
		TestAccessManager accessManager = (TestAccessManager) AnnotationsHelper.getAccessManager(TestAccessManager.class);
		
	//	Test access with role user
	//
		accessManager.setRole(TestAccessManager.Role.user);
		Message message = new Message();
		assertTrue(AnnotationsHelper.isReadOnly(message, "date"));
		
	//	Test with admin user
	//
		accessManager.setRole(TestAccessManager.Role.admin);
		assertFalse(AnnotationsHelper.isReadOnly(message, "date"));
	}

}
