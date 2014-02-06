package com.wind.quicknote.view.tree;

import com.wind.quicknote.model.NoteNode;

public class TopicItem {

	private long id;
	private String name;
    private String icon;

    // DTO would be a better choice
    public TopicItem(NoteNode note) {
		this.id = note.getId();
		this.name = note.getName();
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

	public String getIcon() {
		return icon;
	}
	
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
}
