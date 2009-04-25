/**
 * 
 */
package net.sf.gilead.adapter4appengine.datanucleus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.appengine.repackaged.org.apache.commons.logging.Log;
import com.google.appengine.repackaged.org.apache.commons.logging.LogFactory;
import com.google.gwt.user.server.rpc.ISerializationTransformer;

/**
 * Transformer for JDO backed collection encapsulation
 * @author bruno.marchesson
 *
 */
public class JdoBackedCollectionSerializationTransformer implements ISerializationTransformer
{
	//----
	// Attributes
	//----
	/**
	 * Log channel
	 */
	private static Log _log = LogFactory.getLog(JdoBackedCollectionSerializationTransformer.class);
	
	/* (non-Javadoc)
	 * @see com.google.gwt.user.server.rpc.ISerializationTransformer#isTransformable(java.lang.Object)
	 */
	public boolean isTransformable(Object instance)
	{
	//	Precondition checking
	//
		if (instance == null)
		{
			return false;
		}
		
	//	Package checking
	//
		return (instance.getClass().getName().startsWith("org.datanucleus.sco.backed."));
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.user.server.rpc.ISerializationTransformer#transform(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public Object transform(Object instance)
	{
		if (_log.isDebugEnabled())
		{
			_log.debug("Transforming backed collection : " + instance);
		}
		
		Collection<Object> backed = (Collection<Object>)instance;
		
	//	Create relevant regular collection
	//
		Collection<Object> result = null;
		if (instance instanceof List)
		{
			result = new ArrayList<Object>(backed.size());
		}
		else if (instance instanceof Set)
		{
			result = new HashSet<Object>(backed.size());
		}
		else
		{
			// Probably means "to do"
			throw new RuntimeException("Unknown backed collection : " + instance.getClass());
		}
		
	//	Copy collection
	//
		for (Object item : backed)
		{
			result.add(item);
		}
		return  result;
	}
}
