package net.sf.gilead.test.domain.dto;

import java.util.HashSet;
import java.util.Set;

import net.sf.gilead.pojo.java5.LazyPojo;
import net.sf.gilead.test.domain.IMessage;
import net.sf.gilead.test.domain.IUser;

/**
 * DTO User class for Java5 support
 */
public class UserDTO extends LazyPojo implements IUser
{
	/**
	 * Serialisation ID
	 */
	private static final long serialVersionUID = 1058354709157710766L;
	
	// Fields
	private Integer id;
	private Integer version;
	
	private String login;
	private String firstName;
	private String lastName;
	private String password;
	
	private Set<IMessage> messageList;

	// Properties
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getLogin() {
		return this.login;
	}

	public void setLogin(String surname) {
		this.login = surname;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	

	/**
	 * @return the message List
	 */
	public Set<IMessage> getMessageList() {
		return messageList;
	}

	/**
	 * @param messageList the message List to set
	 */
	public void setMessageList(Set<IMessage> messageList) {
		this.messageList = messageList;
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.gilead.testApplication.domain.IUser#addMessage(net.sf.gilead.testApplication.domain.IMessage)
	 */
	public void addMessage(IMessage message)
	{
		((MessageDTO)message).setAuthor(this);
		if (messageList == null)
		{
			messageList = new HashSet<IMessage>();
		}
		messageList.add(message);
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.gilead.testApplication.domain.IUser#removeMessage(net.sf.gilead.testApplication.domain.IMessage)
	 */
	public void removeMessage(IMessage message)
	{
		messageList.remove(message);
	}
}
