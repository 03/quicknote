package com.wind.quicknote.controllers;

import static com.wind.quicknote.helpers.QUtils.DIALOG_WARNING;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	private static Logger log = LoggerFactory.getLogger(NoteTreeList.class);
	
	@Wire
	private Vlayout pLayout;
	
	@Wire
	private Tree topicTree;
	
	@WireVariable
	private NoteService noteService;
	
	private TopicTreeModel topicTreeModel;
	
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
	
	// Model
	public TopicItem getCurrentItem() {
		Treeitem selectedItem = topicTree.getSelectedItem();
		if (selectedItem == null || selectedItem.getValue() == null)
			return null;
		return ((TopicItemTreeNode) selectedItem.getValue()).getData();
	}

	/**
	 * Refresh Tree
	 * @param id setFocus
	 */
	public void refreshTopicTree() {
		
		topicTree.setSizedByContent(true);
		topicTree.setItemRenderer(new TopicTreeRenderer());
		topicTreeModel = TopicTreeModel.getInstance();
		topicTree.setModel(topicTreeModel);
		
		//Events.postEvent(new TopicInitEvent());
		// topicTree.invalidate();
		
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
    
    private void showWarningDialog() {
		Messagebox.show("Please select a node.", DIALOG_WARNING, Messagebox.OK, Messagebox.EXCLAMATION);
	}
	
	@Listen("onClick=#btnInsert")
   	public void insertItem() {
		
		log.debug("#insertItem");
    	TopicItem currentItem = getCurrentItem();
		if (currentItem != null) {

			// current item
			Treeitem selectedTreeItem = topicTree.getSelectedItem();
			TopicItemTreeNode selectedNode = (TopicItemTreeNode) selectedTreeItem.getValue();
			TopicItemTreeNode parentNode = (TopicItemTreeNode) selectedNode.getParent();
			
			Treeitem parentTreeItem = selectedTreeItem.getParentItem();
			if (parentTreeItem == null) {
				topicTreeModel.addToRoot();
        	} else {
        		topicTreeModel.insertAtLastToNode(parentNode);
        	}
            
		} else {

			if (topicTreeModel.getRoot().getChildCount() == 0) {
				// add new node
				topicTreeModel.addToRoot();
				
			} else {
				showWarningDialog();
			}
			
		}
       	
   	}
	
	@Listen("onClick=#btnInsertChild")
   	public void insertChildItem() {
		
		log.debug("#insertChildItem");
    	TopicItem currentItem = getCurrentItem();
		if (currentItem != null) {

			// current item
			Treeitem selectedTreeItem = topicTree.getSelectedItem();
			TopicItemTreeNode selectedNode = (TopicItemTreeNode) selectedTreeItem.getValue();
			topicTreeModel.addToNode(selectedNode);
            
		} else {

			if (topicTreeModel.getRoot().getChildCount() == 0) {
				// add new node
				topicTreeModel.addToRoot();
				
			} else {
				showWarningDialog();
			}
			
		}
       	
   	}
	
	/**
     * smart insert
     */
	@Listen("onClick=#btnSmartInsert")
   	public void smartInsertItem() {
    	
    	TopicItem currentItem = getCurrentItem();
		if (currentItem != null) {

			// current item
			Treeitem selectedTreeItem = topicTree.getSelectedItem();
			// selectedTreeItem[UI] as parent node[ViewModel]
			TopicItemTreeNode selectedNode = (TopicItemTreeNode) selectedTreeItem.getValue();
			TopicItem selectedItem = selectedNode.getData();
			TopicItemTreeNode parentNode = (TopicItemTreeNode) selectedNode.getParent();
			
			Treeitem parentTreeItem = selectedTreeItem.getParentItem();
			if (parentTreeItem == null) {
        		// if on top level, add node on same level
				topicTreeModel.addToRoot();
				
        	} else {
        		
				if (selectedItem.isLeaf()) {
                	// if current node has no children , append it as the first child
                    topicTreeModel.addToNode(selectedNode);
                    
                } else { 
                	// if current node has children, add it as the last child
                	topicTreeModel.insertAtLastToNode(parentNode);
                	
                }
        		
        	}
            
		} else {

			int countOfChildren = topicTreeModel.getRoot().getChildCount();
			if (countOfChildren == 0) {
				// add new node
				topicTreeModel.addToRoot();
				
			} else {
				showWarningDialog();
			}
			
		}
       	
   	}
    
    
	/**
     * removeItem
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Listen("onClick=#btnremove")
	public void removeItem() {
    	
    	TopicItem currentItem = getCurrentItem();
    	
		if (currentItem != null) {
			
			Messagebox.show("Delete it?", "Confirm", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener() {

				public void onEvent(Event event) throws Exception {
					if (((Integer) event.getData()).intValue() == Messagebox.OK) {
						DefaultTreeNode<TopicItem> node = (TopicItemTreeNode)topicTree.getSelectedItem().getValue();
						topicTreeModel.remove(node);
						return;
						
					} else {
						log.debug("Messagebox.CANCEL selected!");
						return;
					}	
				}
				
			});
			
		} else {
			Messagebox.show("No node to delete, try create a new one.", DIALOG_WARNING, Messagebox.OK, Messagebox.EXCLAMATION);
		}
	}
    
    /**
     * renameItem
     */
	@Listen("onClick=#btnname")
	public void renameItem() {
    	
		TopicItem currentItem = getCurrentItem();
		
		if (currentItem != null) {
			
			//Treeitem selectedTreeItem = topicTree.getSelectedItem();
			//...
			
		} else {
			showWarningDialog();
		}
	}
    
    /**
     * currentItem
     */
    @Listen("onClick=#btnshow")
	public void currentItem() {
    	
    	TopicItem currentItem = getCurrentItem();
    	
    	if(currentItem != null) {
    		long id = currentItem.getId();
        	Messagebox.show("current topic id["+id+"] name:"+currentItem.getName(), "Information", Messagebox.OK, Messagebox.INFORMATION);
        	
        	Events.postEvent(new TopicInitEvent());
    	} else {
    		//topicTree.getTreechildren().getFirstChild();
    		showWarningDialog();
    	}
	}
    
	/**
     * The structure of tree
     */
    private final class TopicTreeRenderer implements TreeitemRenderer<TopicItemTreeNode> {
    	
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
                		noteService.updateTopicName( getCurrentItem().getId(), tbox.getValue());
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
                	Events.postEvent(new TopicSelectEvent());
                }
            });
            
            dataRow.addEventListener(Events.ON_OPEN, new EventListener<Event>() {
                @Override
                public void onEvent(Event event) throws Exception {
                    TopicItemTreeNode clickedNodeValue = (TopicItemTreeNode) ((Treeitem) event.getTarget().getParent()).getValue();
                    
                    TopicItem selectedItem = (TopicItem) clickedNodeValue.getData();
                    log.debug("OPEN: "+selectedItem.getName());
                }
            });
            
            dataRow.addEventListener(Events.ON_CLOSE, new EventListener<Event>() {
                @Override
                public void onEvent(Event event) throws Exception {
                    TopicItemTreeNode clickedNodeValue = (TopicItemTreeNode) ((Treeitem) event.getTarget().getParent()).getValue();
                    
                    TopicItem selectedItem = (TopicItem) clickedNodeValue.getData();
                    log.debug("CLOSE: "+selectedItem.getName());
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
            itemAddAfter.setImage("/assets/images/add.jpg");
            itemAddAfter.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
                @Override
                public void onEvent(Event event) throws Exception {

                	log.debug(" ");
                }
            });
            popup.appendChild(itemAddAfter);
            
            Menuitem itemAddBef = new Menuitem("Add Item Before");
            itemAddBef.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
                @Override
                public void onEvent(Event event) throws Exception {

                	log.debug(" ");
                }
            });
            popup.appendChild(itemAddBef);
            
            Menuitem itemAddChild = new Menuitem("Add Child Item");
            itemAddChild.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
                @Override
                public void onEvent(Event event) throws Exception {

                	log.debug(" ");
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

                	log.debug(" ");
                }
            });
            movePopup.appendChild(itemMoveUp);
            
            Menuitem itemMoveDown = new Menuitem("Down");
            itemMoveDown.setImage("/assets/images/arrowDown.png");
            itemMoveDown.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
                @Override
                public void onEvent(Event event) throws Exception {

                	log.debug(" ");
                }
            });
            movePopup.appendChild(itemMoveDown);
            
            Menuitem itemMoveLeft = new Menuitem("Left");
            itemMoveLeft.setImage("/assets/images/arrowLeft.png");
            itemMoveLeft.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
                @Override
                public void onEvent(Event event) throws Exception {

                	log.debug(" ");
                }
            });
            movePopup.appendChild(itemMoveLeft);
            
            Menuitem itemMoveRight = new Menuitem("Right");
            itemMoveRight.setImage("/assets/images/arrowRight.png");
            itemMoveRight.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
                @Override
                public void onEvent(Event event) throws Exception {

                	log.debug(" ");
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

                	log.debug("Properties");
                }
            });
            popup.appendChild(itemProp);
            
			return popup;
		}
        
    }
    
	// Customise Event
 	public static final String ON_TOPIC_SELECT = "onTopicSelect";

 	public class TopicSelectEvent extends Event {

 		private static final long serialVersionUID = 7547668136120826171L;

 		public TopicSelectEvent() {
 			super(ON_TOPIC_SELECT, NoteTreeList.this);
 		}
 	}
 	
 	// Customise Event
  	public class TopicInitEvent extends Event {

  		private static final long serialVersionUID = 7547668136120826171L;

  		public TopicInitEvent() {
  			super("onTopicInit", NoteTreeList.this);
  		}
  	}
    
}
