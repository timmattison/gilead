/**
 * 
 */
package net.sf.gilead.core.serialization;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

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
	public void testByteStringProxySerialization()
	{
		IProxySerialization proxySerialization = new ByteStringProxySerialization();
		testIntegerConversion(proxySerialization);
		testLongConversion(proxySerialization);
		testMapConversion(proxySerialization);
	}
	
	/**
	 * Test bytes serialization
	 */
	public void testXStreamProxySerialization()
	{
		IProxySerialization proxySerialization = new XStreamProxySerialization();
		testIntegerConversion(proxySerialization);
		testLongConversion(proxySerialization);
		testMapConversion(proxySerialization);
	}
	
	//-------------------------------------------------------------------------
	//
	// Internal Test methods
	//
	//-------------------------------------------------------------------------
	/**
	 * Test Integer convertor
	 */
	protected void testIntegerConversion(IProxySerialization proxySerialization)
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
	protected void testLongConversion(IProxySerialization proxySerialization)
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
	
	/**
	 * Test Map conversion
	 */
	protected void testMapConversion(IProxySerialization proxySerialization)
	{
	//	Map creation
	//
		HashMap<String, Serializable> map = new HashMap<String, Serializable>();
		map.put("id", 1);
		map.put("initialized", true);
		ArrayList<Integer> idList = new ArrayList<Integer>();
		idList.add(2);
		idList.add(3);
		idList.add(4);
		map.put("idList", idList);
		
	//	Test conversion
	//
		long start = System.currentTimeMillis();
		Object serialized = proxySerialization.serialize(map);
		long serialization = System.currentTimeMillis();
		
		assertNotNull(serialized);
		HashMap<String, Serializable> unserialized = (HashMap<String, Serializable>)
													 proxySerialization.unserialize(serialized);
		long end = System.currentTimeMillis();
		
		_log.info("Map serialization took [" + (serialization-start) + ", " + (end-serialization) + "] ms");
		
	//	Map checking
	//
		assertNotNull(unserialized);
		assertEquals(map.size(), unserialized.size());
		assertNotNull(unserialized.get("idList"));
		assertEquals(3, ((ArrayList<Integer>)unserialized.get("idList")).size());
	}
}
