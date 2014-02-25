package com.wind.quicknote.helper;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.Clients;

import com.wind.quicknote.model.UserRole;
import com.wind.quicknote.model.UserStatus;
import com.wind.quicknote.system.UserCredentialManager;

public class QUtils {

	// Constants
	public static final String TOPIC_NEW_ITEM = "[New]";
	public static final String TOPIC_NEW_CONTENT = "";
	
	public static final String DIALOG_WARNING = "Warning";
	private static final String alphabet = "abcdefghijklmnopqrstuvwxyz";
	private static String[] samples = { "Ada", "Basic", "C", "C++", "Cobol",
			"Fortran", "Groovy", "Java", "JavaScript", "Lisp", "Python",
			"Ruby", "Scala" };

	public static String getRandomIconURL() {
		Random randomGenerator = new Random();
		int num = 1 + randomGenerator.nextInt(12);
		return "/assets/images/filetypes/ft" + num + ".gif";
	}
	
	public static String getRandomString(int length) {
		
		if(length <= 0)
			throw new IllegalArgumentException("Length cannot be less than or equal to 0");
		
		Random random = new Random();
		StringBuilder sb = new StringBuilder(length);
		
		for(int i=0; i<length; i++) {
			sb.append(alphabet.charAt(random.nextInt(alphabet.length())));
		}
		
		return sb.toString();
	}
	
	public static String[] getSampleData() {
		return samples;
	}
	
	private static List<UserStatus> userStatusList = Arrays.asList(UserStatus.values());;
	public static List<UserStatus> getUserStatusList() {
        return userStatusList;
	}
	
	private static List<UserRole> userRoleList = Arrays.asList(UserRole.values());;
	public static List<UserRole> getUserRoleList() {
		return userRoleList;
	}
	
	/*
	 * http://www.zkoss.org/javadoc/latest/zk/org/zkoss/zk/ui/util/Clients.html
	 * http://books.zkoss.org/wiki/ZK_Component_Reference/Essential_Components/Popup
	 */
	public static void showClientInfo(String message, Component component) {
		Clients.showNotification(message,"info",component,"at_pointer",2000);
	}
	
	public static void showClientWarning(String message, Component component) {
		Clients.showNotification(message,"warning",component,"at_pointer",2000);
	}
	
	public static void showClientError(String message, Component component) {
		Clients.showNotification(message,"error",component,"at_pointer",2000);
	}
	
	public static final String URL_HOME_PAGE = "/pages/note.zul";
	public static final String URL_HOME_PAGE_ADMIN = "/pages/admin.zul";
	public static final String URL_HOME_PAGE_PREMIUM = "/pages/premium.zul";
	
	public static boolean login(String name, String password) {
		UserCredentialManager mgmt = UserCredentialManager.getIntance();
		mgmt.login(name, password);
		if (mgmt.isAuthenticated()) {
			// put it in session
			HttpSession hSess = (HttpSession) ((HttpServletRequest) Executions.getCurrent().getNativeRequest()).getSession();
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

			return true;
		}
		
		return false;
	}
	
}
