/**
 * 
 */
package net.sf.gilead.adapter4appengine.datanucleus;

import com.google.gwt.user.server.rpc.ISerializationFilter;

/**
 * Serialization filter for DataNucleus JDO fields.
 * @author bruno.marchesson
 *
 */
public class JdoSerializationFilter implements ISerializationFilter
{
	/* (non-Javadoc)
	 * @see com.google.gwt.user.server.rpc.ISerializationAdapter#beforeSerialization(java.lang.Object)
	 */
	public void beforeSerialization(Object obj)
	{
	}


	/* (non-Javadoc)
	 * @see com.google.gwt.user.server.rpc.ISerializationAdapter#afterSerialization(java.lang.Object)
	 */
	public void afterSerialization(Object obj)
	{
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.user.server.rpc.ISerializationAdapter#shouldSerialize(java.lang.Class, java.lang.String)
	 */
	public boolean shouldSerialize(Class<?> clazz, String fieldName)
	{
		return (fieldName.startsWith("jdo") == false);
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.user.server.rpc.ISerializationAdapter#shouldSerialize(java.lang.Object, java.lang.String, java.lang.Object)
	 */
	public boolean shouldSerialize(Object obj, String fieldName, Object value)
	{
		return (fieldName.startsWith("jdo") == false);
	}

}
