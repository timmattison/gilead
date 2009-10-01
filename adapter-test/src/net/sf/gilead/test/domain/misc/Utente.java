package net.sf.gilead.test.domain.misc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CollectionOfElements;

import net.sf.gilead.pojo.java5.LightEntity;

@Entity 
public class Utente extends LightEntity implements Serializable { 
 
	/**
	 * 
	 */
	private static final long serialVersionUID = -843102774046368069L;
	
	@Id
	@GeneratedValue
	private Integer id;
	
	@CollectionOfElements(fetch=FetchType.LAZY) 
	@Cascade({org.hibernate.annotations.CascadeType.ALL,org.hibernate.annotations.CascadeType.DELETE_ORPHAN}) 
	private List<Preference> preferences = new ArrayList<Preference>();

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the preferences
	 */
	public List<Preference> getPreferences() {
		return preferences;
	}

	/**
	 * @param preferences the preferences to set
	 */
	public void setPreferences(List<Preference> preferences) {
		this.preferences = preferences;
	} 
	
	
 
} 