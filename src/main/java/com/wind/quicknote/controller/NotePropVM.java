package com.wind.quicknote.controller;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import com.wind.quicknote.model.NoteNode;
import com.wind.quicknote.service.NoteService;
import com.wind.quicknote.view.tree.TopicItem;

/**
 * @author Luke
 * 
 */

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class NotePropVM {

	@WireVariable
	private NoteService noteService;
	
	private NoteNode node;
	
	public NoteNode getNode() {
		return node;
	}

	public void setNode(NoteNode node) {
		this.node = node;
	}

	@AfterCompose
	public void init(@ExecutionArgParam("topicItem") TopicItem topicItem) {
		
		long id = topicItem.getId();
		node = noteService.findTopic(id);
	}
	
}


