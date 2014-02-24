package com.wind.quicknote.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	
	private static Logger log = LoggerFactory.getLogger(NoteNodeDAOImpl.class);

	private static final int DEFAULT_SORTING = 100;

	public List<NoteNode> findAll() {
        return search("from NoteNode");
	}

	public List<NoteNode> findMatchedTopicsByUser(long userId, String keyword) {
        
		/*Map<String, Object> sqlParams = new HashMap<String, Object> ();
        if(keyword != null) {
          	sqlParams.put("userId", userId);
        	sqlParams.put("keyword", "%"+keyword+"%");
        	return search("from NoteNode t where t.text like :keyword and t.parent is not null order by t.id", sqlParams);
        } else {
        	return search("from NoteNode t where t.parent is not null order by t.id");
        }*/
        
        NoteNode root = findRootByUser(userId);
        List<NoteNode> result = new ArrayList<NoteNode>();
        
        if(root != null) {
        	findMatchChildren(result, root, keyword);
        }
        return result;
	}
	
	private void findMatchChildren(List<NoteNode> result, NoteNode root,
			String keyword) {
		if (root.hasChildren()) {
			for (NoteNode node : root.getChildren()) {
				findMatchChildren(result, node, keyword);
				if (keyword == null || node.getText().indexOf(keyword) != -1) {
					result.add(node);
				}
			}
		}
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
		
		List<NoteNode> list = executeNamedQuery( "findRootNoteByUser", sqlParams, 1);
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
        	note.setSorting(DEFAULT_SORTING);
        	
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
		
		List<NoteNode> children = parent.getChildren();
		int size = children.size();
		if(size == 0) {
			note.setSorting(DEFAULT_SORTING);
		} else {
			NoteNode lastChild = children.get(size - 1);
			note.setSorting(lastChild.getSorting() + DEFAULT_SORTING);
		}
		
		save(note);
		return note;
	}
	
	@Override
	public NoteNode addChild(long pid, int pos, String name, String text, String picUrl) {

		NoteNode parent = (NoteNode) find(NoteNode.class, pid);
		NoteNode note = new NoteNode();
		note.setParent(parent);
		note.setName(name);
		note.setText(text);
		note.setIcon(picUrl == null ? parent.getIcon() : picUrl);
		note.setCreated(new Date());
		note.setSorting(DEFAULT_SORTING);

		List<NoteNode> children = parent.getChildren();
		int size = children.size();
		if (pos < 0) {
			// add before the first child
			NoteNode currentChild = children.get(0);
			note.setSorting(currentChild.getSorting() - DEFAULT_SORTING);

		} else if (pos + 1 >= size) {
			// add after the last child
			NoteNode currentChild = children.get(pos);
			note.setSorting(currentChild.getSorting() + DEFAULT_SORTING);

		} else {
			
			int start = children.get(pos).getSorting();
			int end = children.get(pos + 1).getSorting();
			int diff = (end - start) / 2;
			log.debug("addChild: diff is " + diff);
			
			if (diff > 0) {
				note.setSorting(start + diff);
				
			} else {
				log.debug("reassign orders for all children");
				for(int i=0; i<size; i++) {
					children.get(i).setSorting(DEFAULT_SORTING * i);
				}
				
				note.setSorting(children.get(pos).getSorting() + DEFAULT_SORTING / 2);
			}

		}

		save(note);
		return note;
	}

	@Override
	public void changeParent(long id, long pid) {
		NoteNode parent = (NoteNode) find(NoteNode.class, pid);
		NoteNode entity = (NoteNode) find(NoteNode.class, id);
		entity.setParent(parent);

		// update sorting on new location
		List<NoteNode> children = parent.getChildren();
		if (children.size() > 0) {
			entity.setSorting(children.get(children.size() - 1).getSorting()
					+ DEFAULT_SORTING);
		} else {
			entity.setSorting(DEFAULT_SORTING);
		}

	}

	@Override
	public void swapPosition(long pid, int pos1, int pos2) {
		
		NoteNode parent = (NoteNode) find(NoteNode.class, pid);
		List<NoteNode> children = parent.getChildren();
		
		int sorting1 = children.get(pos1).getSorting();
		int sorting2 = children.get(pos2).getSorting();
		children.get(pos1).setSorting(sorting2);
		children.get(pos2).setSorting(sorting1);
		
	}


}
