package net.sf.gilead.gwt.client;

import net.sf.gilead.pojo.java5.LightEntity;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Remote loading service declaration
 * @author bruno.marchesson
 *
 */
@RemoteServiceRelativePath("LoadingService")
public interface LoadingService extends RemoteService
{
	/**
	 * Load an association from the parent entity
	 * @param parent the entity
	 * @param property the name of the property to load
	 * @return the loaded entity
	 */
	public LightEntity loadAssociation(LightEntity parent, String propertyName);
}
