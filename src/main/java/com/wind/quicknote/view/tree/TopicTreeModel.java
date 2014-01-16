package com.wind.quicknote.view.tree;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zul.DefaultTreeModel;
import org.zkoss.zul.DefaultTreeNode;
import org.zkoss.zul.TreeNode;

import com.wind.quicknote.helper.QUtils;
import com.wind.quicknote.helper.SpringBeanUtil;
import com.wind.quicknote.model.NoteNode;
import com.wind.quicknote.model.NoteUser;
import com.wind.quicknote.service.NoteService;
import com.wind.quicknote.system.UserCredentialManager;
 
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
	
	private static NoteService noteService = SpringBeanUtil.getBean("noteService", NoteService.class);
	
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
    
	public void insertBefore(DefaultTreeNode<TopicItem> parent, int position) throws IndexOutOfBoundsException {
    	
    	long pid = parent.getData().getId();
    	// save to database
		NoteNode note = noteService.addTopic(pid, position - 1, TOPIC_NEW_ITEM, TOPIC_NEWLY_ADDED_CONTENT, QUtils.getRandomIconURL());
		
		if (parent instanceof TopicItemTreeNode) {
			parent.getChildren().add(position, new TopicItemTreeNode(new TopicItem(note), null, true));
    	}
    }
	
	public void insertAfter(DefaultTreeNode<TopicItem> parent, int position) throws IndexOutOfBoundsException {
    	
    	long pid = parent.getData().getId();
    	// save to database
		NoteNode note = noteService.addTopic(pid, position, TOPIC_NEW_ITEM, TOPIC_NEWLY_ADDED_CONTENT, QUtils.getRandomIconURL());
		
		if (parent instanceof TopicItemTreeNode) {
			parent.getChildren().add(position + 1, new TopicItemTreeNode(new TopicItem(note), null, true));
    	}
    }
    
	public void swap(DefaultTreeNode<TopicItem> parent, int position, int newPosition)
            throws IndexOutOfBoundsException {
        DefaultTreeNode<TopicItem> stn = parent;
        
        int count = stn.getChildCount();
		if (newPosition < 0 || newPosition >= count)
			return;
        
		TreeNode<TopicItem> elem = stn.getChildAt(position);
		TreeNode<TopicItem> newElem = stn.getChildAt(newPosition);
		
		if (position > newPosition) {
			// move up
			stn.remove(position);
			stn.getChildren().add(newPosition, elem);
			
		} else {
			// move down
			stn.remove(newPosition);
			stn.getChildren().add(position, newElem);
		}
		
    }
    
	/**
	 * Swap positions on parent
	 * 
	 * @param parent
	 * @param pos
	 * @param newPos
	 * @throws IndexOutOfBoundsException
	 */
	public void swapPos(DefaultTreeNode<TopicItem> parent, int pos, int newPos) {
    	
    	// save to database
		long pid = parent.getData().getId();
		noteService.swapTopicPosition(pid, pos, newPos);
		
		if (parent instanceof TopicItemTreeNode) {
            swap((TopicItemTreeNode)parent, pos, newPos);
    	}
        
    }
	
	
	public void updateTopicName(TopicItem topicItem, String value) {
		
		noteService.updateTopicName(topicItem.getId(), value);
		topicItem.setName(value);
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
    
    /**
     * Add as last child to node
     * 
     * @param node
     */
	public void addToNode(TopicItemTreeNode node) {
    	// save to database
    	NoteNode note_added = noteService.addTopic(node.getData().getId(), TOPIC_NEW_ITEM, TOPIC_NEWLY_ADDED_CONTENT, QUtils.getRandomIconURL());
        node.getChildren().add(new TopicItemTreeNode(new TopicItem(note_added), null, true));
    }
    
    /**
     * move node as last child to the new parent
     * 
     * @param parent
     * @param nodeTobeAdded
     */
	public void moveToNode(TopicItemTreeNode parent, TopicItemTreeNode nodeTobeAdded) {
		
		// cancel if on current parent
		if(nodeTobeAdded.getParent().getData().getId() == parent.getData().getId()) {
			return;
		}
		
    	// change parent
    	noteService.changeParentId(nodeTobeAdded.getData().getId(), parent.getData().getId());
        parent.getChildren().add(nodeTobeAdded);
    }
    
    public void addToRoot() {
    	// save to database
    	NoteNode note_added = noteService.addTopic(getRoot().getData().getId(), TOPIC_NEW_ITEM, TOPIC_NEWLY_ADDED_CONTENT, QUtils.getRandomIconURL());
    	
    	getRoot().add(new TopicItemTreeNode(new TopicItem(note_added), null, true));
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
