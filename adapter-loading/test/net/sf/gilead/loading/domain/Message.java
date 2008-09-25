package net.sf.gilead.loading.domain;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Version;

/**
 * C test class.
 * @author bruno.marchesson
 *
 */
@Entity
public class Message 
{
	//----
	// Attributes
	//----
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	@Version
	private Integer version;
	
	private String text;
	private Date date;
	
	@ManyToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	private User author;

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

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}
}
