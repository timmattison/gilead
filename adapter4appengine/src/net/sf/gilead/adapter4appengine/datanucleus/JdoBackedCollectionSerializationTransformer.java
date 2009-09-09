/**
 * 
 */
package net.sf.gilead.adapter4appengine.datanucleus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.user.server.rpc.ISerializationTransformer;

/**
 * Transformer for JDO backed collection encapsulation
 * 
 * @author bruno.marchesson
 * 
 */
public class JdoBackedCollectionSerializationTransformer implements
		ISerializationTransformer {
	// ----
	// Attributes
	// ----
	/**
	 * Log channel
	 */
	private static final Logger _log = Logger
			.getLogger(JdoBackedCollectionSerializationTransformer.class
					.getName());

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.user.server.rpc.ISerializationTransformer#isTransformable
	 * (java.lang.Object)
	 */
	public boolean isTransformable(Object instance) {
		// Precondition checking
		//
		if (instance == null) {
			return false;
		}

		// Package checking
		//
		return (instance.getClass().getName()
				.startsWith("org.datanucleus.sco.backed."));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.user.server.rpc.ISerializationTransformer#transform(java
	 * .lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public Object transform(Object instance) {
		if (_log.isLoggable(Level.FINE)) {
			_log.fine("Transforming backed collection : " + instance);
		}

		Collection<Object> backed = (Collection<Object>) instance;

		// Create relevant regular collection
		//
		Collection<Object> result = null;
		if (instance instanceof List) {
			if (instance instanceof LinkedList) {
				result = new LinkedList<Object>();
			} else {
				result = new ArrayList<Object>(backed.size());
			}
		} else if (instance instanceof Set) {
			result = new HashSet<Object>(backed.size());
		} else {
			// Probably means "to do"
			throw new RuntimeException("Unknown backed collection : "
					+ instance.getClass());
		}

		// Copy collection
		//
		for (Object item : backed) {
			result.add(item);
		}
		return result;
	}
}
