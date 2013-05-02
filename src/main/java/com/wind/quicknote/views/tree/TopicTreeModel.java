package com.wind.quicknote.views.tree;

import org.zkoss.zul.DefaultTreeModel;
import org.zkoss.zul.DefaultTreeNode;
 
/*
 * http://forum.zkoss.org/question/81157/add-mvvm-model-to-spring-for-adding-transactional-annotation/
 * http://books.zkoss.org/wiki/ZK_Developer's_Reference/MVVM/Data_Binding
 */

public class TopicTreeModel extends DefaultTreeModel<TopicItem> {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 7973634536446973065L;
	
    DefaultTreeNode<TopicItem> _root;
    
    public TopicTreeModel(TopicItemTreeNode treeNode) {
        super(treeNode);
        _root = treeNode;
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
