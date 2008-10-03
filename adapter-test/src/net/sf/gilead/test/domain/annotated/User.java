package net.sf.gilead.test.domain.annotated;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import net.sf.gilead.annotations.ReadOnly;
import net.sf.gilead.annotations.ServerOnly;
import net.sf.gilead.pojo.java5.LightEntity;
import net.sf.gilead.test.domain.IMessage;
import net.sf.gilead.test.domain.IUser;

/**
 * User Domain class for JAVA5 server
 */
@Entity
@Table(name="user")
@Inheritance(strategy=InheritanceType.JOINED)
public class User extends LightEntity implements IUser
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
	@Id
	@GeneratedValue
	@Column(name="ID")
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	@Version
	@Column(name="VERSION")
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	@ReadOnly
	@Column(name="LOGIN", nullable=false, length=45)
	public String getLogin() {
		return this.login;
	}

	public void setLogin(String surname) {
		this.login = surname;
	}

	@Column(name="FIRST_NAME", nullable=false, length=45)
	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Column(name="LAST_NAME", nullable=false, length=45)
	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	@ServerOnly
	@Column(name="PASSWORD", length=45)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the messageList
	 */
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity=Message.class)
	@JoinColumn(name="USER_ID")
	@org.hibernate.annotations.Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	public Set<IMessage> getMessageList() {
		return messageList;
	}

	/**
	 * @param messageList the messageList to set
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
		((Message)message).setAuthor(this);
		
		// Create message list if needed
		if (messageList == null)
		{
			messageList = new HashSet<IMessage>();
		}
		messageList.add((Message)message);
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
