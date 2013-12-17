package com.wind.quicknote.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Window;

import com.wind.quicknote.model.NoteUser;
import com.wind.quicknote.service.NoteService;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class SignupViewModel {

	private static Logger log = LoggerFactory.getLogger(SignupViewModel.class);
	
	@WireVariable
	private NoteService noteService;
	
	private int memoHeight = 6;

	public int getMemoHeight() {
		return memoHeight;
	}

	public void setMemoHeight(int memoHeight) {
		this.memoHeight = memoHeight;
	}

	@Command
	@NotifyChange("memoHeight")
	public void changeMemoHeight(
			@ContextParam(ContextType.TRIGGER_EVENT) InputEvent change) {
		try {
			int parsed = Integer.parseInt(change.getValue());
			if (parsed > 0) {
				this.memoHeight = parsed;
			}
		} catch (NumberFormatException nfe) {
			// nothing that we can do here, the validation should pick it up
		}
	}

	
	@Command
    public void submit(@BindingParam("cmp")  Window win) {
		
		log.debug("----------- Proceeding to create user -----------");
		noteService.addUser(noteUser);
		win.detach();
    }
	
	private NoteUser noteUser = new NoteUser();
	private String retypedPassword;
	//private String captcha = QUtils.getRandomString(4), captchaInput;

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
