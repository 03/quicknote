package com.wind.quicknote.views.tree;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zul.DefaultTreeModel;
import org.zkoss.zul.DefaultTreeNode;
import org.zkoss.zul.TreeNode;

import com.wind.quicknote.helpers.QUtils;
import com.wind.quicknote.helpers.SpringBeanUtil;
import com.wind.quicknote.models.NoteNode;
import com.wind.quicknote.models.NoteUser;
import com.wind.quicknote.services.NoteService;
import com.wind.quicknote.systems.UserCredentialManager;
 
/*
 * http://forum.zkoss.org/question/81157/add-mvvm-model-to-spring-for-adding-transactional-annotation/
 * http://books.zkoss.org/wiki/ZK_Developer's_Reference/MVVM/Data_Binding
 */
public class TopicTreeModel extends DefaultTreeModel<TopicItem> {
	
	
	private static Logger log = LoggerFactory.getLogger(TopicTreeModel.class);
	
	public static final String TOPIC_NEW_ITEM = "[New Item]";
	public static final String TOPIC_NEWLY_ADDED_CONTENT = "newly added content...";
    /**
	 * 
	 */
	private static final long serialVersionUID = 7973634536446973065L;
	
	private static NoteService noteService = SpringBeanUtil.getBean("noteService", NoteService.class);;
	
    DefaultTreeNode<TopicItem> _root;
    
    private TopicTreeModel(TopicItemTreeNode treeNode) {
        super(treeNode, true);
        _root = treeNode;
    }
    
    public static TopicTreeModel getInstance() {
    	
    	log.debug("TopicTreeModel inited.");
    	
		NoteUser user = UserCredentialManager.getIntance().getUser();
		TopicTreeModel topicTreeModel = new TopicTreeModel(
				createTreeNode(noteService.findRootTopicByUser(user.getId())));

    	return topicTreeModel;
    }
    
    
    private static TopicItemTreeNode createTreeNode(NoteNode node) {
		
		TopicItem rootTopic = new TopicItem(node);

		if (node.hasChildren()) {
			
			List<NoteNode> notes = node.getChildren();
			int size = notes.size();

			TopicItemTreeNode[] array = new TopicItemTreeNode[size];
			for (int i = 0; i < size; i++) {
				NoteNode child = notes.get(i);
				array[i] = createTreeNode(child);
			}

			return new TopicItemTreeNode(rootTopic, array);
			
		} else {

			return new TopicItemTreeNode(rootTopic, null, true);
		}

	}
 
	/**
     * remove the nodes which parent is <code>parent</code> with indexes
     * <code>indexes</code>
     * 
     * @param parent
     *            The parent of nodes are removed
     * @param indexFrom
     *            the lower index of the change range
     * @param indexTo
     *            the upper index of the change range
     * @throws IndexOutOfBoundsException
     *             - indexFrom < 0 or indexTo > number of parent's children
     */
    public void remove(DefaultTreeNode<TopicItem> parent, int indexFrom, int indexTo) throws IndexOutOfBoundsException {
        DefaultTreeNode<TopicItem> stn = parent;
        for (int i = indexTo; i >= indexFrom; i--)
            try {
                stn.getChildren().remove(i);
            } catch (Exception exp) {
                exp.printStackTrace();
            }
    }
 
    public void remove(DefaultTreeNode<TopicItem> target) throws IndexOutOfBoundsException {
    	
    	// remove data from database
    	noteService.removeTopic(target.getData().getId());
    	
        int index = 0;
        DefaultTreeNode<TopicItem> parent = null;
        // find the parent and index of target
        parent = dfSearchParent(_root, target);
        for (index = 0; index < parent.getChildCount(); index++) {
            if (parent.getChildAt(index).equals(target)) {
                break;
            }
        }
        remove(parent, index, index);
    }
    
    /**
     * insert new nodes which parent is <code>parent</code> with indexes
     * <code>indexes</code> by new nodes <code>newNodes</code>
     * 
     * @param parent
     *            The parent of nodes are inserted
     * @param indexFrom
     *            the lower index of the change range
     * @param indexTo
     *            the upper index of the change range
     * @param newNodes
     *            New nodes which are inserted
     * @throws IndexOutOfBoundsException
     *             - indexFrom < 0 or indexTo > number of parent's children
     */
    public void insert(DefaultTreeNode<TopicItem> parent, int indexFrom, int indexTo, DefaultTreeNode<TopicItem>[] newNodes)
            throws IndexOutOfBoundsException {
        DefaultTreeNode<TopicItem> stn = parent;
        for (int i = indexFrom; i <= indexTo; i++) {
            try {
                stn.getChildren().add(i, newNodes[i - indexFrom]);
            } catch (Exception exp) {
                throw new IndexOutOfBoundsException("Out of bound: " + i + " while size=" + stn.getChildren().size());
            }
        }
    }
    
    @SuppressWarnings("unchecked")
	public void insertAtLast(DefaultTreeNode<TopicItem> parent) throws IndexOutOfBoundsException {
    	
    	long pid = parent.getData().getId();
    	
    	// save to database
		NoteNode note = noteService.addTopic(pid, TOPIC_NEW_ITEM, TOPIC_NEWLY_ADDED_CONTENT, QUtils.getRandomIconURL());
		
    	int index = parent.getChildCount() - 1;
		if (parent instanceof TopicItemTreeNode) {
            insert((TopicItemTreeNode)parent, index, index,
                    new DefaultTreeNode[] { new TopicItemTreeNode(new TopicItem(note), null, true) });
    	}
        
    }
    
    @SuppressWarnings("unchecked")
	public void insertAt(DefaultTreeNode<TopicItem> parent, int position) throws IndexOutOfBoundsException {
    	
    	long pid = parent.getData().getId();
    	
    	// save to database
		NoteNode note = noteService.addTopic(pid, TOPIC_NEW_ITEM, TOPIC_NEWLY_ADDED_CONTENT, QUtils.getRandomIconURL());
		
		if (parent instanceof TopicItemTreeNode) {
            insert((TopicItemTreeNode)parent, position, position,
                    new DefaultTreeNode[] { new TopicItemTreeNode(new TopicItem(note), null, true) });
    	}
        
    }
    
    public void add(TreeNode<TopicItem> child) {
		super.addOpenObject(child);
	}
    
    /**
     * append new nodes which parent is <code>parent</code> by new nodes
     * <code>newNodes</code>
     * 
     * @param parent
     *            The parent of nodes are appended
     * @param newNodes
     *            New nodes which are appended
     */
    public void add(DefaultTreeNode<TopicItem> parent, DefaultTreeNode<TopicItem>[] newNodes) {
        DefaultTreeNode<TopicItem> stn = (DefaultTreeNode<TopicItem>) parent;
 
        for (int i = 0; i < newNodes.length; i++)
            stn.getChildren().add(newNodes[i]);
    }
    
    @SuppressWarnings("unchecked")
	public void addToNode(TopicItemTreeNode node) {
    	// save to database
    	NoteNode note = noteService.addTopic(node.getData().getId(), TOPIC_NEW_ITEM, TOPIC_NEWLY_ADDED_CONTENT, QUtils.getRandomIconURL());
		
        add(node, new DefaultTreeNode[] { new TopicItemTreeNode(new TopicItem(note), null, true) });
    }
    
    public void addToRoot() {
    	// save to database
    	NoteNode note = noteService.addTopic(getRoot().getData().getId(), TOPIC_NEW_ITEM, TOPIC_NEWLY_ADDED_CONTENT, QUtils.getRandomIconURL());
    	
    	getRoot().add(new TopicItemTreeNode(new TopicItem(note), null, true));
    }
    
    private DefaultTreeNode<TopicItem> dfSearchParent(DefaultTreeNode<TopicItem> node, DefaultTreeNode<TopicItem> target) {
        if (node.getChildren() != null && node.getChildren().contains(target)) {
            return node;
        } else {
            int size = getChildCount(node);
            for (int i = 0; i < size; i++) {
                DefaultTreeNode<TopicItem> parent = dfSearchParent((DefaultTreeNode<TopicItem>) getChild(node, i), target);
                if (parent != null) {
                    return parent;
                }
            }
        }
        return null;
    }
 
}
