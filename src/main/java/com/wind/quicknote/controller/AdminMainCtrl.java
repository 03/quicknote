package com.wind.quicknote.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Label;
import org.zkoss.zul.Window;

import com.wind.quicknote.model.NoteUser;
import com.wind.quicknote.service.NoteService;
import com.wind.quicknote.system.UserCredentialManager;

/**
 * @author Luke
 * 
 */
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class AdminMainCtrl extends SelectorComposer<Window> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1044877626292900465L;

	private static Logger log = LoggerFactory.getLogger(AdminMainCtrl.class);

	@WireVariable
	private NoteService noteService;

	@Wire
	private Label loginUsrName;

	@Override
	public void doAfterCompose(Window comp) throws Exception {
		super.doAfterCompose(comp);

		UserCredentialManager mgmt = UserCredentialManager.getIntance();
		NoteUser user = mgmt.getUser();
		if (user != null) {
			String name = user.getFirstName() + " " + user.getLastName();
			String role = user.getRole().name();
			loginUsrName.setValue(name + " (" + role + ")");
		} else {
			loginUsrName.setValue("Guest");
		}

	}

	@Listen("onClick=#logoutBtn")
	public void confirm() {
		log.debug(loginUsrName + " logout.");
		doLogout();
	}

	private void doLogout() {

		UserCredentialManager mgmt = UserCredentialManager.getIntance();
		if (mgmt.isAuthenticated()) {
			// remove it from session
			HttpSession hSess = (HttpSession) ((HttpServletRequest) Executions
					.getCurrent().getNativeRequest()).getSession();
			hSess.removeAttribute("user");
		}

		mgmt.logOff();
		Executions.getCurrent().sendRedirect("/login.zul");

	}

}
