package com.wind.quicknote.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.wind.quicknote.model.NoteNode;


/**
 * @author Luke
 * 
 *         This class provides functionality to access the {@code NoteNode} model
 *         storage system
 * 
 */
@SuppressWarnings("unchecked")
@Repository("noteNodeDAO")
public class NoteNodeDAOImpl extends GenericDao<NoteNode> implements NoteNodeDAO {

	public List<NoteNode> findAll() {
        return search("from NoteNode");
	}

	public List<NoteNode> findAllAvailableByUser(long userId) {
        
        Map<String, Object> sqlParams = new HashMap<String, Object> ();
		sqlParams.put("status", "a");
		return search("from NoteNode where status=:status", sqlParams);
	}
	
	@Override
	public List<NoteNode> findChildren(long id) {
		
		NoteNode entity = (NoteNode) find(NoteNode.class, id);
		if(entity.hasChildren()) {
			List<NoteNode> list = entity.getChildren();
	        return list;
		}
		
		return new ArrayList<NoteNode>();
		
	}

	public void remove(long id) {
		
		NoteNode node = (NoteNode) find(NoteNode.class, id);
		if (node.hasChildren()) {
			for (NoteNode child : node.getChildren()) {
				remove(child.getId());
			}
		}
		delete(node);
	}

	@Override
	public NoteNode findRootByUser(long userId) {
		
		@SuppressWarnings("rawtypes")
		Map sqlParams = new HashMap();
		sqlParams.put("userId", userId);
		
		List<NoteNode> list = executeNamedQuery(
				"findRootNoteByUser", sqlParams, 1);
		NoteNode rootnote = list.get(0);
		if (rootnote != null) {
			// setting up children manually
			findChildNodes(rootnote);
			return rootnote;

		} else {
			
			NoteNode note = new NoteNode();
            note.setText("ROOT(invisible) of userid["+String.valueOf(userId)+"]");
            note.setName("ROOT");
    		note.setParent(null);
        	note.setCreated(new Date());
        	
        	save(note);
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
	public NoteNode addChild(long pid, String name, String text, String picUrl) {

		NoteNode parent = (NoteNode) find(NoteNode.class, pid);
		NoteNode note = new NoteNode();
		note.setParent(parent);
		note.setName(name);
		note.setText(text);
		note.setIcon(picUrl == null? parent.getIcon():picUrl);
		note.setCreated(new Date());
		
		save(note);
		return note;
	}

	@Override
	public void changeParent(long id, long pid) {
		NoteNode parent = (NoteNode) find(NoteNode.class, pid);
		NoteNode entity = (NoteNode) find(NoteNode.class, id);
		entity.setParent(parent);
		
	}


}
