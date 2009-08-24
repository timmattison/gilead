package net.sf.gilead.gwt.client;

import net.sf.gilead.pojo.base.ILightEntity;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Remote loading service declaration
 * @author bruno.marchesson
 *
 */
@RemoteServiceRelativePath("LoadingService")
public interface LoadingService<T extends ILightEntity> extends RemoteService
{
	/**
	 * Load an association from the parent entity
	 * @param parent the entity
	 * @param property the name of the property to load
	 * @return the loaded entity
	 */
	public <K extends ILightEntity> K loadEntityAssociation(T parent, String propertyName);
}
