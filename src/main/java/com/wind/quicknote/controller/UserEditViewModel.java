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
import com.wind.quicknote.system.UserCredentialManager;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class UserEditViewModel {

	private static Logger log = LoggerFactory.getLogger(UserEditViewModel.class);
	
	@WireVariable
	private NoteService noteService;
	
	@Command
    public void submit(@BindingParam("cmp")  Window component) {
		
		log.debug("----------- Proceeding to update user -----------");
		if(newPassword != null && !noteUser.getPassword().equals(newPassword))
			noteUser.setPassword(newPassword);
		
		noteService.updateUser(noteUser);
		
		QUtils.showClientInfo("Save successfully!", component);
    }
	
	private NoteUser noteUser = UserCredentialManager.getIntance().getUser();
	
	private String oldPassword;
	private String newPassword;
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

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	
}
