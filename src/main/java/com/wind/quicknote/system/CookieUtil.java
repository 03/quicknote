package com.wind.quicknote.system;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.zkoss.zk.ui.Executions;

/**
 * 
 *  http://forum.zkoss.org/question/43055/use-of-cookies/
 */
public class CookieUtil {

	public static void setCookie(String name, String value) {
		
		Cookie cookie = new Cookie(name, value);
		// setMaxAge of Cookie in seconds
		//cookie.setMaxAge(900);
		
		((HttpServletResponse) Executions.getCurrent().getNativeResponse())
				.addCookie(cookie);
	}

	public static String getCookie(String name) {
		
		Cookie[] cookies = ((HttpServletRequest) Executions.getCurrent()
				.getNativeRequest()).getCookies();
		
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(name)) {
					return cookie.getValue();
				}
			}
		}
		
		return null;
	}

	public static String getLastUserIfExists() {
		
		Cookie[] cookies = ((HttpServletRequest) Executions.getCurrent()
				.getNativeRequest()).getCookies();
		
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("lastUserName")) {
					return cookie.getValue();
				}
			}
		}
		
		return null;
	}
}
