/**
 * 
 */
package net.sf.gilead.performance;

import net.sf.gilead.core.TestHelper;

/**
 * Performance test for stateless mode
 * @author bruno.marchesson
 *
 */
public class StatelessPerformanceTest extends PerformanceTest
{
	/**
	 * Test setup
	 */
	@Override
	protected void setUp() throws Exception
	{
	//	Init bean manager
	//
		_beanManager = TestHelper.initStatelessBeanManager();
	
	//	Call base setup
	//
		super.setUp();
	}
}
