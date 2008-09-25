/**
 * 
 */
package net.sf.gilead.annotations;

import junit.framework.TestCase;
import net.sf.gilead.test.domain.Configuration;
import net.sf.gilead.test.domain.annotated.Message;
import net.sf.gilead.test.domain.annotated.User;

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
		assertTrue(AnnotationsHelper.isServerOnly(Message.class, "version"));
		assertFalse(AnnotationsHelper.isServerOnly(Message.class, "author"));
		assertFalse(AnnotationsHelper.isServerOnly(Message.class, "doesNotExist"));
		
	//	Annotation on getter
	//
		assertTrue(AnnotationsHelper.isServerOnly(User.class, "password"));
		assertFalse(AnnotationsHelper.isServerOnly(User.class, "login"));
		assertFalse(AnnotationsHelper.isServerOnly(User.class, "doesNotExist"));
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
		assertTrue(AnnotationsHelper.isReadOnly(Message.class, "keywords"));
		assertFalse(AnnotationsHelper.isReadOnly(Message.class, "message"));
		assertFalse(AnnotationsHelper.isReadOnly(Message.class, "doesNotExist"));
		
	//	Annotation on getter
	//
		assertTrue(AnnotationsHelper.isReadOnly(User.class, "login"));
		assertFalse(AnnotationsHelper.isReadOnly(User.class, "password"));
		assertFalse(AnnotationsHelper.isReadOnly(User.class, "doesNotExist"));

	}

}
