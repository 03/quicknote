package com.wind.quicknote.controller;

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

import com.wind.quicknote.model.UserRole;
import com.wind.quicknote.system.CookieUtil;
import com.wind.quicknote.system.UserCredentialManager;


/**
 * 
 */
public class LoginViewCtrl extends SelectorComposer<Window> {

	public static final String URL_HOME_PAGE = "/pages/note.zul";
	public static final String URL_HOME_PAGE_ADMIN = "/pages/admin.zul";
	public static final String URL_HOME_PAGE_PREMIUM = "/pages/premium.zul";

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
			Executions.getCurrent().sendRedirect(URL_HOME_PAGE);
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
		UserCredentialManager mgmt = UserCredentialManager.getIntance();
		mgmt.login(nameTxb.getValue(), passwordTxb.getValue());
		if (mgmt.isAuthenticated()) {

			// put it in session
			HttpSession hSess = (HttpSession) ((HttpServletRequest) Executions
					.getCurrent().getNativeRequest()).getSession();
			String userName = mgmt.getUser().getLoginName();
			hSess.setAttribute("user", userName);
			
			// redirect by role
			UserRole role = mgmt.getUser().getRole();
			if (UserRole.Admin.equals(role)) {
				Executions.getCurrent().sendRedirect(URL_HOME_PAGE_ADMIN);
			} else if (UserRole.Premium.equals(role)) {
				Executions.getCurrent().sendRedirect(URL_HOME_PAGE_PREMIUM);
			} else if (UserRole.Standard.equals(role)) {
				// TODO: temporary login standard user to premium page
				Executions.getCurrent().sendRedirect(URL_HOME_PAGE_PREMIUM);
			} else {
				Executions.getCurrent().sendRedirect(URL_HOME_PAGE);
			}

		} else {

			Clients.evalJavaScript("loginFailed()");
			mesgLbl.setValue("The Username or Password is invalid!");
		}
	}

}
