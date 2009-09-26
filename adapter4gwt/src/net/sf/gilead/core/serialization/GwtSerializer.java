/**
 * 
 */
package net.sf.gilead.core.serialization;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import net.sf.gilead.pojo.gwt.IRequestParameter;
import net.sf.gilead.pojo.gwt.basic.BooleanParameter;
import net.sf.gilead.pojo.gwt.basic.ByteParameter;
import net.sf.gilead.pojo.gwt.basic.CharacterParameter;
import net.sf.gilead.pojo.gwt.basic.DateParameter;
import net.sf.gilead.pojo.gwt.basic.DoubleParameter;
import net.sf.gilead.pojo.gwt.basic.FloatParameter;
import net.sf.gilead.pojo.gwt.basic.IntegerParameter;
import net.sf.gilead.pojo.gwt.basic.LongParameter;
import net.sf.gilead.pojo.gwt.basic.ShortParameter;
import net.sf.gilead.pojo.gwt.basic.StringParameter;
import net.sf.gilead.pojo.gwt.collection.ListParameter;
import net.sf.gilead.pojo.gwt.collection.MapParameter;
import net.sf.gilead.pojo.gwt.collection.SetParameter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gwt.user.client.rpc.SerializationException;

/**
 * GWT compatible serialization.
 * Since Object class is not allowed, we replace it with a marker interface
 * and encapsulates each basic supported types and collections in an implementation
 * of the interface.  
 * @author bruno.marchesson
 *
 */
public class GwtSerializer
{
	//----
	// Attributes
	//----
	/**
	 * Log channel
	 */
	private static final Log _log = LogFactory.getLog(GwtSerializer.class);
	
	//----
	// Constructor
	//----
	/**
	 * Constructor
	 */
	public GwtSerializer()
	{
	}
	
	//-------------------------------------------------------------------------
	//
	// Public interface
	//
	//-------------------------------------------------------------------------
	/**
	 * Converts a serializable type to GWT supported encapsulation.
	 * @throws SerializationException if the serializable argument is not supported by GWT JRE. 
	 */
	public IRequestParameter convertToGwt(Serializable serializable) 
									 	  throws SerializationException
	{
	//	Precondition checking
	//
		if (serializable == null)
		{
			return null;
		}
		
		if (_log.isDebugEnabled())
		{
			_log.debug("Converting " + serializable + " to GWT supported type");
		}
	
	//	Type checking
	//
		if (serializable instanceof List)
		{
			return convertListToGwt(serializable);
		}
		else if (serializable instanceof Set)
		{
			return convertSetToGwt(serializable);
		}
		else if (serializable instanceof Map)
		{
			return convertMapToGwt(serializable);
		}
		else
		{
		//	Basic type ?
		//
			return convertBasicToGwt(serializable);
		}
	}

	/**
	 * 
	 * @param serialized
	 * @return
	 */
	public Serializable convertFromGwt(IRequestParameter parameter) 
						throws SerializationException
	{
		if (_log.isDebugEnabled())
		{
			_log.debug("Converting " + parameter + " from GWT back to serializable type");
		}
	//	Precondition checking
	//
		if (parameter == null)
		{
			return null;
		}
		
	//	Type checking
	//
		if (parameter instanceof ListParameter)
		{
			return convertListFromGwt(parameter);
		}
		else if (parameter instanceof SetParameter)
		{
			return convertSetFromGwt(parameter);
		}
		else if (parameter instanceof MapParameter)
		{
			return convertMapFromGwt(parameter);
		}
		else
		{
		//	Basic type ?
		//
			return convertBasicFromGwt(parameter);
		}
	}

	//------------------------------------------------------------------------
	//
	// Internal methods
	//
	//------------------------------------------------------------------------
	/**
	 * Convert the argument object to GWT serialzable IRequestParameter
	 */
	protected IRequestParameter convertBasicToGwt(Serializable object) 
								throws SerializationException
	{
	//	Precondition checking
	//
		if (object == null)
		{
			return null;
		}
		 
	//	Check basic parameters (most current first) 
	//
		if (object instanceof Integer)
		{
			return new IntegerParameter((Integer) object);
		}
		if (object instanceof String)
		{
			return new StringParameter((String)object);
		}
		if (object instanceof Long)
		{
			return new LongParameter((Long)object);
		}
		if (object instanceof Boolean)
		{
			return new BooleanParameter((Boolean) object);
		}
		if (object instanceof Date)
		{
			return new DateParameter((Date) object);
		}
		if (object instanceof Short)
		{
			return new ShortParameter((Short) object);
		}
		if (object instanceof Character)
		{
			return new CharacterParameter((Character) object);
		}
		if (object instanceof Double)
		{
			return new DoubleParameter((Double) object);
		}
		if (object instanceof Float)
		{
			return new FloatParameter((Float) object);
		}
		if (object instanceof Byte)
		{
			return new ByteParameter((Byte) object);
		}
		
	//	else : unsupported type
	//
		throw new SerializationException("Unsupported type : " + object.getClass());
	}
	
	/**
	 * Convert IRequestParameter back to Serializable
	 * @param parameter
	 * @return
	 */
	protected Serializable convertBasicFromGwt(IRequestParameter parameter)
	{
	//	Just use the underlying value
	//
		return (Serializable) parameter.getValue();
	}
	
	/**
	 * Convert the argument list to GWT serializable IRequestParameter
	 */
	@SuppressWarnings("unchecked")
	protected IRequestParameter convertListToGwt(Serializable object) 
								throws SerializationException
	{
	//	Precondition checking
	//
		Collection<Serializable> objectList = (Collection<Serializable>) object;
		if ((objectList == null) ||
			(objectList.isEmpty()))
		{
			return null;
		}
		
	//	Create underlying list
	//
		List<IRequestParameter> serializableCollection = null;
		if (object instanceof ArrayList)
		{	
			serializableCollection = new ArrayList<IRequestParameter>(objectList.size());
		}
		else if (object instanceof LinkedList)
		{
			serializableCollection = new LinkedList<IRequestParameter>();
		}
	//	else : unsupported GWT list
	//
		else
		{
			throw new SerializationException("Unsupported collection type : " + object.getClass());
		}
		
	//	Copy list contents
	//
		for (Serializable item : objectList)
		{
			serializableCollection.add(convertToGwt(item));
		}
		
		return new ListParameter(serializableCollection);
	}
	
	/**
	 * Convert the argument list from GWT IRequestParameter one
	 * @throws SerializationException 
	 */
	@SuppressWarnings("unchecked")
	protected Serializable convertListFromGwt(IRequestParameter object) 
											throws SerializationException
	{
	//	Precondition checking
	//
		Collection<IRequestParameter> objectList = (Collection<IRequestParameter>) object.getValue();
		if ((objectList == null) ||
			(objectList.isEmpty()))
		{
			return null;
		}
		
	//	Create underlying list
	//
		List<Serializable> serializableCollection = null;
		if (objectList instanceof ArrayList)
		{	
			serializableCollection = new ArrayList<Serializable>(objectList.size());
		}
		else if (objectList instanceof LinkedList)
		{
			serializableCollection = new LinkedList<Serializable>();
		}
	//	else : unsupported GWT list
	//
		else
		{
			throw new SerializationException("Unsupported collection type : " + objectList.getClass());
		}
		
	//	Copy list contents
	//
		for (IRequestParameter item : objectList)
		{
			serializableCollection.add(convertFromGwt(item));
		}
		
		return (Serializable) serializableCollection;
	}
	
	/**
	 * Convert the argument set to GWT serializable IRequestParameter
	 */
	@SuppressWarnings("unchecked")
	protected IRequestParameter convertSetToGwt(Serializable object) 
								throws SerializationException
	{
	//	Precondition checking
	//
		Collection<Serializable> objectList = (Collection<Serializable>) object;
		if ((objectList == null) ||
			(objectList.isEmpty()))
		{
			return null;
		}
		
	//	Create underlying list
	//
		Set<IRequestParameter> serializableCollection = null;
		if (object instanceof HashSet)
		{	
			serializableCollection = new HashSet<IRequestParameter>(objectList.size());
		}
		else if (object instanceof LinkedHashSet)
		{	
			serializableCollection = new LinkedHashSet<IRequestParameter>(objectList.size());
		}
		else if (object instanceof TreeSet)
		{
			serializableCollection = new TreeSet<IRequestParameter>();
		}
	//	else : unsupported GWT set
	//
		else
		{
			throw new SerializationException("Unsupported collection type : " + object.getClass());
		}
		
	//	Copy list contents
	//
		for (Serializable item : objectList)
		{
			serializableCollection.add(convertToGwt(item));
		}
		
		return new SetParameter(serializableCollection);
	}
	
	/**
	 * Convert the argument set to GWT serializable IRequestParameter
	 */
	@SuppressWarnings("unchecked")
	protected Serializable convertSetFromGwt(IRequestParameter object) 
								throws SerializationException
	{
	//	Precondition checking
	//
		Collection<IRequestParameter> objectList = (Collection<IRequestParameter>) object.getValue();
		if ((objectList == null) ||
			(objectList.isEmpty()))
		{
			return null;
		}
		
	//	Create serializable set
	//
		Set<Serializable> serializableCollection = null;
		if (objectList instanceof HashSet)
		{	
			serializableCollection = new HashSet<Serializable>(objectList.size());
		}
		else if (objectList instanceof LinkedHashSet)
		{	
			serializableCollection = new LinkedHashSet<Serializable>(objectList.size());
		}
		else if (objectList instanceof TreeSet)
		{
			serializableCollection = new TreeSet<Serializable>();
		}
	//	else : unsupported GWT set
	//
		else
		{
			throw new SerializationException("Unsupported collection type : " + objectList.getClass());
		}
		
	//	Copy list contents
	//
		for (IRequestParameter item : objectList)
		{
			serializableCollection.add(convertFromGwt(item));
		}
		
		return (Serializable) serializableCollection;
	}
	
	/**
	 * Convert the argument map to GWT serializable IRequestParameter
	 */
	@SuppressWarnings("unchecked")
	protected IRequestParameter convertMapToGwt(Serializable object) 
								throws SerializationException
	{
	//	Precondition checking
	//
		Map<Serializable, Serializable> objectMap = (Map<Serializable, Serializable>) object;
		if ((objectMap == null) ||
			(objectMap.isEmpty()))
		{
			return null;
		}
		
	//	Create underlying list
	//
		Map<IRequestParameter, IRequestParameter> serializableCollection = null;
		if (object instanceof HashMap)
		{	
			serializableCollection = new HashMap<IRequestParameter, IRequestParameter>(objectMap.size());
		}
		else if (object instanceof LinkedHashMap)
		{	
			serializableCollection = new LinkedHashMap<IRequestParameter,IRequestParameter>(objectMap.size());
		}
		else if (object instanceof TreeMap)
		{
			serializableCollection = new TreeMap<IRequestParameter, IRequestParameter>();
		}
	//	else : unsupported GWT map
	//
		else
		{
			throw new SerializationException("Unsupported collection type : " + object.getClass());
		}
		
	//	Copy map contents
	//
		for (Map.Entry<Serializable, Serializable> item : objectMap.entrySet())
		{
			serializableCollection.put(convertToGwt(item.getKey()),
									   convertToGwt(item.getValue()));
		}
		
		return new MapParameter(serializableCollection);
	}
	
	/**
	 * Convert the argument map from GWT serializable IRequestParameter
	 */
	@SuppressWarnings("unchecked")
	protected Serializable convertMapFromGwt(IRequestParameter object) 
								throws SerializationException
	{
	//	Precondition checking
	//
		Map<IRequestParameter, IRequestParameter> objectMap = (Map<IRequestParameter, IRequestParameter>) object.getValue();
		if ((objectMap == null) ||
			(objectMap.isEmpty()))
		{
			return null;
		}
		
	//	Create underlying list
	//
		Map<Serializable, Serializable> serializableCollection = null;
		if (objectMap instanceof HashMap)
		{	
			serializableCollection = new HashMap<Serializable, Serializable>(objectMap.size());
		}
		else if (objectMap instanceof LinkedHashMap)
		{	
			serializableCollection = new LinkedHashMap<Serializable,Serializable>(objectMap.size());
		}
		else if (objectMap instanceof TreeMap)
		{
			serializableCollection = new TreeMap<Serializable, Serializable>();
		}
	//	else : unsupported GWT map
	//
		else
		{
			throw new SerializationException("Unsupported collection type : " + object.getClass());
		}
		
	//	Copy map contents
	//
		for (Map.Entry<IRequestParameter, IRequestParameter> item : objectMap.entrySet())
		{
			serializableCollection.put(convertFromGwt(item.getKey()),
									   convertFromGwt(item.getValue()));
		}
		
		return (Serializable) serializableCollection;
	}
}
