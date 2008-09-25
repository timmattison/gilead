package net.sf.gilead.loading.domain.interfaces;

import net.sf.gilead.loading.annotations.LoadingInterface;
import net.sf.gilead.loading.domain.User;


/**
 * Interface for loading User and associated account (bi-directional)
 */
@LoadingInterface(persistentClass=User.class)
public interface IUserAndAccount {

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

	public abstract IAccountAndUser getAccount();
	public abstract void setAccount(IAccountAndUser account);

}