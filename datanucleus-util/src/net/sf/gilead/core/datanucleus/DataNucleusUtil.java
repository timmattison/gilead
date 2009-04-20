/**
 * 
 */
package net.sf.gilead.core.datanucleus;

import java.io.Serializable;
import java.util.Map;

import javax.jdo.spi.PersistenceCapable;

import net.sf.gilead.core.IPersistenceUtil;

/**
 * DataNucleus enhancement manager
 * @author bruno.marchesson
 *
 */
public class DataNucleusUtil implements IPersistenceUtil {

	/* (non-Javadoc)
	 * @see net.sf.gilead.core.IPersistenceUtil#closeCurrentSession()
	 */
	@Override
	public void closeCurrentSession() 
	{
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see net.sf.gilead.core.IPersistenceUtil#createEntityProxy(java.util.Map)
	 */
	@Override
	public Object createEntityProxy(Map<String, Serializable> proxyInformations)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see net.sf.gilead.core.IPersistenceUtil#createPersistentCollection(java.util.Map, java.lang.Object)
	 */
	@Override
	public Object createPersistentCollection(
			Map<String, Serializable> proxyInformations,
			Object underlyingCollection)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see net.sf.gilead.core.IPersistenceUtil#getId(java.lang.Object)
	 */
	@Override
	public Serializable getId(Object pojo)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see net.sf.gilead.core.IPersistenceUtil#getId(java.lang.Object, java.lang.Class)
	 */
	@Override
	public Serializable getId(Object pojo, Class<?> persistentClass)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see net.sf.gilead.core.IPersistenceUtil#getUnenhancedClass(java.lang.Class)
	 */
	@Override
	public Class<?> getUnenhancedClass(Class<?> clazz)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see net.sf.gilead.core.IPersistenceUtil#initialize(java.lang.Object)
	 */
	@Override
	public void initialize(Object proxy)
	{
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see net.sf.gilead.core.IPersistenceUtil#isEnhanced(java.lang.Class)
	 */
	@Override
	public boolean isEnhanced(Class<?> clazz)
	{
		return (PersistenceCapable.class.isAssignableFrom(clazz));
	}

	/* (non-Javadoc)
	 * @see net.sf.gilead.core.IPersistenceUtil#isInitialized(java.lang.Object)
	 */
	@Override
	public boolean isInitialized(Object proxy)
	{
		if (proxy instanceof PersistenceCapable)
		{
			// 
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see net.sf.gilead.core.IPersistenceUtil#isPersistentClass(java.lang.Class)
	 */
	@Override
	public boolean isPersistentClass(Class<?> clazz)
	{
		return PersistenceCapable.class.isAssignableFrom(clazz);
	}

	/* (non-Javadoc)
	 * @see net.sf.gilead.core.IPersistenceUtil#isPersistentCollection(java.lang.Class)
	 */
	@Override
	public boolean isPersistentCollection(Class<?> collectionClass) 
	{
		return PersistenceCapable.class.isAssignableFrom(collectionClass);
	}

	/* (non-Javadoc)
	 * @see net.sf.gilead.core.IPersistenceUtil#isPersistentPojo(java.lang.Object)
	 */
	public boolean isPersistentPojo(Object pojo)
	{
		if (pojo instanceof PersistenceCapable == false)
		{
			return false;
		}
		
	//	Is POJO transient or not
	//
		return ((PersistenceCapable)pojo).jdoIsPersistent();
	}

	/* (non-Javadoc)
	 * @see net.sf.gilead.core.IPersistenceUtil#load(java.io.Serializable, java.lang.Class)
	 */
	@Override
	public Object load(Serializable id, Class<?> persistentClass) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see net.sf.gilead.core.IPersistenceUtil#openSession()
	 */
	@Override
	public void openSession() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see net.sf.gilead.core.IPersistenceUtil#serializeEntityProxy(java.lang.Object)
	 */
	@Override
	public Map<String, Serializable> serializeEntityProxy(Object proxy) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see net.sf.gilead.core.IPersistenceUtil#serializePersistentCollection(java.lang.Object)
	 */
	@Override
	public Map<String, Serializable> serializePersistentCollection(
			Object persistentCollection) {
		// TODO Auto-generated method stub
		return null;
	}

}
