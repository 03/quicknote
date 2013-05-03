package com.wind.quicknote.views.tree;

import com.wind.quicknote.models.NoteNode;

public class TopicItem {

	private long id;
	private String name;
	private String content;
    private String icon;

    // convenient model, DTO is a better choice
    private NoteNode note;
 
    public TopicItem(NoteNode note) {
		super();
		this.note = note;
		this.id = note.getId();
		this.name = note.getName();
		this.content = note.getName();
		this.icon = note.getIcon();
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getIcon() {
		return icon;
	}
	
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	public boolean isLeaf() {
		return !note.hasChildren();
	}


}
