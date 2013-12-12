package com.wind.quicknote.controller;

import java.util.HashMap;
import java.util.Map;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Window;

import com.wind.quicknote.service.NoteService;
import com.wind.quicknote.view.tree.TopicItem;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class NoteMainVM {

	@WireVariable
	private NoteService noteService;
	
    @Command
    public void showModelWin()
    {
        Map<String, String> myMap = new HashMap<String, String>();
    	myMap.put("currentImageSrc", "/assets/images/filetypes/ft5.gif");
    	final Window win = (Window) Executions.createComponents(
				"/pages/popup/iconChooser.zul", null, myMap);
		win.setMaximizable(true);
		win.doModal();
    }
    
    @GlobalCommand
    public void updateTopicIconCommand(@BindingParam("topicItem") TopicItem topicItem)
    {
    	// save to database
    	noteService.updateTopicIcon(topicItem.getId(), topicItem.getIcon());
    }
    
}
