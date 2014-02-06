package com.wind.quicknote.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.envers.Audited;

/**
 * Id, Name, IconUrl, Status, ParentId, Text, Attachment, Created, Updated
 * 
 * http://viralpatel.net/blogs/hibernate-one-to-one-mapping-tutorial-using-annotation/
 * http://javadigest.wordpress.com/2012/01/27/using-the-sequence-generator-in-hibernate/
 * http://www.mkyong.com/hibernate/hibernate-fetching-strategies-examples/
 * 
 * http://stackoverflow.com/questions/10124468/jpa-query-for-getting-all-nodes-in-a-single-tree
 * 
 */

@NamedQueries
({
	
		@NamedQuery(name = "findSimpleNodeById", query = "from NoteNode where id = :Id"),

		@NamedQuery(name = "findRootNoteByUser", query = "select user.rootnote from NoteUser user where user.id = :userId") 

})

@Entity
@Table(name = "note_nodes")
public class NoteNode /* extends DefaultRevisionEntity */ {

	@Id
	@SequenceGenerator(name = "NodeSequence", sequenceName = "node_seq", allocationSize=5)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="NodeSequence")
	private long id;

	@ManyToOne
	@JoinColumn(name="pid")
	private NoteNode parent;
	
	@OneToMany(cascade = CascadeType.PERSIST, mappedBy = "parent", fetch = FetchType.LAZY)
	@OrderBy("sorting")
	//@OneToMany(cascade = CascadeType.PERSIST, mappedBy = "parent", fetch = FetchType.EAGER)
	//@Fetch(FetchMode.JOIN)
	private List<NoteNode> children = new ArrayList<NoteNode>();
	
	@Audited
	@Column(name="name")
	private String name;
	
	@Audited
	@Column(name="tag")
	private String tag;
	
	@Audited
	@Column(name="text", length=4000)
	private String text;
	
	@Audited
	@Column(name="attachment")
	private byte[] attachment;
	
	@Audited
	@Column(name="icon_url")
	private String icon;
	
	@Audited
	@Column(name="status")
	private String status;
	
	@Column(name="created")
	private Date created;
	
	@Column(name="updated")
	private Date updated;
	
	@Column(name="sorting")
	private int sorting;
	
	public NoteNode() {
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

	public NoteNode getParent() {
		return parent;
	}

	public void setParent(NoteNode parent) {
		this.parent = parent;
	}

	public List<NoteNode> getChildren() {
		return children;
	}

	public void setChildren(List<NoteNode> children) {
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
	
	private String getParentName(NoteNode note) {
		
		NoteNode parent = note.getParent();
		if(parent != null) {
			return  getParentName(parent) + " -> " +  note.getName();
		} else {
			// ignore root
			// return note.getName();
			return "";
		}
		
	}
	
}
