/**
 * 
 */
package net.sf.gilead.core.serialization;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Test serialization manager behavior
 * @author bruno.marchesson
 *
 */
public class SerializationManagerTest extends TestCase
{
	//----
	// Log channel
	//----
	/**
	 * Log channel
	 */
	private static Log _log = LogFactory.getLog(SerializationManagerTest.class);
	

	
	//-------------------------------------------------------------------------
	//
	// Public test methods
	//
	//-------------------------------------------------------------------------
	/**
	 * Test bytes serialization
	 */
	public void testBytesProxySerialization()
	{
		IProxySerialization proxySerialization = new BytesProxySerialization();
		testIntegerToBytesConversion(proxySerialization);
		testLongToBytesConversion(proxySerialization);
	}
	
	/**
	 * Test bytes serialization
	 */
	public void testXStreamProxySerialization()
	{
		IProxySerialization proxySerialization = new XStreamProxySerialization();
		testIntegerToBytesConversion(proxySerialization);
		testLongToBytesConversion(proxySerialization);
	}
	
	//-------------------------------------------------------------------------
	//
	// Internal Test methods
	//
	//-------------------------------------------------------------------------
	/**
	 * Test Integer convertor
	 */
	protected void testIntegerToBytesConversion(IProxySerialization proxySerialization)
	{
	//	Integer conversion
	//
		Integer value = new Integer(1);
		long start = System.currentTimeMillis();
		Object serialized = proxySerialization.serialize(value);
		long serialization = System.currentTimeMillis();
		
		assertNotNull(serialized);
		assertEquals(value, proxySerialization.unserialize(serialized));
		long end = System.currentTimeMillis();
		
		_log.info("Integer serialization took [" + (serialization-start) + ", " + (end-serialization) + "] ms");
		
	//	int conversion
	//
		int intValue = 1;
		start = System.currentTimeMillis();
		serialized = proxySerialization.serialize(intValue);
		serialization = System.currentTimeMillis();
		
		assertNotNull(serialized);
		assertEquals(intValue, proxySerialization.unserialize(serialized));
		end = System.currentTimeMillis();
		
		_log.info("int serialization took [" + (serialization-start) + ", " + (end-serialization) + "] ms");
	}
	
	/**
	 * Test Long convertor
	 */
	protected void testLongToBytesConversion(IProxySerialization proxySerialization)
	{
	//	Long conversion
	//
		Long value = new Long(1);
		long start = System.currentTimeMillis();
		Object serialized = proxySerialization.serialize(value);
		long serialization = System.currentTimeMillis();
		
		assertNotNull(serialized);
		assertEquals(value, proxySerialization.unserialize(serialized));
		long end = System.currentTimeMillis();
		
		_log.info("Long serialization took [" + (serialization-start) + ", " + (end-serialization) + "] ms");
		
	//	long conversion
	//
		long longValue = 1;
		start = System.currentTimeMillis();
		serialized = proxySerialization.serialize(longValue);
		serialization = System.currentTimeMillis();
		
		assertNotNull(serialized);
		assertEquals(longValue, proxySerialization.unserialize(serialized));
		end = System.currentTimeMillis();
		
		_log.info("long serialization took [" + (serialization-start) + ", " + (end-serialization) + "] ms");
	}
}
