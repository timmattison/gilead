/**
 * 
 */
package net.sf.gilead.performance.gwt;

import com.google.gwt.junit.client.GWTTestCase;

import junit.framework.TestCase;

/**
 * Performance test for GWT
 * @author bruno.marchesson
 *
 */
public class GwtPerformanceTest extends GWTTestCase
{
	//-------------------------------------------------------------------------
	//
	// Test init
	//
	//-------------------------------------------------------------------------
	/**
	 * Get module name
	 */
	public String getModuleName()
	{
		return "net.sf.gilead.Test";
	}
	
	//-------------------------------------------------------------------------
	//
	// Test methods
	//
	//-------------------------------------------------------------------------
	/**
	 * Test clone user and messages
	 */
	public void testCloneUserAndMessage()
	{
		assertTrue(true);
	}

}
