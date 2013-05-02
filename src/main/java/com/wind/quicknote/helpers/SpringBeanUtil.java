package com.wind.quicknote.helpers;

import javax.servlet.http.HttpSession;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.zkoss.zk.ui.Sessions;

public class SpringBeanUtil {

	public static Object getBean(String beanName) {
		
		HttpSession session = (HttpSession) Sessions.getCurrent().getNativeSession();
        WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(session.getServletContext());
        //ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		return ctx.getBean(beanName);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String beanName, Class<T> className) {
		
		HttpSession session = (HttpSession) Sessions.getCurrent().getNativeSession();
        WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(session.getServletContext());
		return (T) ctx.getBean(beanName);
	}

}
