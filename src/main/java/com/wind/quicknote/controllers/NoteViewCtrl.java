package com.wind.quicknote.controllers;

import java.util.List;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.ListModelList;

import com.wind.quicknote.models.NoteNode;
import com.wind.quicknote.services.NoteService;
import com.wind.quicknote.views.tree.TopicItem;

/**
 * 
 *         This is the controller for the Note view as referenced in
 *         note.zul
 *         
 *         http://books.zkoss.org/wiki/ZK_Essentials/Chapter_9:_Spring_Integration
 *         http://books.zkoss.org/wiki/ZK_Spring_Essentials/Working_with_ZK_Spring/Working_with_ZK_Spring_Core/Using_Spring_Variable_Resolver
 * 
 */

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class NoteViewCtrl extends SelectorComposer<Div> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6048147508374332396L;

	@Wire
	private Grid topicsGrid;
	
	@WireVariable
	private NoteService noteService;

	@Override
	public void doAfterCompose(Div comp) throws Exception {
		super.doAfterCompose(comp);

		List<NoteNode> items = noteService.findAllTopics();
		ListModelList<NoteNode> prodModel = new ListModelList<NoteNode>(items);
		topicsGrid.setModel(prodModel);
	}
	
	@Listen("onTopicSelect=#notetreeList")
	public void showTopicContent(Event fe) {

		if (!(fe.getTarget() instanceof NoteTreeList)) {
			return;
		}
		
		NoteTreeList item = (NoteTreeList) fe.getTarget();
		
		TopicItem currentNode = item.getCurrentNodeSelected();
		List<NoteNode> items = noteService.findChildTopics(currentNode.getId());
		ListModelList<NoteNode> prodModel = new ListModelList<NoteNode>(items);
		topicsGrid.setModel(prodModel);
		
		
       	//editor.setValue(currentNode.getContent());
		//BindUtils.postGlobalCommand(null, null, "updateEditor", null);
	}
}