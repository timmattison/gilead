package net.sf.gilead.loading.domain.interfaces;

import net.sf.gilead.loading.annotations.LoadingInterface;
import net.sf.gilead.loading.domain.User;

/**
 * Interface of the simplest loading strategy for User
 * @author bruno.marchesson
 *
 */
@LoadingInterface(persistentClass=User.class)
public interface ISimpleUser {

	//----
	// Properties
	//----
	public abstract Integer getId();
	public abstract void setId(Integer id);

	public abstract Integer getVersion();
	public abstract void setVersion(Integer version);

	public abstract String getFirstName();
	public abstract void setFirstName(String string);
	
	public abstract String getLastName();
	public abstract void setLastName(String string);

}