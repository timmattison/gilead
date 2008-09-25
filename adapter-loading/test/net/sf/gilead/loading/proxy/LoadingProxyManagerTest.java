/**
 * 
 */
package net.sf.gilead.loading.proxy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import net.sf.gilead.loading.domain.User;
import net.sf.gilead.loading.domain.interfaces.ISimpleUser;
import net.sf.gilead.loading.domain.interfaces.IUserAndAccount;
import net.sf.gilead.proxy.xml.AdditionalCode;

import org.junit.Test;

/**
 * Test loading code generator.
 * @author bruno.marchesson
 *
 */
public class LoadingProxyManagerTest
{
	//-------------------------------------------------------------------------
	//
	// Test methods
	//
	//-------------------------------------------------------------------------
	/**
	 * Test method for {@link net.sf.gilead.loading.proxy.LoadingProxyManager#generateCodeFor(java.lang.Class)}.
	 */
	@Test
	public void testSimpleUserGenerateAdditionalCode()
	{
	//	Generate additional code
	//
		AdditionalCode additionalCode = LoadingProxyManager.getInstance().generateCodeFor(ISimpleUser.class);
		
	//	Code verification
	//
		assertNotNull(additionalCode);
		assertNotNull(additionalCode.getConstructors());
		assertEquals(ISimpleUser.class.getCanonicalName(), 
					 additionalCode.getImplementedInterface());
		assertTrue(additionalCode.getMethods().size() == 
				   ISimpleUser.class.getMethods().length);
		
	//	Test code generation
	//
		Class<?> generatedProxy = LoadingProxyManager.getInstance().getWrapper(ISimpleUser.class);
		assertNotNull(generatedProxy);
		assertTrue(ISimpleUser.class.isAssignableFrom(generatedProxy));
		assertFalse(User.class.isAssignableFrom(generatedProxy));

	}

	@Test
	public void testUserAndAccountGenerateAdditionalCode()
	{
	//	Generate additional code
	//
		AdditionalCode additionalCode = LoadingProxyManager.getInstance().generateCodeFor(IUserAndAccount.class);
		
	//	Code verification
	//
		assertNotNull(additionalCode);
		assertEquals(IUserAndAccount.class.getCanonicalName(), 
					 additionalCode.getImplementedInterface());
		assertTrue(additionalCode.getMethods().size() == 
				   IUserAndAccount.class.getMethods().length);
		
	//	Test code generation
	//
		Class<?> generatedProxy = LoadingProxyManager.getInstance().getWrapper(IUserAndAccount.class);
		assertNotNull(generatedProxy);
		assertTrue(IUserAndAccount.class.isAssignableFrom(generatedProxy));
		assertFalse(User.class.isAssignableFrom(generatedProxy));		
	}
}
