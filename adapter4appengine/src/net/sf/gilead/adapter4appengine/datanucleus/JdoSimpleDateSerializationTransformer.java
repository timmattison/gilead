/**
 * 
 */
package net.sf.gilead.adapter4appengine.datanucleus;

import com.google.gwt.user.server.rpc.ISerializationTransformer;

/**
 * JDO Date encapsulation transformer.
 * @author bruno.marchesson
 *
 */
public class JdoSimpleDateSerializationTransformer implements ISerializationTransformer
{
	/*
	 * (non-Javadoc)
	 * @see com.google.gwt.user.server.rpc.ISerializationTransformer#isTransformable(java.lang.Object)
	 */
	public boolean isTransformable(Object instance)
	{
		return (instance instanceof org.datanucleus.sco.simple.Date);
	}

	/*
	 * (non-Javadoc)
	 * @see com.google.gwt.user.server.rpc.ISerializationTransformer#transform(java.lang.Object)
	 */
	public Object transform(Object instance)
	{
		org.datanucleus.sco.simple.Date date = (org.datanucleus.sco.simple.Date) instance;
		return date.getValue();
	}
	
}
