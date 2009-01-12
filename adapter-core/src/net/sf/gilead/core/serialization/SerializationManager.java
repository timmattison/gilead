/**
 * 
 */
package net.sf.gilead.core.serialization;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;

import net.sf.gilead.exception.ConvertorException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.thoughtworks.xstream.XStream;

/**
 * Serialization manager singleton.
 * It serializes Serializable instances to simple byte array and
 * deserializes them when back.
 * (needed for proxy informations, since GWT does not like
 * Serializable type in Map<String, Serializable>)
 * @author bruno.marchesson
 *
 */
public class SerializationManager
{	
	//----
	// Singleton
	//----
	/**
	 * The unique instance of manager
	 */
	private static SerializationManager _instance = null;

	/**
	 * @return the unique instance of the manager
	 */
	public static SerializationManager getInstance()
	{
		if (_instance == null)
		{
			_instance = new SerializationManager();
		}
		return _instance;
	}
	
	//----
	// Attributes
	//----
	/**
	 * Log channel.
	 */
	private static Log _log = LogFactory.getLog(SerializationManager.class);
	
	/**
	 * The XStream facade
	 */
	private XStream _xstream;
	
	//-------------------------------------------------------------------------
	//
	// Constructor
	//
	//-------------------------------------------------------------------------
	/**
	 * Private constructor.
	 */
	protected SerializationManager()
	{
		_xstream = new XStream();
	}
	
	//-------------------------------------------------------------------------
	//
	// Public interface
	//
	//-------------------------------------------------------------------------
	/**
	 * Convert Serializable to bytes.
	 */
	public byte[] serializeToBytes(Serializable serializable)
	{
		if (_log.isDebugEnabled())
		{
			_log.debug("Serialization of " + serializable);
		}
	//	Precondition checking
	//
		if (serializable == null)
		{
			return null;
		}
		
	//	Serialize using Java mechanism
	//
		try
		{
			ByteArrayOutputStream out = new ByteArrayOutputStream();
		    ObjectOutputStream oos = new ObjectOutputStream(out);
		    oos.writeObject(serializable);
		    oos.close();
		    
		    return out.toByteArray();
		}
		catch(IOException ex)
		{
			throw new ConvertorException("Error converting Serializable", ex);
		}
	}
	
	/**
	 * Convert Serializable to bytes.
	 */
	public String serializeToString(Serializable serializable)
	{
		if (_log.isDebugEnabled())
		{
			_log.debug("Serialization of " + serializable);
		}
	//	Precondition checking
	//
		if (serializable == null)
		{
			return null;
		}
		
	//	Serialize to bytes and encapsulate into string
	//
		/*try
		{
			return new BASE64Encoder().encode(serializeToBytes(serializable));
		}
		catch (Exception e)
		{
			throw new ConvertorException("Error converting Serializable", e);
		}*/
		return _xstream.toXML(serializable);
	}
	
	/**
	 * Regenerate Serializable from bytes.
	 */
	public Serializable unserializeFromBytes(byte[] bytes)
	{
		if (_log.isDebugEnabled())
		{
			_log.debug("Unserialization of " + Arrays.toString(bytes));
		}
		
	//	Precondition checking
	//
		if ((bytes == null) ||
			(bytes.length == 0))
		{
			return null;
		}
		
	//	Convert back to Serializable
	//
		try
		{
			ByteArrayInputStream in = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(in);
			return (Serializable) ois.readObject();
		}
		catch (Exception e)
		{
			throw new ConvertorException("Error converting Serializable", e);
		}

	}
	
	/**
	 * Regenerate Serializable from String.
	 */
	public Serializable unserializeFromString(String string)
	{
		if (_log.isDebugEnabled())
		{
			_log.debug("Unserialization of " + string);
		}
		
	//	Precondition checking
	//
		if ((string == null) ||
			(string.length() == 0))
		{
			return null;
		}
		
	//	Convert back to bytes and Serializable
	//
		/*try
		{
			return unserializeFromBytes(new BASE64Decoder().decodeBuffer(string));
		}
		catch (Exception e)
		{
			throw new ConvertorException("Error converting Serializable", e);
		}*/
		return (Serializable) _xstream.fromXML(string);
	}
}
