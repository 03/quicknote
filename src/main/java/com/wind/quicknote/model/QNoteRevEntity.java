package com.wind.quicknote.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;

/**
 * http://docs.jboss.org/hibernate/core/4.2/devguide/en-US/html/ch15.html
 */
@Entity
@RevisionEntity(QNoteRevisionListener.class)
@Table(name="qnote_rev")
public class QNoteRevEntity extends DefaultRevisionEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6109388381162340732L;
	
	private String userName;
	private String ipAddr;
	private Date updated;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getIpAddr() {
		return ipAddr;
	}
	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}
	public Date getUpdated() {
		return updated;
	}
	public void setUpdated(Date updated) {
		this.updated = updated;
	}
	
}
