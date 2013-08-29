package com.wind.quicknote.daos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

import com.wind.quicknote.models.NoteNode;


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
	public List<NoteNode> findAllAvailableByUser(long userId) {
		
		//NoteUser user = UserCredentialManager.getIntance().getUser();
        
        NoteNode example = new NoteNode();
        example.setStatus("a");
        return getHibernateTemplate().findByExample(example);
	}
	
	@Override
	public List<NoteNode> findChildren(long id) {
		
		/*@SuppressWarnings("unchecked")
		List<NoteNode> list =  getHibernateTemplate().find("from NoteNode where parent.id=?", id);
		
		if(list != null && list.size() > 0)
			return list;*/
		
		NoteNode entity = (NoteNode) getHibernateTemplate().get(NoteNode.class, id);
		if(entity.hasChildren()) {
			List<NoteNode> list = entity.getChildren();
	        return list;
		}
		
		return new ArrayList<NoteNode>();
		
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
	public NoteNode findRootByUser(long userId) {
		
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session.getNamedQuery("findRootNoteByUser").setLong("userId", userId);
		
		NoteNode rootnote = (NoteNode) query.uniqueResult();
		if (rootnote != null) {
			// set children manually
			findChildNodes(rootnote);
			return rootnote;

		} else {
			
			NoteNode note = new NoteNode();
            note.setText("ROOT(invisible) of userid["+String.valueOf(userId)+"]");
            note.setName("ROOT");
    		note.setParent(null);
        	note.setCreated(new Date());
    		getHibernateTemplate().save(note);
    		return note;
		}
        
	}
	
	/*
	 * get root node, get root's all cascaded children
	 */
	public void findChildNodes(NoteNode parent) {
		
		/*Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Criteria crit = session.createCriteria(NoteNode.class);
		crit.setFetchMode("children", FetchMode.JOIN);
		crit.add(Restrictions.eq("id", 1L));
		NoteNode myobj = (NoteNode) crit.uniqueResult();*/
		
		/*
		 * Set as lazy load, fetch all cascaded children manually
		 */
		if (parent.hasChildren()) {
			for (NoteNode child : parent.getChildren()) {
				findChildNodes(child);
			}
		}
		
	}
	
	@Override
	public void updateText(long id, String text) {

		NoteNode entity = (NoteNode) getHibernateTemplate().get(NoteNode.class, id);
		entity.setUpdated(new Date());
		entity.setText(text);
		getHibernateTemplate().merge(entity);
	}
	
	@Override
	public void updateName(long id, String name) {

		NoteNode entity = (NoteNode) getHibernateTemplate().get(NoteNode.class, id);
		entity.setName(name);
		entity.setUpdated(new Date());
		getHibernateTemplate().merge(entity);
	}

	@Override
	public void updateIcon(long id, String iconSrc) {
		NoteNode entity = (NoteNode) getHibernateTemplate().get(NoteNode.class, id);
		entity.setIcon(iconSrc);
		entity.setUpdated(new Date());
		getHibernateTemplate().merge(entity);
		
	}
	
	@Override
	public NoteNode findById(long id) {
		return (NoteNode) getHibernateTemplate().get(NoteNode.class, id);
	}

	@Override
	public NoteNode addChild(long pid, String name, String text, String picUrl) {

		NoteNode parent = (NoteNode) getHibernateTemplate().get(NoteNode.class, pid);
		NoteNode note = new NoteNode();
		note.setParent(parent);
		note.setName(name);
		note.setText(text);
		note.setIcon(picUrl == null? parent.getIcon():picUrl);
		note.setCreated(new Date());
		
		getHibernateTemplate().save(note);
		return note;
	}

	@Override
	public void changeParent(long id, long pid) {
		NoteNode parent = (NoteNode) getHibernateTemplate().get(NoteNode.class, pid);
		NoteNode entity = (NoteNode) getHibernateTemplate().get(NoteNode.class, id);
		entity.setParent(parent);
		getHibernateTemplate().merge(entity);
		
	}



}
