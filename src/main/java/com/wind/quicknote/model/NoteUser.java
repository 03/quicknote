package com.wind.quicknote.model;

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
@Table(name="note_users")
public class NoteUser {
	
	@Id
	@SequenceGenerator(name = "UserSequence", sequenceName = "user_seq", allocationSize=5)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="UserSequence")
	private long id;
	
	@Column(name="login_name")
	private String loginName;
	@Column(name="first_name")
	private String firstName;
	@Column(name="last_name")
	private String lastName;
	@Column(name="role")
	private String role;
	@Column(name="password")
	private String password;
	@Column(name="email")
	private String email;
	@Column(name="description")
	private String desc;
	@Column(name="icon_url")
	private String icon;
	@Column(name="status")
	private String status;
	
	@JoinColumn(name="root_id")
	@OneToOne(cascade = CascadeType.ALL, optional = true)
	private NoteNode rootnote;
	
	/*@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "user_rootnodes", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "node_id"))
	private Set<NoteNode> nodes;*/
	
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
	
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
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
