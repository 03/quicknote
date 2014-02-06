package com.wind.quicknote.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkforge.ckez.CKeditor;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Window;

import com.wind.quicknote.helper.QUtils;
import com.wind.quicknote.model.NoteNode;
import com.wind.quicknote.model.NoteUser;
import com.wind.quicknote.service.NoteService;
import com.wind.quicknote.system.UserCredentialManager;
import com.wind.quicknote.view.tree.TopicItem;

/**
 * @author Luke
 * 
 */
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class PremiumMainCtrl extends SelectorComposer<Window> {

	private static final long serialVersionUID = 5730426085235946339L;
	private static Logger log = LoggerFactory.getLogger(PremiumMainCtrl.class);
	
	private long currentNodeId = 0;
	
	@WireVariable
	private NoteService noteService;
	
	@Wire
	private Label loginUsrName;
	
	@Wire
	private Label notePath;
	
	@Wire
	private CKeditor editor;
	
	@Wire
	private Listbox filterbox;
	
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
						NoteNode node = noteService.findTopic(currentNodeId);
						notePath.setValue(node.getPath());
						editor.setValue(node.getText());
					}
				});
		
		List<NoteNode> list = noteService.findAllTopicsByUser(user.getId());
		filterbox.setModel(new SimpleListModel(list.toArray()));
	}
	
	@Listen("onClick=#logoutBtn")
	public void confirm() {
		doLogout();
	}
	
	@Listen("onSelect=#filterbox")
	// Zul page example: onSelect="searchBar.value=self.selectedItem.value.name; searchBar.close();"
	public void changeNote(Event fe) {
		
		if (!(fe.getTarget() instanceof Listbox)) {
			return;
		}
		
		Listitem item = ((Listbox) fe.getTarget()).getSelectedItem();
		
		NoteNode selectedNode = (NoteNode) item.getValue();
		currentNodeId = selectedNode.getId();
		notePath.setValue(selectedNode.getPath());
		editor.setValue(selectedNode.getText());
		
		EventQueues.lookup("myqueue2", EventQueues.SESSION, true).publish(new Event("onSelectSearchItem", null, currentNodeId));
	}
	
	
	/*
	 * http://forum.zkoss.org/question/12670/get-onchanging-value-in-java/
	 * Should use InputEvent instead of Event
	 * 
	 * Same effect as: searchBar.addEventListener(Events.ON_CHANGING, new EventListener<Event>() { ...
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Listen("onChanging=#searchBar")
	public void changeSelected(InputEvent fe) {
		String keyword = fe.getValue();
		
		log.debug("onChanging=#searchBar: oldVal->"
				+ ((Bandbox) fe.getTarget()).getValue() + " newVal->" + keyword);
		
		// refresh list
		List<NoteNode> list = noteService.findMatchedTopicsByUser( UserCredentialManager.getIntance().getUser().getId(), keyword);
		filterbox.setModel(new SimpleListModel(list.toArray()));
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
		
		NoteNode node = noteService.findTopic(currentNodeId);
		editor.setValue(node.getText());
		notePath.setValue(node.getPath());
	}
	
	
}

