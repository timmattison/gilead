/**
 * 
 */
package net.sf.gilead.core.serialization;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.javabean.JavaBeanConverter;

/**
 * XStream Serialization strategy.
 * It serializes Serializable instances to String using XStream and
 * deserializes them when back.
 * (needed for proxy informations, since GWT does not like
 * Serializable type in Map<String, Serializable>)
 * @author bruno.marchesson
 *
 */
public class XStreamProxySerialization implements IProxySerialization
{	
	//----
	// Attributes
	//----
	/**
	 * Log channel.
	 */
	private static Log _log = LogFactory.getLog(XStreamProxySerialization.class);
	
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
	 * Constructor.
	 */
	public XStreamProxySerialization()
	{
		_xstream = new XStream();
		_xstream.registerConverter(new SerializableIdConverter(_xstream));
		// _xstream.registerConverter(new JavaBeanConverter(_xstream.getMapper()));
	}
	
	//-------------------------------------------------------------------------
	//
	// Public interface
	//
	//-------------------------------------------------------------------------
	/**
	 * Convert Serializable to bytes.
	 */
	public String serialize(Serializable serializable)
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
		return _xstream.toXML(serializable);
	}
	
	/**
	 * Regenerate Serializable from String.
	 */
	public Serializable unserialize(Object object)
	{
		String string = (String) object;
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
		return (Serializable) _xstream.fromXML(string);
	}
}
