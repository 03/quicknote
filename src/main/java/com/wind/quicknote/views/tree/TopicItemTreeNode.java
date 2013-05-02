package com.wind.quicknote.views.tree;

import org.zkoss.zul.DefaultTreeNode;

public class TopicItemTreeNode extends DefaultTreeNode<TopicItem> {

	private static final long serialVersionUID = -7012663776755277499L;
    
    private boolean open = true;
    
    public TopicItemTreeNode(TopicItem data, boolean open) {
        super(data);
        setOpen(open);
    }
 
    public TopicItemTreeNode(TopicItem data, DefaultTreeNode<TopicItem>[] children) {
        super(data, children);
    }
 
    public TopicItemTreeNode(TopicItem data, DefaultTreeNode<TopicItem>[] children, boolean open) {
        super(data, children);
        setOpen(open);
    }
 
    public TopicItemTreeNode(TopicItem data) {
        super(data);
 
    }
 
    public boolean isOpen() {
        return open;
    }
 
    public void setOpen(boolean open) {
        this.open = open;
    }
}
