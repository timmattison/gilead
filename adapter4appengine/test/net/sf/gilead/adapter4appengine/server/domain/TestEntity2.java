package net.sf.gilead.adapter4appengine.server.domain;

import java.io.Serializable;
import java.util.Arrays;

import com.google.appengine.api.datastore.*;

/**
 * Test object for DataNucleus emulation classes.
 * This is an extention of {@link TestEntity} (tests more classes)
 * 
 * @see
 * 		{@link TestEntity}
 * 
 * @author Alex Dobjanschi (alex.dobjanschi@gmail.com)
 *
 */
@SuppressWarnings("serial")
public class TestEntity2 implements Serializable {

	/**
	 * Assume we have a large text, here we'll hold all its bytes (since String is limited to 500) 
	 */
	private Blob textBytes;

	/**
	 * Only the first 100 here
	 */
	private ShortBlob textThumb;

	/**
	 * "Link" to the contents (only a naming coincidence)
	 */
	private Link link;
	
	/**
	 * The key for this entity
	 */
	private Key key;

	public TestEntity2 () {
		
	}

	public Blob getTextBytes() {
		return textBytes;
	}

	public void setTextBytes(Blob textBytes) {
		this.textBytes = textBytes;
		this.textThumb = new ShortBlob (this.textBytes.getBytes());
	}

	public ShortBlob getTextThumb() {
		return textThumb;
	}

	public Link getLink() {
		return link;
	}

	public void setLink(Link link) {
		this.link = link;
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}
}
