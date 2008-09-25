package net.sf.gilead.loading.domain.interfaces;

import net.sf.gilead.loading.annotations.LoadingInterface;
import net.sf.gilead.loading.domain.Account;

@LoadingInterface(persistentClass=Account.class)
public interface ISimpleAccount {

	//----
	// Properties
	//----
	public abstract Integer getId();
	public abstract void setId(Integer id);

	public abstract Integer getVersion();
	public abstract void setVersion(Integer version);

	public abstract String getLogin();
	public abstract void setLogin(String string);

	public abstract String getDomain();
	public abstract void setDomain(String string);

}