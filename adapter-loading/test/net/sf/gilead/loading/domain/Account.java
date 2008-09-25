package net.sf.gilead.loading.domain;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Version;

import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

/**
 * B test class.
 * @author bruno.marchesson
 *
 */
@Entity
public class Account 
{
	//----
	// Attributes
	//----
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	@Version
	private Integer version;
	
	private String login;
	private String domain;
	
	@OneToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL, optional=true)
	@PrimaryKeyJoinColumn
	@LazyToOne(LazyToOneOption.PROXY)
	private User user;
	
	@ManyToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	private Set<Right> rightList;

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

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Set<Right> getRightList() {
		return rightList;
	}

	public void setRightList(Set<Right> rightList) {
		this.rightList = rightList;
	}
}
