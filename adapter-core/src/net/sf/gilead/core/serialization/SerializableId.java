package net.sf.gilead.core.serialization;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
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
	 * The associated class name
	 */
	protected String className;

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
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * @param className the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
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
		
		if (className.equals(((SerializableId)other).className) == false)
		{
			return false;
		}
		if (id == null)
		{
			return (((SerializableId)other).id == null);
		}
		else 
		{
			return id.equals(((SerializableId)other).id);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see java.io.Externalizable#writeExternal(java.io.ObjectOutput)
	 */
//	public void writeExternal(ObjectOutput out) throws IOException
//	{
//		out.writeUTF(className);
//		out.writeObject(id);
//		
//	}
//	
//	/*
//	 * (non-Javadoc)
//	 * @see java.io.Externalizable#readExternal(java.io.ObjectInput)
//	 */
//	public void readExternal(ObjectInput in) throws IOException,
//													ClassNotFoundException
//	{
//		className = in.readUTF();
//		id = (Serializable) in.readObject();
//	}	
}
