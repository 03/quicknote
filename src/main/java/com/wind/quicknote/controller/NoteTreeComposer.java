package com.wind.quicknote.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treeitem;

import com.wind.quicknote.views.tree.TopicItem;
import com.wind.quicknote.views.tree.TopicItemTreeNode;

public class NoteTreeComposer extends GenericForwardComposer<Tree> {
	
	private static Logger log = LoggerFactory.getLogger(NoteTreeComposer.class);

	/**
	 * https://code.google.com/p/zk-sample-code/source/browse/trunk/WebContent/samples/sam/combobox/setSelectedIndexOnAfterRender.zul?r=50
	 * <tree id="topicTree" onAfterRender="self.setSelectedItem(self.getTreechildren().getFirstChild())" />
	 */
	private static final long serialVersionUID = 8673381423570688852L;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void doAfterCompose(Tree comp) throws Exception {
		super.doAfterCompose(comp);
		
		comp.addEventListener("onAfterRender", new EventListener(){

			@Override
			public void onEvent(Event event) throws Exception {
				Tree tree = (Tree) event.getTarget();
				
				Treeitem item = (Treeitem) tree.getTreechildren().getFirstChild();
				tree.setSelectedItem(item);
				
				TopicItem currentNode = ((TopicItemTreeNode) item.getValue()).getData();
				log.debug("doAfterCompose currentNodeId: "+ currentNode.getId());
				
				EventQueues.lookup("myqueue1", EventQueues.APPLICATION, true).publish(new Event("onTopicInit", null, currentNode));
			}
		});
		
	}
	
}
