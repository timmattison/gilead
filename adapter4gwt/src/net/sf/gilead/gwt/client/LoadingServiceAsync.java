/**
 * 
 */
package net.sf.gilead.gwt.client;

import net.sf.gilead.pojo.java5.LightEntity;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Loading remote service implementation
 * @author bruno.marchesson
 * @generated generated asynchronous callback interface to be used on the client side
 *
 */

public interface LoadingServiceAsync {

	/**
	 * Load an association from the parent entity
	 * @param parent the entity
	 * @param property the name of the property to load
	 * @return the loaded entity
	 */
	void loadAssociation(LightEntity parent, String propertyName, AsyncCallback<LightEntity> callback);

}
