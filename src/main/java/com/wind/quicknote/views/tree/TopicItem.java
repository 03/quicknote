package com.wind.quicknote.views.tree;

public class TopicItem {

	private long id;
	private String name;
	private String content;
    private String profilepic;
    private boolean isLeaf = true;
 
    public TopicItem(long id, String name, String content, String profilepic) {
		super();
		this.id = id;
		this.name = name;
		this.content = content;
		this.profilepic = profilepic;
	}

	public TopicItem(String name, String content, String profilepic) {
		super();
		this.name = name;
		this.content = content;
		this.profilepic = profilepic;
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

	public String getProfilepic() {
		return profilepic;
	}

	public void setProfilepic(String profilepic) {
		this.profilepic = profilepic;
	}

	public boolean isLeaf() {
		return isLeaf;
	}

	public void setLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}

}
