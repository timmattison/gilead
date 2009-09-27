/**
 * 
 */
package net.sf.gilead.core.serialization;

import java.io.Serializable;

import net.sf.gilead.pojo.gwt.IRequestParameter;
import net.sf.gilead.pojo.gwt.SerializedParameter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gwt.user.client.rpc.SerializationException;

/**
 * GWT compatible map serialization.
 * Each basic type is encapsulated as IRequestParameter.
 * Non basic type is serialised as string and send as this
 * @author bruno.marchesson
 *
 */
public class GwtProxySerialization extends GwtSerializer implements IProxySerialization
{
	//----
	// Attributes
	//----
	/**
	 * Logger channel
	 */
	private static final Logger _log = LoggerFactory.getLogger(GwtProxySerialization.class);
	
	/**
	 * String serializer
	 */
	protected IProxySerialization _stringSerializer;
	
	//----
	// Properties
	//----
	/**
	 * @return the String Serializer
	 */
	public IProxySerialization getStringSerializer() 
	{
		return _stringSerializer;
	}

	/**
	 * @param serializer the string serializer to set
	 */
	public void setStringSerializer(IProxySerialization serializer)
	{
		_stringSerializer = serializer;
	}

	//----
	// Constructor
	//----
	/**
	 * Constructor
	 */
	public GwtProxySerialization()
	{
		_stringSerializer = new JBossProxySerialization();
	}
	
	//-------------------------------------------------------------------------
	//
	// IProxySerialization implementation
	//
	//-------------------------------------------------------------------------
	/* (non-Javadoc)
	 * @see net.sf.gilead.core.serialization.IProxySerialization#serialize(java.io.Serializable)
	 */
	@SuppressWarnings("unchecked")
	public Object serialize(Serializable serializable)
	{
	//	Precondition checking
	//
		if (serializable == null)
		{
			return null;
		}
	
	//	Convert to GWT
	//
		try
		{
			return convertToGwt(serializable);
		}
		catch(SerializationException ex)
		{
			// should not happen
			throw new RuntimeException(ex);
		}
	}

	/* (non-Javadoc)
	 * @see net.sf.gilead.core.serialization.IProxySerialization#unserialize(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public Serializable unserialize(Object serialized)
	{
	//	Precondition checking
	//
		if (serialized == null)
		{
			return null;
		}
	
	//	Convert from GWT
	//
		try
		{
			return convertFromGwt((IRequestParameter) serialized);
		}
		catch(SerializationException ex)
		{
			// should not happen
			throw new RuntimeException(ex);
		}
	}

	//------------------------------------------------------------------------
	//
	// Overridden methods
	//
	//------------------------------------------------------------------------
	/*
	 * (non-Javadoc)
	 * @see net.sf.gilead.core.serialization.GwtSerializer#convertBasicToGwt(java.io.Serializable)
	 */
	protected IRequestParameter convertBasicToGwt(Serializable object)
								throws SerializationException
	{
		try
		{
			return super.convertBasicToGwt(object);
		}
		catch(SerializationException ex)
		{
			if (_log.isDebugEnabled())
			{
				_log.debug(object.getClass() + " not serializable => convert to string");
			}
			return new SerializedParameter((String)_stringSerializer.serialize(object));
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.gilead.core.serialization.GwtSerializer#convertBasicFromGwt(net.sf.gilead.gwt.client.parameters.IRequestParameter)
	 */
	protected Serializable convertBasicFromGwt(IRequestParameter parameter)
	{
		if (parameter instanceof SerializedParameter)
		{
			return _stringSerializer.unserialize(parameter.getValue());
		}
		else
		{
			return super.convertBasicFromGwt(parameter);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.gilead.core.serialization.GwtSerializer#convertListToGwt(java.io.Serializable)
	 */
	protected IRequestParameter convertListToGwt(Serializable object)
								throws SerializationException
	{
		try
		{
			return super.convertListToGwt(object);
		}
		catch(SerializationException ex)
		{
			if (_log.isDebugEnabled())
			{
				_log.debug(object.getClass() + " not serializable => convert to string");
			}
			return new SerializedParameter((String)_stringSerializer.serialize(object));
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.gilead.core.serialization.GwtSerializer#convertListFromGwt(net.sf.gilead.gwt.client.parameters.IRequestParameter)
	 */
	protected Serializable convertListFromGwt(IRequestParameter parameter)
											throws SerializationException
	{
		if (parameter instanceof SerializedParameter)
		{
			return _stringSerializer.unserialize(parameter.getValue());
		}
		else
		{
			return super.convertListFromGwt(parameter);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.gilead.core.serialization.GwtSerializer#convertSetToGwt(java.io.Serializable)
	 */
	protected IRequestParameter convertSetToGwt(Serializable object)
								throws SerializationException
	{
		try
		{
			return super.convertSetToGwt(object);
		}
		catch(SerializationException ex)
		{
			if (_log.isDebugEnabled())
			{
				_log.debug(object.getClass() + " not serializable => convert to string");
			}
			return new SerializedParameter((String)_stringSerializer.serialize(object));
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.gilead.core.serialization.GwtSerializer#convertSetFromGwt(net.sf.gilead.gwt.client.parameters.IRequestParameter)
	 */
	protected Serializable convertSetFromGwt(IRequestParameter parameter)
											throws SerializationException
	{
		if (parameter instanceof SerializedParameter)
		{
			return _stringSerializer.unserialize(parameter.getValue());
		}
		else
		{
			return super.convertSetFromGwt(parameter);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.gilead.core.serialization.GwtSerializer#convertMapToGwt(java.io.Serializable)
	 */
	protected IRequestParameter convertMapToGwt(Serializable object)
								throws SerializationException							
	{
		try
		{
			return super.convertMapToGwt(object);
		}
		catch(SerializationException ex)
		{
			if (_log.isDebugEnabled())
			{
				_log.debug(object.getClass() + " not serializable => convert to string");
			}
			return new SerializedParameter((String)_stringSerializer.serialize(object));
		}
	}
	
	@Override
	protected Serializable convertMapFromGwt(IRequestParameter parameter)
											throws SerializationException
	{
		if (parameter instanceof SerializedParameter)
		{
			return _stringSerializer.unserialize(parameter.getValue());
		}
		else
		{
			return super.convertMapFromGwt(parameter);
		}
	}
}
