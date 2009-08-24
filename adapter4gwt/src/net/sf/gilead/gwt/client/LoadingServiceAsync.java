/**
 * 
 */
package net.sf.gilead.gwt.client;

import net.sf.gilead.pojo.base.ILightEntity;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Loading remote service implementation
 * @author bruno.marchesson
 * @generated generated asynchronous callback interface to be used on the client side
 *
 */

public interface LoadingServiceAsync<T extends ILightEntity> {

	/**
	 * Load an association from the parent entity
	 * @param <K>
	 * @param parent the entity
	 * @param property the name of the property to load
	 * @return the loaded entity
	 */
	public <K extends ILightEntity> void loadEntityAssociation(T parent, String propertyName, AsyncCallback<K> callback);
}
