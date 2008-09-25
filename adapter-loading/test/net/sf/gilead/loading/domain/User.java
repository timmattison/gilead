package net.sf.gilead.loading.domain;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Version;

import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

/**
 * A test class.
 * @author bruno.marchesson
 *
 */
@Entity
public class User 
{
	//----
	// Attributes
	//----
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	@Version
	private Integer version;
	
	private String firstName;
	private String lastName;
	
	@OneToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL, optional=true)
	@PrimaryKeyJoinColumn
	@LazyToOne(LazyToOneOption.PROXY)
	private Account account;
	
	@OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="author")
	private Set<Message> messageList;

	//----
	// Properties
	//----
	public Integer getId() {
		return id;
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

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Set<Message> getMessageList() {
		return messageList;
	}

	public void setMessageList(Set<Message> messageList) {
		this.messageList = messageList;
	}
	
}
