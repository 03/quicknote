package com.wind.quicknote.services.daos.impls;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.wind.quicknote.models.NoteContents;
import com.wind.quicknote.models.NoteNode;
import com.wind.quicknote.services.daos.NoteNodeDAO;


/**
 * @author Luke
 * 
 *         This class provides functionality to access the {@code NoteNode} model
 *         storage system
 * 
 */
@Component("noteNode")
public class NoteNodeDAOImpl extends CommonDAO implements NoteNodeDAO {

	@SuppressWarnings("unchecked")
	public List<NoteNode> findAll() {
        return getHibernateTemplate().find("from NoteNode");
	}

	@SuppressWarnings("unchecked")
	public List<NoteNode> findAllAvailable() {
        
        NoteNode example = new NoteNode();
        example.setStatus("a");
        return getHibernateTemplate().findByExample(example);
	}
	
	@Override
	public List<NoteNode> findChildTopics(long id) {
		
		NoteNode entity = (NoteNode) getHibernateTemplate().get(NoteNode.class, id);
        return entity.getChildren();
	}


	public void remove(long id) {
		NoteNode node = getHibernateTemplate().get(NoteNode.class, id);
		if (node.hasChildren()) {
			for (NoteNode child : node.getChildren()) {
				remove(child.getId());
			}
		}
		getHibernateTemplate().delete(node);
	}

	@Override
	public NoteNode findRootNode() {
		return (NoteNode) getHibernateTemplate().get(NoteNode.class, Long.parseLong("1"));
	}
	
	@Override
	public String findTopicText(long id) {

		NoteNode entity = (NoteNode) getHibernateTemplate().get(NoteNode.class, id);
		NoteContents details = entity.getDetails();
		if(details != null) {
			return details.getText();
		}
		return "";
	}

	@Override
	public void updateTopicText(long id, String text) {

		NoteNode entity = (NoteNode) getHibernateTemplate().get(NoteNode.class, id);
		entity.setUpdated(new Date());
		entity.getDetails().setText(text);
		entity.getDetails().setUpdated(new Date());
		getHibernateTemplate().merge(entity);
	}
	
	@Override
	public void updateTopicName(long id, String name) {

		NoteNode entity = (NoteNode) getHibernateTemplate().get(NoteNode.class, id);
		entity.setName(name);
		entity.setUpdated(new Date());
		getHibernateTemplate().merge(entity);
	}

	@Override
	public long addChild(long pid, String name, String content, String picUrl) {

		NoteNode parent = (NoteNode) getHibernateTemplate().get(NoteNode.class, pid);
		NoteNode note = new NoteNode();
		note.setParent(parent);
		note.setName(name);
		note.setContent(content);
		note.setIcon(picUrl == null? parent.getIcon():picUrl);
		note.setCreated(new Date());
		note.setUpdated(new Date());
		
		NoteContents detail = new NoteContents();
		detail.setCreated(new Date());
		detail.setText(content);
		
		note.setDetails(detail);
		detail.setNote(note);
		getHibernateTemplate().save(note);
		
		return note.getId();
	}

	@Override
	public void changeParentId(long id, long pid) {
		NoteNode parent = (NoteNode) getHibernateTemplate().get(NoteNode.class, pid);
		NoteNode entity = (NoteNode) getHibernateTemplate().get(NoteNode.class, id);
		entity.setParent(parent);
		getHibernateTemplate().merge(entity);
		
	}

}
