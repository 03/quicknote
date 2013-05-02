package com.wind.quicknote.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.zkforge.ckez.CKeditor;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Label;
import org.zkoss.zul.Window;

import com.wind.quicknote.models.NoteUser;
import com.wind.quicknote.services.NoteService;
import com.wind.quicknote.systems.UserCredentialManager;
import com.wind.quicknote.views.tree.TopicItem;


/**
 * @author Luke
 * 
 */
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class NoteMainCtrl extends SelectorComposer<Window> {

	private static final long serialVersionUID = 5730426085235946339L;
	
	private TopicItem currentNode = null;
	
	@WireVariable
	private NoteService noteService;
	
	@Wire
	private Label loginUsrName;
	
	@Wire
	private CKeditor editor;
	
	@Override
	public void doAfterCompose(Window comp) throws Exception {
		super.doAfterCompose(comp);
		
		UserCredentialManager mgmt = UserCredentialManager.getIntance();
		NoteUser user = mgmt.getUser();
		if(user!=null) {
			String name = user.getName();
			loginUsrName.setValue(name);
		} else {
			loginUsrName.setValue("Guest");
		}
		
		/*
		 * http://www.zkoss.org/zkdemo/input/wysiwyg_editor
		 */
		editor.setCustomConfigurationsPath("/js/ckeditorcfg.js");
		//editor.setToolbar("MyToolbar");
		
	}
	
	@Listen("onClick=#logoutBtn")
	public void confirm() {
		doLogout();
	}

	private void doLogout() {

		UserCredentialManager mgmt = UserCredentialManager.getIntance();
		if (mgmt.isAuthenticated()) {
			// remove it from session
			HttpSession hSess = (HttpSession) ((HttpServletRequest)Executions.getCurrent().getNativeRequest()).getSession();
			hSess.removeAttribute("user");
		}
		
		mgmt.logOff();
		Executions.getCurrent().sendRedirect("/login.zul");
		
	}
	
	@Listen("onClick=#btnsave")
	public void updateContent() {
		long id = currentNode.getId();
		String text = editor.getValue();
		
		// Events.postEvent(this, new TopicSelectEvent());
		noteService.updateTopicText(id, text);
	}
	
	@Listen("onTopicSelect=#notetreeList")
	public void showTopicContent(Event fe) {

		if (!(fe.getTarget() instanceof NoteTreeList)) {
			return;
		}
		
		NoteTreeList item = (NoteTreeList) fe.getTarget();
		
		currentNode = item.getCurrentNodeSelected();
		String text = noteService.findTopicText(currentNode.getId());
       	editor.setValue(text);
       	//editor.setValue(currentNode.getContent());
		//BindUtils.postGlobalCommand(null, null, "updateEditor", null);
	}
	
	@GlobalCommand
	//@NotifyChange("topicsTree")
	public void updateEditor() {
		//no post processing to be done
	}
	
	
}

