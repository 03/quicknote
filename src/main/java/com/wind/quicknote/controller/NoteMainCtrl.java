package com.wind.quicknote.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkforge.ckez.CKeditor;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Window;

import com.wind.quicknote.helper.QUtils;
import com.wind.quicknote.model.NoteNode;
import com.wind.quicknote.model.NoteUser;
import com.wind.quicknote.service.NoteService;
import com.wind.quicknote.system.SessionCacheManager;
import com.wind.quicknote.system.UserCredentialManager;
import com.wind.quicknote.view.tree.TopicItem;


/**
 * @author Luke
 * 
 */
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class NoteMainCtrl extends SelectorComposer<Window> {

	private static final long serialVersionUID = 5730426085235946339L;
	private static Logger log = LoggerFactory.getLogger(NoteMainCtrl.class);
	
	private long currentNodeId = 0;
	
	@WireVariable
	private NoteService noteService;
	
	@Wire
	private Label loginUsrName;
	
	@Wire
	private CKeditor editor;
	
	@Wire
	private Combobox searchBar;
	
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void doAfterCompose(Window comp) throws Exception {
		super.doAfterCompose(comp);
		
		UserCredentialManager mgmt = UserCredentialManager.getIntance();
		NoteUser user = mgmt.getUser();
		if(user!=null) {
			String name = user.getFirstName() + " " + user.getLastName();
			String role = user.getRole().name();
			loginUsrName.setValue(name + " (" + role + ")");
		} else {
			loginUsrName.setValue("Guest");
		}
		
		/*
		 * http://www.zkoss.org/zkdemo/input/wysiwyg_editor
		 * http://books.zkoss.org/wiki/ZK_Developer's_Reference/Event_Handling/Event_Queues
		 */
		//editor.setCustomConfigurationsPath("/js/ckeditorcfg.js");
		//editor.setToolbar("Full");
		
		EventQueues.lookup("myqueue1", EventQueues.SESSION, true)
		.subscribe(new EventListener() {
			
			public void onEvent(Event evt) {
				if(!"onTopicInit".equals(evt.getName())) {
					return;
				}
				TopicItem item = (TopicItem) evt.getData();
				log.info("I've got this!! " + item.getId());

				currentNodeId = item.getId();
				NoteNode node = SessionCacheManager.get(currentNodeId);
				if(node == null)
				{
					node = noteService.findTopic(currentNodeId);
					SessionCacheManager.put(currentNodeId, node);
				}
				editor.setValue(node.getText());
			}
		});

		searchBar.setModel(new SimpleListModel(QUtils.getSampleData()));
		
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
		
		String text = editor.getValue();
		
		// update cache
		SessionCacheManager.get(currentNodeId).setText(text);
		noteService.updateTopicText(currentNodeId, text);
		
		QUtils.showClientInfo("Save successfully!", editor);
	}

	/**
	 * show content when select an item in tree
	 */
	@Listen("onTopicSelect=#notetreeList")
	public void showTopicContent(Event fe) {

		if (!(fe.getTarget() instanceof NoteTreeList)) {
			return;
		}

		NoteTreeList item = (NoteTreeList) fe.getTarget();
		currentNodeId = item.getCurrentItem().getId();
		
		// search in cache first
		NoteNode node = SessionCacheManager.get(currentNodeId);
		if(node == null)
		{
			node = noteService.findTopic(currentNodeId);
			SessionCacheManager.put(currentNodeId, node);
		}
		
		editor.setValue(node.getText());
	}
	
	
	/**
	 * show content when select an item in list
	 */
	@Listen("onTopicItemSelect=#topicList")
	public void showTopicItemContent(Event fe) {

		if (!(fe.getTarget() instanceof Listbox)) {
			return;
		}
		
		Listitem item = ((Listbox) fe.getTarget()).getSelectedItem();
		
		NoteNode selectedNode = (NoteNode) item.getValue();
		currentNodeId = selectedNode.getId();
		editor.setValue(selectedNode.getText());
	}
	
}

