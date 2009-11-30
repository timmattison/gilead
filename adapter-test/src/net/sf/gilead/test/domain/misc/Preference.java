package net.sf.gilead.test.domain.misc;

import java.io.Serializable;

import javax.persistence.Embeddable;

import org.hibernate.annotations.Parent;

import net.sf.gilead.pojo.java5.LightEntity;

@Embeddable 
public class Preference extends LightEntity implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -731857320269444208L;

	@Parent 
	private Utente user; 
	 
	private int intValue;

	/**
	 * @return the user
	 */
	public Utente getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(Utente user) {
		this.user = user;
	}

	/**
	 * @return the value
	 */
	public int getIntValue() {
		return intValue;
	}

	/**
	 * @param value the value to set
	 */
	public void setIntValue(int value) {
		this.intValue = value;
	} 
} 