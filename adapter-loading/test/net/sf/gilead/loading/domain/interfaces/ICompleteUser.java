package net.sf.gilead.loading.domain.interfaces;

import java.util.List;

import net.sf.gilead.loading.annotations.LoadingInterface;
import net.sf.gilead.loading.domain.User;

@LoadingInterface(persistentClass=User.class)
public interface ICompleteUser {

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

	public abstract IAccountAndRights getAccount();
	public abstract void setAccount(IAccountAndRights account);
	
	public abstract List<ISimpleMessage> getMessageList();
	public abstract void setMessageList(List<ISimpleMessage> messageList);
}