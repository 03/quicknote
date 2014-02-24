package com.wind.quicknote.controller;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.wind.quicknote.helper.QUtils;
import com.wind.quicknote.system.CookieUtil;
import com.wind.quicknote.system.UserCredentialManager;

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
			Executions.getCurrent().sendRedirect(QUtils.URL_HOME_PAGE);
		}
		nameTxb.setFocus(true);
		
		String userName = CookieUtil.getLastUserIfExists();
		if(userName != null) {
			nameTxb.setValue(userName);
		}

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
		
		if(!QUtils.login(nameTxb.getValue(), passwordTxb.getValue())){
			Clients.evalJavaScript("loginFailed()");
			mesgLbl.setValue("The Username or Password is invalid!");
		}
	}

}
