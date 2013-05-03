package com.wind.quicknote.controllers;

import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.event.DropEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.DefaultTreeNode;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;
import org.zkoss.zul.Treerow;
import org.zkoss.zul.Vlayout;

import com.wind.quicknote.models.NoteNode;
import com.wind.quicknote.services.NoteService;
import com.wind.quicknote.views.tree.TopicItem;
import com.wind.quicknote.views.tree.TopicItemTreeNode;
import com.wind.quicknote.views.tree.TopicTreeModel;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class NoteTreeList extends Div implements IdSpace {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4656138582305427445L;
	
	@Wire
	private Vlayout pLayout;
	
	@Wire
	private Tree topicTree;
	
	@WireVariable
	private NoteService noteService;
	
	private TopicTreeModel topicTreeModel;
	
	/**
	 * Current Node of Topic
	 */
	private TopicItem currentItem = null;
	
	public NoteTreeList() {
		// 1. Render the template
		Executions.createComponents("/pages/noteTreeList.zul", this, null);
		
		// 2. Wire variables, components and event listeners (optional)
		//wire service manually by calling Selectors API
        Selectors.wireVariables(this, this, Selectors.newVariableResolvers(getClass(), null));
		Selectors.wireComponents(this, this, false);
		Selectors.wireEventListeners(this, this);
		
		refreshTopicTree();
	}

	/**
	 * Refresh Tree
	 * @param id setFocus
	 */
	public void refreshTopicTree() {
		NoteNode root = noteService.findRootTopic();
		topicTree.setItemRenderer(new TopicTreeRenderer());
		topicTreeModel = new TopicTreeModel(convertToTopicItemTreeNode(root));
		topicTree.setModel(topicTreeModel);
		currentItem = null;
		
		topicTree.setSizedByContent(true);
		// topicTree.invalidate();
	}
	
	private TopicItemTreeNode convertToTopicItemTreeNode(NoteNode node) {
		
		//TopicItem rootTopic = new TopicItem(root.getId(), root.getName(), root.getContent(), root.getIcon());
		TopicItem rootTopic = new TopicItem(node);

		if (node.hasChildren()) {
			
			List<NoteNode> notes = node.getChildren();
			int size = notes.size();

			TopicItemTreeNode[] array = new TopicItemTreeNode[size];
			for (int i = 0; i < size; i++) {
				NoteNode child = notes.get(i);
				array[i] = convertToTopicItemTreeNode(child);
			}

			return new TopicItemTreeNode(rootTopic, array);
			
		} else {

			return new TopicItemTreeNode(rootTopic, null, true);
		}

	}
	
	/**
     * 
     * findNoteById
     * @param tree
     * @param id
     * @return
     */
    public Treeitem findNoteById(Tree tree, long id) {
    	
    	Collection<Treeitem> items = tree.getItems();
    	for(Treeitem item : items) {
    		TopicItem currentNodeItem = ((TopicItemTreeNode) item.getValue()).getData();
    		if(currentNodeItem.getId() == id) {
    			return item;
    		}
    	}
    	
    	return null;
    	
    }
	
    /**
     * insertChildItem
     */
    @SuppressWarnings("unchecked")
	@Listen("onClick=#btninsert")
   	public void insertItem() {
    	
		if (currentItem != null) {

			// current item
			Treeitem selectedTreeItem = topicTree.getSelectedItem();
			// selectedTreeItem[UI] as parent node[ViewModel]
			TopicItemTreeNode selectedNode = (TopicItemTreeNode) selectedTreeItem.getValue();
			TopicItem selectedItem = selectedNode.getData();
			TopicItemTreeNode parentNode = (TopicItemTreeNode) selectedNode.getParent();
			TopicItem parentItem = parentNode.getData();
			
			Treeitem parentTreeItem = selectedTreeItem.getParentItem();
			if (parentTreeItem == null) {
        		// if first level node, add same level node
				
				// save to database
				NoteNode note = noteService.addTopic(parentItem.getId(), "[New Item]", "newly added content...", getRandomIconURL());
				// change currentItem to the newly added
				currentItem = new TopicItem(note);
				topicTreeModel.getRoot().add(new TopicItemTreeNode(currentItem, null, true));
				
        	} else {
        		
    			// save to database
        		NoteNode note = noteService.addTopic(selectedItem.getId(), "[New Item]", "newly added content...", getRandomIconURL());
    			// change currentItem to the newly added
    			currentItem = new TopicItem(note);
        		
				if (selectedItem.isLeaf()) {
                	// if current node has no children , append it as the first child
                	
        			// update model
                    topicTreeModel.add(selectedNode, new DefaultTreeNode[] { new TopicItemTreeNode(currentItem, null, true) });
                    
                } else { 
                	// if current node has children, add it as the last child
                	
                	int index = parentTreeItem.getTreechildren().getChildren().indexOf(selectedTreeItem);
					if (parentTreeItem.getValue() instanceof TopicItemTreeNode) {
                        topicTreeModel.insert((TopicItemTreeNode)parentTreeItem.getValue(), index, index,
                                new DefaultTreeNode[] { new TopicItemTreeNode(currentItem, null, true) });
                	}
                }
        		
        	}
            
		} else {

			int countOfChildren = topicTreeModel.getRoot().getChildCount();
			if (countOfChildren == 0) {
				// add new node
				
				// save to database
				NoteNode note = noteService.addTopic(topicTreeModel.getRoot().getData().getId(), "[New Item]", "newly added content :)", getRandomIconURL());
				// change currentItem to the newly added
				currentItem = new TopicItem(note);
				topicTreeModel.getRoot().add(new TopicItemTreeNode(currentItem, null, true));
				
			} else {
				
				Messagebox.show("Please select a node.", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
			}
			
		}
       	
   	}
    
    
    private String getRandomIconURL() {
		Random randomGenerator = new Random();
		int num = 1 + randomGenerator.nextInt(9);
		return "/assets/images/filetypes/ft" + num + ".gif";
	}

	/**
     * removeItem
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Listen("onClick=#btnremove")
	public void removeItem() {
    	
		if (currentItem != null) {
			
			Messagebox.show("Are you sure to delete?", "Confirm Deletion", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener() {

				public void onEvent(Event event) throws Exception {
					if (((Integer) event.getData()).intValue() == Messagebox.OK) {
						DefaultTreeNode<TopicItem> node = (TopicItemTreeNode)topicTree.getSelectedItem().getValue();
						topicTreeModel.remove(node);
				        // remove data from database
						noteService.removeTopic(node.getData().getId());
						
						//change currentNode to null;
						currentItem = null;
						
						return;
					} else {
						System.out.println("Messagebox.CANCEL selected!");
						return;
					}	
				}
				
			});
			
		} else {
			Messagebox.show("Please select a node.", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
		}
	}
    
    /**
     * renameItem
     */
	@Listen("onClick=#btnname")
	public void renameItem() {
    	
		if (currentItem != null) {
			
			//Treeitem selectedTreeItem = topicTree.getSelectedItem();
			//...
			
		} else {
			Messagebox.show("Please select a node.", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
		}
	}
    
    /**
     * currentItem
     */
    @Listen("onClick=#btnshow")
	public void currentItem() {
    	
    	if(currentItem != null) {
    		long id = currentItem.getId();
        	Messagebox.show("current topic id["+id+"] name:"+currentItem.getName(), "Information", Messagebox.OK, Messagebox.INFORMATION);
    	} else {
    		Messagebox.show("Please select a node.", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
    	}
    	
	}
    
	/**
     * The structure of tree
     */
    private final class TopicTreeRenderer implements TreeitemRenderer<TopicItemTreeNode> {
        //@Override
        public void render(final Treeitem treeItem, TopicItemTreeNode treeNode, int index) throws Exception {
        	TopicItemTreeNode ctn = treeNode;
        	TopicItem topicItem = (TopicItem) ctn.getData();
            Treerow dataRow = new Treerow();
            dataRow.setParent(treeItem);
            treeItem.setValue(ctn);
            treeItem.setOpen(ctn.isOpen());
 
            Treecell treeCell = new Treecell();
        	Hlayout hlayout = new Hlayout();
            Image image = new Image(topicItem.getIcon());
            image.setWidth("20px");
            image.setHeight("20px");
            hlayout.appendChild(image);
            
            final Label label = new Label(topicItem.getName());
            label.setVisible(true);
            
            final Textbox tbox = new Textbox(topicItem.getName());
            tbox.setVisible(false);
            
            hlayout.appendChild(label);
            hlayout.appendChild(tbox);
            
            label.addEventListener(Events.ON_DOUBLE_CLICK, new EventListener<Event>() {
                @Override
                public void onEvent(Event event) throws Exception {

                	tbox.setValue(label.getValue());
                	label.setVisible(false);
                	tbox.setVisible(true);
                	tbox.setFocus(true);
                }
            });
            
            
            tbox.addEventListener(Events.ON_BLUR, new EventListener<Event>() {
                @Override
                public void onEvent(Event event) throws Exception {

                	String previousValue = label.getValue();
                	String currentValue = tbox.getValue();
                	if(!previousValue.equals(currentValue)) {
                		label.setValue(currentValue);
                		noteService.updateTopicName(currentItem.getId(), tbox.getValue());
                	}
                	
                	tbox.setVisible(false);
                	label.setVisible(true);
                	
                }
            });
            
            hlayout.setSclass("h-inline-block");
        	treeCell.appendChild(hlayout);
            dataRow.appendChild(treeCell);
            
            dataRow.setDraggable("true");
            dataRow.appendChild(treeCell);
            dataRow.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
                @Override
                public void onEvent(Event event) throws Exception {
                    TopicItemTreeNode clickedNodeValue = (TopicItemTreeNode) ((Treeitem) event.getTarget().getParent()).getValue();
                    
                    currentItem = (TopicItem) clickedNodeValue.getData();
                    Events.postEvent(new TopicSelectEvent());
                }
            });
            
            // Both category row and contact row can be item dropped
            dataRow.setDroppable("true");
            dataRow.addEventListener(Events.ON_DROP, new EventListener<Event>() {
                @SuppressWarnings("unchecked")
                @Override
                public void onEvent(Event event) throws Exception {
                    // The dragged target is a TreeRow
                	/*
                	 * treeItem is the current node
                	 * parentItem is current node's parent
                	 * draggedItem is the node to be add as either child or sibling node
                	 */
                	long parentid = 0;
                    Treeitem draggedItem = (Treeitem) ((DropEvent) event).getDragged().getParent();
                    TopicItemTreeNode draggedValue = (TopicItemTreeNode) draggedItem.getValue();
                    Treeitem parentItem = treeItem.getParentItem();
                    
                    topicTreeModel.remove(draggedValue);
                    if (((TopicItem) ((TopicItemTreeNode)treeItem.getValue()).getData()).isLeaf()) { 
                    	// if current node has no children, append it as the first child
                        topicTreeModel.add((TopicItemTreeNode) treeItem.getValue(),
                                new DefaultTreeNode[] { draggedValue });
                        
                        parentid = ((TopicItemTreeNode) treeItem.getValue()).getData().getId();
                        		
                    } else { 
                    	// if current node has children, add it as the last child 
                        int index = parentItem.getTreechildren().getChildren().indexOf(treeItem);
                        if(parentItem.getValue() instanceof TopicItemTreeNode) {
                            topicTreeModel.insert((TopicItemTreeNode)parentItem.getValue(), index, index,
                                    new DefaultTreeNode[] { draggedValue });
                        }
                        
                        parentid = ((TopicItemTreeNode) parentItem.getValue()).getData().getId();
                    }
                    
                    // save to database - change parent id of draggedItem
                    noteService.changeParentId(draggedValue.getData().getId(), parentid);
                    
                }
            });
 
        }
        
    }
    
 
    public TopicItem getCurrentNodeSelected() {
		return currentItem;
	}

	public void setCurrentNodeSelected(TopicItem currentNodeSelected) {
		this.currentItem = currentNodeSelected;
	}

	// Customise Event
 	public static final String ON_TOPIC_SELECT = "onTopicSelect";

 	public class TopicSelectEvent extends Event {

 		private static final long serialVersionUID = 7547668136120826171L;

 		public TopicSelectEvent() {
 			super(ON_TOPIC_SELECT, NoteTreeList.this);
 		}
 	}
    
}
