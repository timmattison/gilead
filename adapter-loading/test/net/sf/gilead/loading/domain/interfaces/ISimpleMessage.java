package net.sf.gilead.loading.domain.interfaces;

import java.util.Date;

import net.sf.gilead.loading.annotations.LoadingInterface;
import net.sf.gilead.loading.domain.Message;

@LoadingInterface(persistentClass=Message.class)
public interface ISimpleMessage {

	//----
	// Properties
	//----
	public abstract Integer getId();
	public abstract void setId(Integer id);

	public abstract Integer getVersion();
	public abstract void setVersion(Integer version);

	public abstract String getText();
	public abstract void setText(String string);

	public abstract Date getDate();
	public abstract void setDate(Date date);
}