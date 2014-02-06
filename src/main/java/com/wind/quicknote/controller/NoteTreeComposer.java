package com.wind.quicknote.controller;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treeitem;

import com.wind.quicknote.view.tree.TopicItem;
import com.wind.quicknote.view.tree.TopicItemTreeNode;

public class NoteTreeComposer extends GenericForwardComposer<Tree> {
	
	private static Logger log = LoggerFactory.getLogger(NoteTreeComposer.class);

	/**
	 * https://code.google.com/p/zk-sample-code/source/browse/trunk/WebContent/samples/sam/combobox/setSelectedIndexOnAfterRender.zul?r=50
	 * <tree id="topicTree" onAfterRender="self.setSelectedItem(self.getTreechildren().getFirstChild())" />
	 */
	private static final long serialVersionUID = 8673381423570688852L;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void doAfterCompose(final Tree comp) throws Exception {
		super.doAfterCompose(comp);
		
		comp.addEventListener("onAfterRender", new EventListener(){

			@Override
			public void onEvent(Event event) throws Exception {
				Tree tree = (Tree) event.getTarget();
				
				Treeitem item = (Treeitem) tree.getTreechildren().getFirstChild();
				if(item == null) {
					// no children found, could be the case onTopicInitonTopicInitfor new user or user delete all its notes
					return;
				}
				tree.setSelectedItem(item);
				
				TopicItem currentNode = ((TopicItemTreeNode) item.getValue()).getData();
				log.debug("doAfterCompose currentNodeId: "+ currentNode.getId());
				
				EventQueues.lookup("myqueue1", EventQueues.SESSION, true).publish(new Event("onTopicInit", null, currentNode));
				
				
			}
		});
		
		EventQueues.lookup("myqueue2", EventQueues.SESSION, true).subscribe(
				new EventListener() {
					public void onEvent(Event evt) {
						Long id = (Long) evt.getData();
						log.debug("Event[onSelectSearchItem]? "+ evt.getName());

						Collection<Treeitem> col = comp.getItems();
						for (Treeitem item : col) {
							if (((TopicItemTreeNode) item.getValue()).getData()
									.getId() == id) {
								comp.setSelectedItem(item);
								comp.invalidate();
								break;
							}
						}
					}
				});
		
	}
	
}
