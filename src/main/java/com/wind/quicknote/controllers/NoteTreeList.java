package com.wind.quicknote.controllers;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.zkoss.zul.Menu;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.Menuseparator;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;
import org.zkoss.zul.Treerow;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;

import com.wind.quicknote.models.NoteNode;
import com.wind.quicknote.services.NoteService;
import com.wind.quicknote.views.tree.TopicItem;
import com.wind.quicknote.views.tree.TopicItemTreeNode;
import com.wind.quicknote.views.tree.TopicTreeModel;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class NoteTreeList extends Div implements IdSpace {

	public static final String TOPIC_NEW_ITEM = "[New Item]";
	private static final String TOPIC_NEWLY_ADDED_CONTENT = "newly added content...";

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
				NoteNode note = noteService.addTopic(parentItem.getId(), TOPIC_NEW_ITEM, TOPIC_NEWLY_ADDED_CONTENT, getRandomIconURL());
				// change currentItem to the newly added
				currentItem = new TopicItem(note);
				topicTreeModel.getRoot().add(new TopicItemTreeNode(currentItem, null, true));
				
        	} else {
        		
				if (selectedItem.isLeaf()) {
                	// if current node has no children , append it as the first child
					
	    			// save to database
	        		NoteNode note = noteService.addTopic(selectedItem.getId(), TOPIC_NEW_ITEM, TOPIC_NEWLY_ADDED_CONTENT, getRandomIconURL());
	        		
					// change currentItem to the newly added
	    			currentItem = new TopicItem(note);
                	
        			// update model
                    topicTreeModel.add(selectedNode, new DefaultTreeNode[] { new TopicItemTreeNode(currentItem, null, true) });
                    
                } else { 
                	// if current node has children, add it as the last child
                	
        			// save to database
            		NoteNode note = noteService.addTopic(parentItem.getId(), TOPIC_NEW_ITEM, TOPIC_NEWLY_ADDED_CONTENT, getRandomIconURL());
            		
                	// change currentItem to the newly added
        			currentItem = new TopicItem(note);
                	
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
				NoteNode note = noteService.addTopic(topicTreeModel.getRoot().getData().getId(), TOPIC_NEW_ITEM, TOPIC_NEWLY_ADDED_CONTENT, getRandomIconURL());
				// change currentItem to the newly added
				currentItem = new TopicItem(note);
				topicTreeModel.getRoot().add(new TopicItemTreeNode(currentItem, null, true));
				
				//change currentNode to null;
				currentItem = null;
				
			} else {
				
				Messagebox.show("Please select a node.", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
			}
			
		}
       	
   	}
    
    
    private String getRandomIconURL() {
		Random randomGenerator = new Random();
		int num = 1 + randomGenerator.nextInt(12);
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
            final Image image = new Image(topicItem.getIcon());
            image.setWidth("20px");
            image.setHeight("20px");
            hlayout.appendChild(image);
            
            final Label label = new Label(topicItem.getName());
            label.setVisible(true);
            
            final Textbox tbox = new Textbox(topicItem.getName());
            tbox.setVisible(false);
            
            hlayout.appendChild(label);
            hlayout.appendChild(tbox);
            
            Menupopup popup = createMenuPopup(topicItem, image, label);
            label.setContext(popup);
            hlayout.appendChild(popup);
            
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
                    
                    TopicItem selectedItem = (TopicItem) clickedNodeValue.getData();
                    if(currentItem != selectedItem) {
                    	currentItem = selectedItem;
                        Events.postEvent(new TopicSelectEvent());
                    }
                    
                }
            });
            
            dataRow.addEventListener(Events.ON_OPEN, new EventListener<Event>() {
                @Override
                public void onEvent(Event event) throws Exception {
                    TopicItemTreeNode clickedNodeValue = (TopicItemTreeNode) ((Treeitem) event.getTarget().getParent()).getValue();
                    
                    TopicItem selectedItem = (TopicItem) clickedNodeValue.getData();
                    System.out.println("OPEN: "+selectedItem.getName());
                }
            });
            
            dataRow.addEventListener(Events.ON_CLOSE, new EventListener<Event>() {
                @Override
                public void onEvent(Event event) throws Exception {
                    TopicItemTreeNode clickedNodeValue = (TopicItemTreeNode) ((Treeitem) event.getTarget().getParent()).getValue();
                    
                    TopicItem selectedItem = (TopicItem) clickedNodeValue.getData();
                    System.out.println("CLOSE: "+selectedItem.getName());
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
                    TopicItemTreeNode currentTreeNode = treeItem.getValue();
                    
                    topicTreeModel.remove(draggedValue);
                    
                    if (((TopicItem) (currentTreeNode).getData()).isLeaf()) { 
                    	// if current node has no children, append it as the first child
                        topicTreeModel.add(currentTreeNode, new DefaultTreeNode[] { draggedValue });
                        
                        parentid = currentTreeNode.getData().getId();
                        		
                    } else {
                    	
                    	// if current node has children, add it as the last child 
                    	if (parentItem == null) {
                        	// first level node
                    		int index = currentTreeNode.getChildCount() - 1;
                    		topicTreeModel.insert(currentTreeNode, index, index, new DefaultTreeNode[] { draggedValue });
                    		
                    		parentid = currentTreeNode.getData().getId();
                        	
                        } else {
                        	
                        	TopicItemTreeNode parentTreeNode = parentItem.getValue();
                        	
                        	int index = parentItem.getTreechildren().getChildren().indexOf(treeItem);
                            if(parentTreeNode instanceof TopicItemTreeNode) {
                                topicTreeModel.insert(parentTreeNode, index, index,
                                        new DefaultTreeNode[] { draggedValue });
                            }
                            
                            parentid = parentTreeNode.getData().getId();
                            
                        }
                    	
                    }
                    
                    // save to database - change parent id of draggedItem
                    noteService.changeParentId(draggedValue.getData().getId(), parentid);
                    
                }
            });
 
        }

		/**
         * Add popup menu
         * 
         * Add Item Before
         * Add Item After
         * Add Child Item
         * ---------------
         * Move Item Up | Down | Left | Right
         * ---------------
         * Icons...
         * ---------------
         * Properties
		 * @param topicItem 
         * 
         */
		private Menupopup createMenuPopup(final TopicItem topicItem, final Image image, final Label label) {
			
			Menupopup popup = new Menupopup();
			
			/*
			 * Add Items
			 */
            Menuitem itemAddAfter = new Menuitem("Add Item After");
            itemAddAfter.setImage("/assets/images/add.png");
            itemAddAfter.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
                @Override
                public void onEvent(Event event) throws Exception {

                	System.out.println(" ");
                }
            });
            popup.appendChild(itemAddAfter);
            
            Menuitem itemAddBef = new Menuitem("Add Item Before");
            itemAddBef.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
                @Override
                public void onEvent(Event event) throws Exception {

                	System.out.println(" ");
                }
            });
            popup.appendChild(itemAddBef);
            
            Menuitem itemAddChild = new Menuitem("Add Child Item");
            itemAddChild.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
                @Override
                public void onEvent(Event event) throws Exception {

                	System.out.println(" ");
                }
            });
            popup.appendChild(itemAddChild);
            
            // Menuseparator
			popup.appendChild(new Menuseparator());
			
			/*
			 * Move Items
			 */
            Menu moveMenu = new Menu("Move Item ..");
            moveMenu.setImage("/assets/images/move.png");
            Menupopup movePopup = new Menupopup();
            Menuitem itemMoveUp = new Menuitem("Up");
            itemMoveUp.setImage("/assets/images/arrowUp.png");
            itemMoveUp.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
                @Override
                public void onEvent(Event event) throws Exception {

                	System.out.println(" ");
                }
            });
            movePopup.appendChild(itemMoveUp);
            
            Menuitem itemMoveDown = new Menuitem("Down");
            itemMoveDown.setImage("/assets/images/arrowDown.png");
            itemMoveDown.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
                @Override
                public void onEvent(Event event) throws Exception {

                	System.out.println(" ");
                }
            });
            movePopup.appendChild(itemMoveDown);
            
            Menuitem itemMoveLeft = new Menuitem("Left");
            itemMoveLeft.setImage("/assets/images/arrowLeft.png");
            itemMoveLeft.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
                @Override
                public void onEvent(Event event) throws Exception {

                	System.out.println(" ");
                }
            });
            movePopup.appendChild(itemMoveLeft);
            
            Menuitem itemMoveRight = new Menuitem("Right");
            itemMoveRight.setImage("/assets/images/arrowRight.png");
            itemMoveRight.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
                @Override
                public void onEvent(Event event) throws Exception {

                	System.out.println(" ");
                }
            });
            movePopup.appendChild(itemMoveRight);
            
            
            moveMenu.appendChild(movePopup);
            popup.appendChild(moveMenu);
            
            // Menuseparator
            popup.appendChild(new Menuseparator());
            
            /*
             * Icons
             * 
             * http://emrpms.blogspot.com.au/2012/06/mvvm-modal-windowpass-parameter-and.html
             * 
             */
            Menuitem itemChangeIcon = new Menuitem("Change Icon");
            itemChangeIcon.setImage("/assets/images/changeIcon.png");
            itemChangeIcon.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
                @Override
                public void onEvent(Event event) throws Exception {

                	// open new window to choose icon
                	Map<String, Object> myMap = new HashMap<String, Object>();
                	myMap.put("topicItem", topicItem);
                	myMap.put("image", image);
                	
                	final Window win = (Window) Executions.createComponents(
							"/pages/popup/iconChooser.zul", null, myMap);
					win.setMaximizable(true);
					win.doModal();
                }
            });
            popup.appendChild(itemChangeIcon);
            
            /*
             * Properties
             */
            Menuitem itemProp = new Menuitem("Properties");
            itemProp.setImage("/assets/images/prop.png");
            itemProp.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
                @Override
                public void onEvent(Event event) throws Exception {

                	System.out.println("Properties");
                }
            });
            popup.appendChild(itemProp);
            
			return popup;
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
