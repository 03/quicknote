package com.wind.quicknote.controller.admin;

import java.util.List;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Div;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;

import com.wind.quicknote.model.NoteUser;
import com.wind.quicknote.service.NoteService;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class UserViewCtrl extends SelectorComposer<Div> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6048147508374332396L;

	@Wire
	private Listbox userList;
	
	@WireVariable
	private NoteService noteService;

	@Override
	public void doAfterCompose(Div comp) throws Exception {
		super.doAfterCompose(comp);

		List<NoteUser> items = noteService.findAllUsers();
		ListModelList<NoteUser> prodModel = new ListModelList<NoteUser>(items);
		userList.setModel(prodModel);
		
		userList.addEventListener(Events.ON_SELECT, new EventListener<Event>() {
            @Override
            public void onEvent(Event event) throws Exception {

            	// send event to show content in editor
            	//Events.postEvent(new TopicItemSelectEvent());
            }
        });
		
	}
	
}
