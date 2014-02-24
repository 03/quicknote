package com.wind.quicknote.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Window;

import com.wind.quicknote.helper.QUtils;
import com.wind.quicknote.model.NoteUser;
import com.wind.quicknote.service.NoteService;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class SignupVM {

	private static Logger log = LoggerFactory.getLogger(SignupVM.class);
	
	@WireVariable
	private NoteService noteService;
	
	@Command
    public void submit(@BindingParam("cmp")  Window win) {
		
		log.debug("----------- Sumbit to create user -----------");
		NoteUser user = noteService.addUser(noteUser);
		if(user != null) {
			win.detach();
			QUtils.login(user.getLoginName(), user.getPassword());
		}
    }
	
	private NoteUser noteUser = new NoteUser();
	private String retypedPassword;

	public NoteUser getNoteUser() {
		return noteUser;
	}

	public void setNoteUser(NoteUser NoteUser) {
		this.noteUser = NoteUser;
	}

	public String getRetypedPassword() {
		return retypedPassword;
	}

	public void setRetypedPassword(String retypedPassword) {
		this.retypedPassword = retypedPassword;
	}

}
