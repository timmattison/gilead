/**
 * 
 */
package net.sf.gilead.adapter4appengine.server.domain;

import java.io.Serializable;

import com.google.appengine.api.datastore.Text;

/**
 * Test entity for GWT emulation.
 * @author bruno.marchesson
 *
 */
public class TestEntity implements Serializable
{
	//----
	// Attributes
	//----
	/**
	 * Serialization ID
	 */
	private static final long serialVersionUID = 489327238782855667L;

	/**
	 * Text field
	 */
	private Text text;
	
	//----
	// Properties
	//----
	/**
	 * @return the text
	 */
	public Text getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(Text text) {
		this.text = text;
	}

	//----
	// Constructor
	//---
	/**
	 * Empty constructor
	 */
	public TestEntity()
	{
	}
}
