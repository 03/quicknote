package com.wind.quicknote.controller;

import java.io.IOException;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.wind.quicknote.system.oauth2.OAuthClientFacebook;
import com.wind.quicknote.system.oauth2.OAuthClientGoogle;

/**
 * 
 */
public class LoginViewCtrl extends SelectorComposer<Window> {

	private static final long serialVersionUID = 5730426085235946339L;
	private static Logger log = LoggerFactory.getLogger(LoginViewCtrl.class);
	
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

		// Get oauthuser (JSONString) from session
		// Executions.getCurrent().getParameter("oauthuser"); // HTTP Get
		HttpSession session = (HttpSession)(Executions.getCurrent()).getDesktop().getSession().getNativeSession();
		String oauthuser = (String) session.getAttribute("oauthuser");
		if (!StringUtils.isBlank(oauthuser)) {
			
			String oauthsite = (String) session.getAttribute("oauthsite");
			QUtils.loginOAuth(oauthsite, oauthuser);
		}
		
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
	
	@Listen("onClick=#signupFB")
	public void signupFB() {
		log.debug("Sign up with Facebook!");
		//Executions.sendRedirect("second.zul?username=TheName&password=ThePassword");
		try {
			Executions.sendRedirect(OAuthClientFacebook.getAuthorizationToken());
		} catch (OAuthSystemException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Listen("onClick=#signupGG")
	public void signupGG() {
		log.debug("Sign up with Google!");
		try {
			Executions.sendRedirect(OAuthClientGoogle.getAuthorizationToken());
		} catch (OAuthSystemException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void doLogin() {
		
		if(!QUtils.login(nameTxb.getValue(), passwordTxb.getValue())){
			Clients.evalJavaScript("loginFailed()");
			mesgLbl.setValue("The Username or Password is invalid!");
		}
	}

}
