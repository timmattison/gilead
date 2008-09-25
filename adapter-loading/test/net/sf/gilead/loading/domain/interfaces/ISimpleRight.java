package net.sf.gilead.loading.domain.interfaces;

import net.sf.gilead.loading.annotations.LoadingInterface;
import net.sf.gilead.loading.domain.Right;

@LoadingInterface(persistentClass=Right.class)
public interface ISimpleRight {

	//----
	// Properties
	//----
	public abstract Integer getId();
	public abstract void setId(Integer id);

	public abstract Integer getVersion();
	public abstract void setVersion(Integer version);

	public abstract String getName();
	public abstract void setName(String string);

	public abstract Integer getValue();
	public abstract void setValue(Integer integer);

}