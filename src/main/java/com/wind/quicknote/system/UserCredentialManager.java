package com.wind.quicknote.system;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;

import com.wind.quicknote.helper.SpringBeanUtil;
import com.wind.quicknote.model.NoteUser;
import com.wind.quicknote.service.NoteService;

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
		if (tempUser != null && tempUser.getPassword().equals(password)) {
			user = tempUser;
		} else {
			user = null;
		}
	}

	public synchronized void logOff() {
		this.user = null;
	}

	public synchronized NoteUser getUser() {
		return user;
	}

	public synchronized boolean isAuthenticated() {
		return user != null;
	}

}
