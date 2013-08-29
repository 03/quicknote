package com.wind.quicknote.models;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 
 */
@Entity
@Table(name="noteusers")
public class NoteUser {
	
	@Id
	//@GeneratedValue(strategy = GenerationType.AUTO)
	@SequenceGenerator(name = "UserSequence", sequenceName = "user_seq", allocationSize=5)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="UserSequence")
	private long id;
	
	@Column(name="name")
	private String name;
	@Column(name="role")
	private String role;
	@Column(name="password")
	private String password;
	@Column(name="email")
	private String email;
	@Column(name="description")
	private String desc;
	@Column(name="iconUrl")
	private String icon;
	@Column(name="status")
	private String status;
	
	@JoinColumn(name="rootid")
	@OneToOne(cascade = CascadeType.ALL, optional = true)
	private NoteNode rootnote;
	
	@Column(name="created")
	private Date created;
	@Column(name="updated")
	private Date updated;

	public NoteUser() {
		
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public NoteNode getRootnote() {
		return rootnote;
	}

	public void setRootnote(NoteNode rootnote) {
		this.rootnote = rootnote;
	}

	
}
