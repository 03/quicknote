package com.wind.quicknote.view.tree;

import com.wind.quicknote.model.NoteNode;

public class TopicItem {

	private long id;
	private String name;
	private String text;
    private String icon;

    // convenient model, DTO is a better choice
    private NoteNode note;
 
    public TopicItem(NoteNode note) {
		super();
		this.note = note;
		this.id = note.getId();
		this.name = note.getName();
		this.text = note.getText();
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

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
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
