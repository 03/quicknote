package com.wind.quicknote.helper;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.Clients;

import com.wind.quicknote.model.UserRole;
import com.wind.quicknote.model.UserStatus;

public class QUtils {

	public static final String DIALOG_WARNING = "Warning";
	private static final String alphabet = "abcdefghijklmnopqrstuvwxyz";
	private static String[] samples = {"Ada","Basic","C","C++","CSS","Cobol","Forth","Fortran",
        "Go","Groovy","Haskell","HTML","Java","JavaScript","Lisp","Python","Ruby",
        "Scala","Scheme"};

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
	
}
