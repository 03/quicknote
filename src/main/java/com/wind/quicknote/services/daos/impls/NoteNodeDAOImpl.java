package com.wind.quicknote.services.daos.impls;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.wind.quicknote.models.NoteContents;
import com.wind.quicknote.models.NoteNode;
import com.wind.quicknote.models.NoteUser;
import com.wind.quicknote.services.daos.NoteNodeDAO;
import com.wind.quicknote.systems.UserCredentialManager;


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
		
		NoteUser user = UserCredentialManager.getIntance().getUser();
        
        NoteNode example = new NoteNode();
        example.setStatus("a");
		example.setOwnerId(String.valueOf(user.getId()));
        return getHibernateTemplate().findByExample(example);
	}
	
	@Override
	public List<NoteNode> findChildTopics(long id) {
		
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

	@SuppressWarnings("unchecked")
	@Override
	public NoteNode findRootNode() {
		
		NoteUser user = UserCredentialManager.getIntance().getUser();
		long userId = user.getId();
		
        List<NoteNode> list =  getHibernateTemplate().find("from NoteNode where name='ROOT' and ownerId=? and parent = null", String.valueOf(userId));
        
        if(list.size() == 0) {
        	// initUserRootNoteNode 
        	NoteNode rootNode = new NoteNode();
            rootNode.setContent("ROOT(invisible) of user["+String.valueOf(userId)+"]");
            rootNode.setName("ROOT");
    		rootNode.setOwnerId(String.valueOf(userId));
    		rootNode.setParent(null);
        	rootNode.setCreated(new Date());
    		getHibernateTemplate().save(rootNode);
    		return rootNode;
    		
        } else {
        	
        	NoteNode root = (NoteNode) list.get(0);
        	
        	// set children manually
        	findChildNodes(root);
    		return root;
        }
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public NoteNode findRootNodeByUser(long userId) {
		
        List<NoteNode> list =  getHibernateTemplate().find("from NoteNode where name='ROOT' and ownerId=? and parent = null", String.valueOf(userId));
        //List<NoteNode> list =  getHibernateTemplate().find("select n from NoteNode n left outer join fetch n.parent p where n.name='ROOT' and n.ownerId=? and n.parent = null", String.valueOf(userId));
        
        if(list.size() == 0) {
        	// initUserRootNoteNode 
        	NoteNode rootNode = new NoteNode();
            rootNode.setContent("ROOT(invisible) of user["+String.valueOf(userId)+"]");
            rootNode.setName("ROOT");
    		rootNode.setOwnerId(String.valueOf(userId));
    		rootNode.setParent(null);
        	rootNode.setCreated(new Date());
    		getHibernateTemplate().save(rootNode);
    		return rootNode;
    		
        } else {
        	
        	NoteNode root = (NoteNode) list.get(0);
        	
        	// set children manually
        	findChildNodes(root);
    		return root;
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
	public String findTopicText(long id) {

		@SuppressWarnings("unchecked")
		List<NoteContents> list =  getHibernateTemplate().find("from NoteContents node where noteId=?", id);
        
		if(list.size() > 0) {
			return list.get(0).getText();
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
	public void updateTopicIcon(long id, String iconSrc) {
		NoteNode entity = (NoteNode) getHibernateTemplate().get(NoteNode.class, id);
		entity.setIcon(iconSrc);
		entity.setUpdated(new Date());
		getHibernateTemplate().merge(entity);
		
	}

	@Override
	public NoteNode addChild(long pid, String name, String content, String picUrl) {

		NoteNode parent = (NoteNode) getHibernateTemplate().get(NoteNode.class, pid);
		NoteNode note = new NoteNode();
		note.setParent(parent);
		note.setName(name);
		note.setContent(content);
		note.setIcon(picUrl == null? parent.getIcon():picUrl);
		note.setCreated(new Date());
		
		NoteUser user = UserCredentialManager.getIntance().getUser();
		note.setOwnerId(String.valueOf(user.getId()));
		
		NoteContents detail = new NoteContents();
		detail.setCreated(new Date());
		detail.setText(content);
		
		note.setDetails(detail);
		detail.setNote(note);
		getHibernateTemplate().save(note);
		
		return note;
	}

	@Override
	public void changeParentId(long id, long pid) {
		NoteNode parent = (NoteNode) getHibernateTemplate().get(NoteNode.class, pid);
		NoteNode entity = (NoteNode) getHibernateTemplate().get(NoteNode.class, id);
		entity.setParent(parent);
		getHibernateTemplate().merge(entity);
		
	}

}
