package com.wind.quicknote.system;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;

import com.wind.quicknote.helper.SpringBeanUtil;
import com.wind.quicknote.model.NoteUser;
import com.wind.quicknote.service.NoteService;
import com.wind.quicknote.system.oauth2.OAuthSite;

/**
 * 
 */
public class UserCredentialManager {

	public static final String KEY_USER_MODEL = UserCredentialManager.class.getName() + "_MODEL";
	private NoteUser user;
	
	private static NoteService noteService = SpringBeanUtil.getBean("noteService", NoteService.class);

	private UserCredentialManager() {
	}

	public static UserCredentialManager getIntance() {
		Session session = Executions.getCurrent().getSession();
		synchronized (session) {
			UserCredentialManager userModel = (UserCredentialManager) session.getAttribute(KEY_USER_MODEL);
			if (userModel == null) {
				session.setAttribute(KEY_USER_MODEL,
						userModel = new UserCredentialManager());
			}
			return userModel;
		}
		
	}

	public synchronized void login(String name, String password) {
		NoteUser tempUser = noteService.findUserByName(name);

		if (tempUser != null && !StringUtils.isBlank(password)
				&& password.equals(tempUser.getPassword())) {
			user = tempUser;
			putUserOnline();
		} else {
			user = null;
		}

	}
	
	public synchronized void loginOAuth(String site, String userString) {

		JSONObject userInfo = new JSONObject(userString);
		
		String loginName = "";
		NoteUser tempUser = null;
		
		if (OAuthSite.FACEBOOK.getSiteName().equalsIgnoreCase(site)) {
			loginName = userInfo.getString("email");
			tempUser = noteService.findUserByName(loginName);
			if (tempUser == null) {
				// Gson gson = new Gson();
				// NoteUserDto dtoR = gson.fromJson(resourceBody, NoteUserDto.class);
				NoteUser noteUser = new NoteUser();
				noteUser.setLoginName(loginName);
				noteUser.setFirstName(userInfo.getString("first_name"));
				noteUser.setLastName(userInfo.getString("last_name"));
				noteUser.setEmail(loginName);
				tempUser = noteService.addUser(noteUser);
			}

		} else if (OAuthSite.GOOGLE.getSiteName().equalsIgnoreCase(site)) {
			loginName = userInfo.getString("email");
			tempUser = noteService.findUserByName(loginName);
			if (tempUser == null) {
				NoteUser noteUser = new NoteUser();
				noteUser.setLoginName(loginName);
				noteUser.setFirstName(userInfo.getString("given_name"));
				noteUser.setLastName(userInfo.getString("family_name"));
				noteUser.setEmail(loginName);
				tempUser = noteService.addUser(noteUser);
			}

		}

		user = tempUser;
		putUserOnline();

	}
	

	@SuppressWarnings("unchecked")
	public void putUserOnline() {

		List<NoteUser> userList = (List<NoteUser>) Executions.getCurrent().getDesktop().getWebApp().getAttribute("ONLINE_USER_LIST");
		if(userList == null) {
			userList = new ArrayList<NoteUser>();
			userList.add(user);
			Executions.getCurrent().getDesktop().getWebApp().setAttribute("ONLINE_USER_LIST", userList);
		} else {
			userList.add(user);
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public void putUserOffline() {
		
		List<NoteUser> userList = (List<NoteUser>) Executions.getCurrent().getDesktop().getWebApp().getAttribute("ONLINE_USER_LIST");
		if(userList == null) {
			userList = new ArrayList<NoteUser>();
			Executions.getCurrent().getDesktop().getWebApp().setAttribute("ONLINE_USER_LIST", userList);
		} else {
			userList.remove(user);
		}
		
	}

	public synchronized void logOff() {
		putUserOffline();
		
		// add to cookie on logOff
		CookieUtil.setCookie("lastUserName", user.getLoginName());
		this.user = null;
	}

	public synchronized NoteUser getUser() {
		return user;
	}

	public synchronized boolean isAuthenticated() {
		return user != null;
	}

}
