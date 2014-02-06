package com.wind.quicknote.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Window;

import com.wind.quicknote.model.NoteUser;
import com.wind.quicknote.service.NoteService;
import com.wind.quicknote.system.UserCredentialManager;

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
			
			UserCredentialManager mgmt = UserCredentialManager.getIntance();
			mgmt.login(user.getLoginName(), user.getPassword());
			if (mgmt.isAuthenticated()) {
				// put it in session
				HttpSession hSess = (HttpSession) ((HttpServletRequest)Executions.getCurrent().getNativeRequest()).getSession();
				String userName = mgmt.getUser().getLoginName();
				hSess.setAttribute("user", userName);
				
				Executions.getCurrent().sendRedirect(LoginViewCtrl.URL_HOME_PAGE);
			}
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
