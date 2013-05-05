package com.wind.quicknote.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.wind.quicknote.systems.UserCredentialManager;


/**
 * 
 */
public class LoginViewCtrl extends SelectorComposer<Window> {

	private static final long serialVersionUID = 5730426085235946339L;
	
	@Wire
	private Window loginWin;
	
	@Wire
	private Textbox nameTxb, passwordTxb;
	
	@Wire
	private Label mesgLbl;

	@Override
	public void doAfterCompose(Window comp) throws Exception {
		super.doAfterCompose(comp);
		loginWin.doHighlighted();
		if (UserCredentialManager.getIntance().isAuthenticated()) {
			Executions.getCurrent().sendRedirect("/pages/note.zul");
		}
		nameTxb.setFocus(true);

	}
	
	@Listen("onOK=#passwordTxb")
	public void onOK() {
		doLogin();
	}
	
	@Listen("onClick=#confirmBtn")
	public void confirm() {
		doLogin();
	}

	private void doLogin() {
		UserCredentialManager mgmt = UserCredentialManager.getIntance();
		mgmt.login(nameTxb.getValue(), passwordTxb.getValue());
		if (mgmt.isAuthenticated()) {
			// put it in session
			HttpSession hSess = (HttpSession) ((HttpServletRequest)Executions.getCurrent().getNativeRequest()).getSession();
			String userName = mgmt.getUser().getName();
			hSess.setAttribute("user", userName);
			
			Executions.getCurrent().sendRedirect("/pages/note.zul");
			/*if("didev".equals(userName)) {
				Executions.getCurrent().sendRedirect("/pages/note.zul");
			}*/
			
		} else {
			
			Clients.evalJavaScript("loginFaild()");
			mesgLbl.setValue("The Username or Password is invalid!");
			
		}
	}

}
