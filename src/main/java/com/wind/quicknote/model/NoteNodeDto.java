package com.wind.quicknote.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NoteNodeDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1040491222422094587L;

	private long id;

	private NoteNodeDto parent;
	private List<NoteNodeDto> children = new ArrayList<NoteNodeDto>();
	private String name;
	private String tag;
	private String text;
	private byte[] attachment;
	private String icon;
	private String status;
	private Date created;
	private Date updated;
	private int sorting;
	
	public NoteNodeDto() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public int getSorting() {
		return sorting;
	}

	public void setSorting(int sorting) {
		this.sorting = sorting;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public NoteNodeDto getParent() {
		return parent;
	}

	public void setParent(NoteNodeDto parent) {
		this.parent = parent;
	}

	public List<NoteNodeDto> getChildren() {
		return children;
	}

	public void setChildren(List<NoteNodeDto> children) {
		this.children = children;
	}
	
	public boolean hasChildren() {
		
		if (children != null && children.size() > 0)
			return true;
		
		return false;
	}

	public byte[] getAttachment() {
		return attachment;
	}

	public void setAttachment(byte[] attachment) {
		this.attachment = attachment;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getPath() {
		
		String path = getParentName(this);
		return path.substring(4, path.length());
	}
	
	private String getParentName(NoteNodeDto note) {
		
		NoteNodeDto parent = note.getParent();
		if(parent != null) {
			return  getParentName(parent) + " -> " +  note.getName();
		} else {
			return "";
		}
		
	}
	
}
