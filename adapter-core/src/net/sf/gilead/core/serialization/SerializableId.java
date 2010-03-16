package net.sf.gilead.core.serialization;

import java.io.Serializable;

/**
 * Id / Class structure for proxy information collection handling
 * @author bruno.marchesson
 *
 */
public class SerializableId implements Serializable
{
	//----
	// Attributes
	//----
	/**
	 * Serialization ID
	 */
	private static final long serialVersionUID = -4365286012503534L;
	
	/**
	 * The underlying id
	 */
	protected Serializable id;
	
	/**
	 * Hash code for non persistent and transient values
	 * Contains values for Number and String non persistent items
	 */
	protected String hashCode;
	
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
	public Serializable getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Serializable id) {
		this.id = id;
	}

	/**
	 * @return the hashCode
	 */
	public String getHashCode() {
		return hashCode;
	}

	/**
	 * @param hashCode the hashCode to set
	 */
	public void setHashCode(String hashCode) {
		this.hashCode = hashCode;
	}

	/**
	 * @return the entity Name
	 */
	public String getEntityName() {
		return entityName;
	}

	/**
	 * @param entityName the entity Name to set
	 */
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	
	//-------------------------------------------------------------------------
	//
	// Public interface
	//
	//-------------------------------------------------------------------------
	@Override
	public boolean equals(Object other)
	{
		if ((other == null) ||
			(other instanceof SerializableId == false))
		{
			return false;
		}
		
		// Check entity name
		if (entityName == null)
		{
			return (((SerializableId)other).entityName == null);
		}
		else if (entityName.equals(((SerializableId)other).entityName) == false)
		{
			return false;
		}
		
		// Check id or hashcode
		if (id == null)
		{
			if (hashCode != null)
			{
				return hashCode.equals(((SerializableId)other).hashCode);
			}
			else
			{
				return ((SerializableId)other).hashCode == null;
			}
		}
		else 
		{
			return id.equals(((SerializableId)other).id);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuilder result = new StringBuilder();
		result.append("[");
		result.append(entityName);
		result.append("/");
		if (id != null)
		{
			result.append("ID:");
			result.append(id);
		}
		else
		{
			result.append("hashcode:");
			result.append(hashCode);
		}
		result.append("]");
		
		return result.toString();
	}
}
