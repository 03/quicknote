package com.wind.quicknote.controller;

import static com.wind.quicknote.helper.QUtils.DIALOG_WARNING;

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

import com.wind.quicknote.view.tree.TopicItem;
import com.wind.quicknote.view.tree.TopicItemTreeNode;
import com.wind.quicknote.view.tree.TopicTreeModel;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class NoteTreeList extends Div implements IdSpace {

	private static final long serialVersionUID = -4656138582305427445L;
	private static Logger log = LoggerFactory.getLogger(NoteTreeList.class);
	
	@Wire
	private Vlayout pLayout;
	
	@Wire
	private Tree topicTree;
	
	private TopicTreeModel topicTreeModel;
	
	public NoteTreeList() {
		// 1. Render the template
		Executions.createComponents("/pages/noteTreeList.zul", this, null);
		
		// 2. Wire variables, components and event listeners (optional)
        Selectors.wireVariables(this, this, Selectors.newVariableResolvers(getClass(), null));
		Selectors.wireComponents(this, this, false);
		Selectors.wireEventListeners(this, this);
		
		refreshTopicTree();
	}
	
	// Model
	public TopicItem getCurrentItem() {
		TopicItemTreeNode node = getCurrentNode();
		if (node == null)
			return null;
		
		return node.getData();
	}
	
	public TopicItemTreeNode getCurrentNode() {
		Treeitem selectedItem = topicTree.getSelectedItem();
		if (selectedItem == null)
			return null;
		
		return (TopicItemTreeNode) selectedItem.getValue();
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
		
		topicTree.invalidate();
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
        		topicTreeModel.insertAtLast(parentNode);
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
	
	public void insertBefore() {
		
		TopicItemTreeNode selectedNode = getCurrentNode();
		TopicItemTreeNode parentNode = (TopicItemTreeNode) selectedNode.getParent();

		topicTreeModel.insertAt(parentNode, parentNode.getIndex(selectedNode));
   	}
	
	public void insertAfter() {
		
		TopicItemTreeNode selectedNode = getCurrentNode();
		TopicItemTreeNode parentNode = (TopicItemTreeNode) selectedNode.getParent();
		
		topicTreeModel.insertAt(parentNode, parentNode.getIndex(selectedNode) + 1);
   	}
	
	public void moveUp() {
		
		TopicItemTreeNode selectedNode = getCurrentNode();
		TopicItemTreeNode parentNode = (TopicItemTreeNode) selectedNode.getParent();

		int idx = parentNode.getIndex(selectedNode);
		topicTreeModel.swapPos(parentNode, idx, idx-1);
   	}
	
	public void moveDown() {
		
		TopicItemTreeNode selectedNode = getCurrentNode();
		TopicItemTreeNode parentNode = (TopicItemTreeNode) selectedNode.getParent();

		int idx = parentNode.getIndex(selectedNode);
		topicTreeModel.swapPos(parentNode, idx, idx+1);
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
    	
		if (getCurrentItem() != null) {

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
					// if current node has no children , append it as the first
					// child
					topicTreeModel.addToNode(selectedNode);
				} else {
					// if current node has children, add it as the last child
					topicTreeModel.insertAtLast(parentNode);
				}
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
    
    


	/**
	 * properties
	 */
	@Listen("onClick=#btnProp")
	public void showItemProperties() {
		log.debug("#Show Properties");
		TopicItem currentItem = getCurrentItem();
		if (currentItem != null) {
			Map<String, Object> myMap = new HashMap<String, Object>();
			myMap.put("topicItem", currentItem);

			final Window win = (Window) Executions.createComponents(
					"/pages/popup/noteProp.zul", null, myMap);
			win.setMaximizable(true);
			win.doModal();

		} else {
			showWarningDialog();
		}
	}
    

	/**
	 * removeItem
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Listen("onClick=#btnRemove")
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
			Messagebox.show("No node to delete, please create a new one.", DIALOG_WARNING, Messagebox.OK, Messagebox.EXCLAMATION);
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
        	Messagebox.show("Current node["+id+"]: "+currentItem.getName(), "Information", Messagebox.OK, Messagebox.INFORMATION);
        	
    	} else {
    		showWarningDialog();
    	}
	}
    
	/**
     * The structure of tree
     */
    private final class TopicTreeRenderer implements TreeitemRenderer<TopicItemTreeNode> {
    	
        private static final String HEIGHT_16PX = "16px";

		public void render(final Treeitem treeItem, TopicItemTreeNode treeNode, int index) throws Exception {
        	
        	TopicItemTreeNode ctn = treeNode;
        	TopicItem topicItem = (TopicItem) ctn.getData();
            final Treerow dataRow = new Treerow();
            dataRow.setParent(treeItem);
            treeItem.setValue(ctn);
            treeItem.setOpen(ctn.isOpen());
 
            Treecell treeCell = new Treecell();
        	Hlayout hlayout = new Hlayout();
        	hlayout.setSclass("h-inline-block");
        	
            final Image image = new Image(topicItem.getIcon());
            image.setWidth(HEIGHT_16PX);
            image.setHeight(HEIGHT_16PX);
            hlayout.appendChild(image);
            
            final Label label = new Label(topicItem.getName());
            label.setVisible(true);
            
            final Textbox tbox = new Textbox(topicItem.getName());
            tbox.setVisible(false);
            
            hlayout.appendChild(label);
            hlayout.appendChild(tbox);
            
            // Popup Context Menu
            Menupopup popup = createMenuPopup(topicItem, image, label, tbox);
            hlayout.appendChild(popup);
            
            // CtrlKeys doesn't seem to work for non-input gadgets, a bug?
            /*label.setCtrlKeys("#f2");
            label.addEventListener(Events.ON_CTRL_KEY, new EventListener<Event>() {
                @Override
                public void onEvent(Event event) throws Exception {
                	
                	int keyCode = ((KeyEvent) event).getKeyCode();
                	if(keyCode == 113) {
                		startRename(label, tbox);
                	}
                }
            });*/
            
            tbox.addEventListener(Events.ON_BLUR, new EventListener<Event>() {
                @Override
                public void onEvent(Event event) throws Exception {
                	endRename(label, tbox);
                }
            });
            
            tbox.addEventListener(Events.ON_OK, new EventListener<Event>() {
                @Override
                public void onEvent(Event event) throws Exception {
                	endRename(label, tbox);
                }
            });
            
            treeCell.setWidth("100%");
        	treeCell.appendChild(hlayout);
            dataRow.appendChild(treeCell);
            dataRow.setContext(popup);
            dataRow.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
                @Override
                public void onEvent(Event event) throws Exception {
                	Events.postEvent(new Event("onTopicSelect", NoteTreeList.this));
                }
            });
            
            dataRow.addEventListener(Events.ON_DOUBLE_CLICK, new EventListener<Event>() {
                @Override
                public void onEvent(Event event) throws Exception {
                	startRename(label, tbox);
                }
            });
            
            // items can be dragged and dropped
            dataRow.setDraggable("true");
            dataRow.setDroppable("true");
            dataRow.addEventListener(Events.ON_DROP, new EventListener<Event>() {

            	@Override
                public void onEvent(Event event) throws Exception {
                	/*
                	 * treeItem is the current node
                	 * parentItem is current node's parent
                	 * draggedItem is the node to be added (dragged target is a TreeRow)
                	 */
                    Treeitem draggedItem = (Treeitem) ((DropEvent) event).getDragged().getParent();
                    topicTreeModel.moveToNode( (TopicItemTreeNode) treeItem.getValue(), (TopicItemTreeNode) draggedItem.getValue());
                }
            });
            
        }

		/**
         * Create pop-up menu
         * 
         * Add Before
         * Add After
         * Add Child
         * ---------------
         * Move Up | Down
         * ---------------
         * Delete
         * ---------------
         * Icon
         * Rename
         * Properties
		 * @param topicItem 
         * 
         */
		private Menupopup createMenuPopup(final TopicItem topicItem, final Image image, final Label label, final Textbox tbox) {
			
			Menupopup popup = new Menupopup();
			
			/*
			 * Add Item
			 */
            Menuitem itemAddAfter = new Menuitem("Add After");
            itemAddAfter.setImage("/assets/images/add.jpg");
            itemAddAfter.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
                @Override
                public void onEvent(Event event) throws Exception {
                	insertAfter();
                }
            });
            popup.appendChild(itemAddAfter);
            
            Menuitem itemAddBef = new Menuitem("Add Before");
            itemAddBef.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
                @Override
                public void onEvent(Event event) throws Exception {
                	insertBefore();
                }
            });
            popup.appendChild(itemAddBef);
            
            Menuitem itemAddChild = new Menuitem("Add Child");
            itemAddChild.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
                @Override
                public void onEvent(Event event) throws Exception {
                	insertChildItem();
                }
            });
            popup.appendChild(itemAddChild);
            
            // Menu separator
			popup.appendChild(new Menuseparator());
			
			/*
			 * Move Item
			 */
            Menu moveMenu = new Menu("Move ..");
            moveMenu.setImage("/assets/images/move.png");
            Menupopup movePopup = new Menupopup();
            Menuitem itemMoveUp = new Menuitem("Up");
            itemMoveUp.setImage("/assets/images/arrowUp.png");
            itemMoveUp.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
                @Override
                public void onEvent(Event event) throws Exception {
                	moveUp();
                }
            });
            movePopup.appendChild(itemMoveUp);
            
            Menuitem itemMoveDown = new Menuitem("Down");
            itemMoveDown.setImage("/assets/images/arrowDown.png");
            itemMoveDown.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
                @Override
                public void onEvent(Event event) throws Exception {
                	moveDown();
                }
            });
            movePopup.appendChild(itemMoveDown);
            
            moveMenu.appendChild(movePopup);
            popup.appendChild(moveMenu);
            
            popup.appendChild(new Menuseparator());
            
            /*
             * Delete
             */
            Menuitem itemDel = new Menuitem("Delete");
            itemDel.setImage("/assets/images/remove.jpg");
            itemDel.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
                @Override
                public void onEvent(Event event) throws Exception {
                	removeItem();
                }
            });
            popup.appendChild(itemDel);
            
            popup.appendChild(new Menuseparator());
            
            /*
             * Icons
             * http://emrpms.blogspot.com.au/2012/06/mvvm-modal-windowpass-parameter-and.html
             */
            Menuitem itemChangeIcon = new Menuitem("Icon");
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
             * Rename
             */
            Menuitem itemRenameIcon = new Menuitem("Rename");
            itemRenameIcon.setImage("/assets/images/rename.jpg");
            itemRenameIcon.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
                @Override
                public void onEvent(Event event) throws Exception {
                	startRename(label, tbox);
                }
            });
            popup.appendChild(itemRenameIcon);
            
            /*
             * Properties
             */
            Menuitem itemProp = new Menuitem("Properties");
            itemProp.setImage("/assets/images/prop.png");
            itemProp.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
                @Override
                public void onEvent(Event event) throws Exception {
                	showItemProperties();
                }
            });
            popup.appendChild(itemProp);
            
			return popup;
		}


		/*
		 * Rename methods
		 */
		private void startRename(final Label label, final Textbox tbox) {
			tbox.setValue(label.getValue());
			label.setVisible(false);
			tbox.setVisible(true);
			tbox.setFocus(true);
		}
        
		private void endRename(final Label label, final Textbox tbox) {
			String previousValue = label.getValue();
			String currentValue = tbox.getValue();
			if(!previousValue.equals(currentValue)) {
				label.setValue(currentValue);
				
				topicTreeModel.updateTopicName(getCurrentItem(), currentValue);
			}
			
			tbox.setVisible(false);
			label.setVisible(true);
		}
    }
 	
}
