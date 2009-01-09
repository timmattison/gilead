/**
 * 
 */
package net.sf.gilead.core.serialization;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.gilead.core.serialization.SerializationManager;
import junit.framework.TestCase;

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
	// Test methods
	//
	//-------------------------------------------------------------------------
	/**
	 * Test Integer convertor
	 */
	public void testIntegerToBytesConversion()
	{
	//	Integer conversion
	//
		Integer value = new Integer(1);
		long start = System.currentTimeMillis();
		byte[] serialized = SerializationManager.getInstance().serializeToBytes(value);
		long serialization = System.currentTimeMillis();
		
		assertNotNull(serialized);
		assertEquals(value, SerializationManager.getInstance().unserializeFromBytes(serialized));
		long end = System.currentTimeMillis();
		
		_log.info("Integer->bytes serialization is " + serialized.length + " bytes long and took [" + 
				  (serialization-start) + ", " + (end-serialization) + "] ms");
		
	//	int conversion
	//
		int intValue = 1;
		start = System.currentTimeMillis();
		serialized = SerializationManager.getInstance().serializeToBytes(intValue);
		serialization = System.currentTimeMillis();
		
		assertNotNull(serialized);
		assertEquals(intValue, SerializationManager.getInstance().unserializeFromBytes(serialized));
		end = System.currentTimeMillis();
		
		_log.info("int->bytes serialization is " + serialized.length + " bytes long and took [" + 
				  (serialization-start) + ", " + (end-serialization) + "] ms");
	}
	
	/**
	 * Test Long convertor
	 */
	public void testLongToBytesConversion()
	{
	//	Integer conversion
	//
		Long value = new Long(1);
		long start = System.currentTimeMillis();
		byte[] serialized = SerializationManager.getInstance().serializeToBytes(value);
		long serialization = System.currentTimeMillis();
		
		assertNotNull(serialized);
		assertEquals(value, SerializationManager.getInstance().unserializeFromBytes(serialized));
		long end = System.currentTimeMillis();
		
		_log.info("Integer->bytes serialization is " + serialized.length + " bytes long and took [" + 
				  (serialization-start) + ", " + (end-serialization) + "] ms");		
	//	int conversion
	//
		long longValue = 1;
		start = System.currentTimeMillis();
		serialized = SerializationManager.getInstance().serializeToBytes(longValue);
		serialization = System.currentTimeMillis();
		
		assertNotNull(serialized);
		assertEquals(longValue, SerializationManager.getInstance().unserializeFromBytes(serialized));
		end = System.currentTimeMillis();
		
		_log.info("long->bytes serialization is " + serialized.length + " bytes long and took [" + 
				  (serialization-start) + ", " + (end-serialization) + "] ms");
	}
	/**
	 * Test Integer convertor
	 */
	public void testIntegerToStringConversion()
	{
	//	Integer conversion
	//
		Integer value = new Integer(1);
		long start = System.currentTimeMillis();
		String serialized = SerializationManager.getInstance().serializeToString(value);
		long serialization = System.currentTimeMillis();
		
		assertNotNull(serialized);
		assertEquals(value, SerializationManager.getInstance().unserializeFromString(serialized));
		long end = System.currentTimeMillis();
		
		_log.info("Integer->string serialization is " + serialized.getBytes().length + " bytes long and took [" + 
				  (serialization-start) + ", " + (end-serialization) + "] ms");
		
	//	int conversion
	//
		int intValue = 1;
		start = System.currentTimeMillis();
		serialized = SerializationManager.getInstance().serializeToString(intValue);
		serialization = System.currentTimeMillis();
		
		assertNotNull(serialized);
		assertEquals(intValue, SerializationManager.getInstance().unserializeFromString(serialized));
		end = System.currentTimeMillis();
		
		_log.info("int->bytes serialization is " + serialized.getBytes().length + " bytes long and took [" + 
				  (serialization-start) + ", " + (end-serialization) + "] ms");

	}
	
	/**
	 * Test Long convertor
	 */
	public void testLongToStringConversion()
	{
	//	Long conversion
	//
		Long value = new Long(1);
		long start = System.currentTimeMillis();
		String serialized = SerializationManager.getInstance().serializeToString(value);
		long serialization = System.currentTimeMillis();
		
		assertNotNull(serialized);
		assertEquals(value, SerializationManager.getInstance().unserializeFromString(serialized));
		long end = System.currentTimeMillis();
		
		_log.info("Long->string serialization is " + serialized.getBytes().length + " bytes long and took [" + 
				  (serialization-start) + ", " + (end-serialization) + "] ms");
	//	int conversion
	//
		long longValue = 1;
		start = System.currentTimeMillis();
		serialized = SerializationManager.getInstance().serializeToString(longValue);
		serialization = System.currentTimeMillis();
		
		assertNotNull(serialized);
		assertEquals(longValue, SerializationManager.getInstance().unserializeFromString(serialized));
		end = System.currentTimeMillis();
		
		_log.info("long->bytes serialization is " + serialized.getBytes().length + " bytes long and took [" + 
				  (serialization-start) + ", " + (end-serialization) + "] ms");
	}
}
