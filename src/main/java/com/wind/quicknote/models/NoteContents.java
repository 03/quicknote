package com.wind.quicknote.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Id, noteId(fk), Text, Attachment, Created, Updated
 */
@Entity
@Table(name = "notecontents")
public class NoteContents {

	@Id
	//@GeneratedValue(strategy = GenerationType.AUTO)
	@SequenceGenerator(name = "ContentSequence", sequenceName = "content_seq", allocationSize=5)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ContentSequence")
	private long id;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "noteId", unique = true)
	private NoteNode note;
	
	@Column(name="text", length=4000)
	private String text;

	@Column(name="attachment")
	private byte[] attachment;
	
	@Column(name="created")
	private Date created;
	@Column(name="updated")
	private Date updated;
	
	public NoteContents() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public byte[] getAttachment() {
		return attachment;
	}

	public void setAttachment(byte[] attachment) {
		this.attachment = attachment;
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

	public NoteNode getNote() {
		return note;
	}

	public void setNote(NoteNode note) {
		this.note = note;
	}

}
