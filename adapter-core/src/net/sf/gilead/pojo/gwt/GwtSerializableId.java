/**
 * 
 */
package net.sf.gilead.pojo.gwt;

import java.io.Serializable;

import net.sf.gilead.pojo.base.ILightEntity;

/**
 * GWT serializabe version of the SerializableId class.
 * @author bruno.marchesson
 *
 */
public class GwtSerializableId implements IRequestParameter
{
	//----
	// Attributes
	//----
	/**
	 * Serialization ID
	 */
	private static final long serialVersionUID = 6651960007634836456L;

	/**
	 * The underlying id
	 */
	protected IRequestParameter id;
	
	/**
	 * Hash code for non persistent and transient values
	 */
	protected Integer hashCode;
	
	/**
	 * The associated entity name
	 */
	protected String entityName;
	
	//----
	// Properties
	//----
	/**
	 * @return the id
	 */
	public IRequestParameter getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(IRequestParameter id) {
		this.id = id;
	}

	/**
	 * @return the hashCode
	 */
	public Integer getHashCode() {
		return hashCode;
	}

	/**
	 * @param hashCode the hashCode to set
	 */
	public void setHashCode(Integer hashCode) {
		this.hashCode = hashCode;
	}

	/**
	 * @return the entityName
	 */
	public String getEntityName() {
		return entityName;
	}

	/**
	 * @param entityName the entityName to set
	 */
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	//-------------------------------------------------------------------------
	//
	// IRequestParameter fake implementation
	//
	//-------------------------------------------------------------------------
	/*
	 * (non-Javadoc)
	 * @see net.sf.gilead.pojo.gwt.IRequestParameter#getParameterClass()
	 */
	public Class<?> getParameterClass()
	{
		return null;
	}

	public Object getValue()
	{
		return null;
	}

}
