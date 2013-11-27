package com.wind.quicknote.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.wind.quicknote.models.NoteNode;


/**
 * @author Luke
 * 
 *         This class provides functionality to access the {@code NoteNode} model
 *         storage system
 * 
 */
@SuppressWarnings("unchecked")
@Repository("noteNodeDAO")
public class NoteNodeDAOImpl implements NoteNodeDAO {

	@SuppressWarnings("rawtypes")
	@Autowired
	private IGenericDao genericDao;
	
	public List<NoteNode> findAll() {
        return genericDao.search("from NoteNode");
	}

	public List<NoteNode> findAllAvailableByUser(long userId) {
        
        Map<String, String> sqlParams = new HashMap<String, String> ();
		sqlParams.put("status", "a");
		return genericDao.search("from NoteNode where status=:status", sqlParams);
	}
	
	@Override
	public List<NoteNode> findChildren(long id) {
		
		NoteNode entity = (NoteNode) genericDao.findById(id, NoteNode.class);
		if(entity.hasChildren()) {
			List<NoteNode> list = entity.getChildren();
	        return list;
		}
		
		return new ArrayList<NoteNode>();
		
	}

	public void remove(long id) {
		
		NoteNode node = (NoteNode) genericDao.findById(id, NoteNode.class);
		if (node.hasChildren()) {
			for (NoteNode child : node.getChildren()) {
				remove(child.getId());
			}
		}
		genericDao.delete(node);
	}

	@Override
	public NoteNode findRootByUser(long userId) {
		
		@SuppressWarnings("rawtypes")
		Map sqlParams = new HashMap();
		sqlParams.put("userId", userId);
		
		List<NoteNode> list = genericDao.executeNamedQuery(
				"findRootNoteByUser", sqlParams, 1);
		NoteNode rootnote = list.get(0);
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
        	
        	genericDao.save(note);
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

		NoteNode entity = (NoteNode) genericDao.findById(id, NoteNode.class);
		entity.setUpdated(new Date());
		entity.setText(text);
		genericDao.merge(entity);
	}
	
	@Override
	public void updateName(long id, String name) {

		NoteNode entity = (NoteNode) genericDao.findById(id, NoteNode.class);
		entity.setName(name);
		entity.setUpdated(new Date());
		genericDao.merge(entity);
	}

	@Override
	public void updateIcon(long id, String iconSrc) {
		NoteNode entity = (NoteNode) genericDao.findById(id, NoteNode.class);
		entity.setIcon(iconSrc);
		entity.setUpdated(new Date());
		genericDao.merge(entity);
		
	}
	
	@Override
	public NoteNode findById(long id) {
		return (NoteNode) genericDao.findById(id, NoteNode.class);
	}

	@Override
	public NoteNode addChild(long pid, String name, String text, String picUrl) {

		NoteNode parent = (NoteNode) genericDao.findById(pid, NoteNode.class);
		NoteNode note = new NoteNode();
		note.setParent(parent);
		note.setName(name);
		note.setText(text);
		note.setIcon(picUrl == null? parent.getIcon():picUrl);
		note.setCreated(new Date());
		
		genericDao.save(note);
		return note;
	}

	@Override
	public void changeParent(long id, long pid) {
		NoteNode parent = (NoteNode) genericDao.findById(pid, NoteNode.class);
		NoteNode entity = (NoteNode) genericDao.findById(id, NoteNode.class);
		entity.setParent(parent);
		genericDao.merge(entity);
		
	}



}
