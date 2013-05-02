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
	private TopicItem currentNode = null;
	
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
		currentNode = null;
		// topicTree.invalidate();
	}
	
	private TopicItemTreeNode convertToTopicItemTreeNode(NoteNode root) {
		
		TopicItem rootTopic = new TopicItem(root.getId(), root.getName(), root.getContent(), root.getIcon());

		if (root.hasChildren()) {
			rootTopic.setLeaf(false);
			
			List<NoteNode> notes = root.getChildren();
			int size = notes.size();

			TopicItemTreeNode[] array = new TopicItemTreeNode[size];
			for (int i = 0; i < size; i++) {
				NoteNode child = notes.get(i);
				array[i] = convertToTopicItemTreeNode(child);
			}

			return new TopicItemTreeNode(rootTopic, array);
			
		} else {

			rootTopic.setLeaf(true);
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
    	
		if (currentNode != null) {

			// current item
			Treeitem selectedTreeItem = topicTree.getSelectedItem();
			// selectedTreeItem[UI] as parent node[ViewModel]
			TopicItemTreeNode parent = (TopicItemTreeNode) selectedTreeItem.getValue();
			
			// save to database
			long newId = noteService.addTopic(parent.getData().getId(), "[New Item]", "newly added content...",	getRandomIconURL());
			// get newId set as Id
			TopicItem rootTopic = new TopicItem(newId, "[New Item]", "newly added content...", getRandomIconURL());
			// change currentNode to the newly added
			currentNode = rootTopic;
			
            if (((TopicItem) parent.getData()).isLeaf()) { 
            	// if current node has no children, append it as the first child
            	
    			// update model
                topicTreeModel.add(parent, new DefaultTreeNode[] { new TopicItemTreeNode(rootTopic, null, true) });
                
            } else { 
            	// if current node has children, add it as the last child 
            	
            	Treeitem parentItem = selectedTreeItem.getParentItem();
                int index = parentItem.getTreechildren().getChildren().indexOf(selectedTreeItem);
                if(parentItem.getValue() instanceof TopicItemTreeNode) {
                    topicTreeModel.insert((TopicItemTreeNode)parentItem.getValue(), index, index,
                            new DefaultTreeNode[] { new TopicItemTreeNode(rootTopic, null, true) });
                }
            }
            
		} else {
			
			Messagebox.show("Please select a node.", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
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
    	
		if (currentNode != null) {
			
			Messagebox.show("Are you sure to delete?", "Confirm Dialog", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener() {

				public void onEvent(Event event) throws Exception {
					if (((Integer) event.getData()).intValue() == Messagebox.OK) {
						DefaultTreeNode<TopicItem> node = (TopicItemTreeNode)topicTree.getSelectedItem().getValue();
						topicTreeModel.remove(node);
				        // remove data from database
						noteService.removeTopic(node.getData().getId());
						
						//change currentNode to null;
						currentNode = null;
						
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
    	
		if (currentNode != null) {
			
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
    	
    	if(currentNode != null) {
    		long id = currentNode.getId();
        	Messagebox.show("current topic id["+id+"] name:"+currentNode.getName(), "Information", Messagebox.OK, Messagebox.INFORMATION);
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
            Image image = new Image(topicItem.getProfilepic());
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
                		noteService.updateTopicName(currentNode.getId(), tbox.getValue());
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
                    
                    currentNode = (TopicItem) clickedNodeValue.getData();
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
		return currentNode;
	}

	public void setCurrentNodeSelected(TopicItem currentNodeSelected) {
		this.currentNode = currentNodeSelected;
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
