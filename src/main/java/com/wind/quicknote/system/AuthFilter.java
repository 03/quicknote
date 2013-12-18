package com.wind.quicknote.system;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthFilter implements Filter {

	private static Logger log = LoggerFactory.getLogger(AuthFilter.class);
	
	public void destroy() {

	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		HttpSession hSess = ((HttpServletRequest) request).getSession();
		String attributeUser = (String) hSess.getAttribute("user");
		if(attributeUser != null) 
			log.debug("Current user -> "+attributeUser);
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		// String fullPath = httpRequest.getRequestURI(); // "/qnote/pages/note.zul"
		// String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length()); // "/pages/note.zul"
		// Get the IP address of client machine.
        // String ipAddress = request.getRemoteAddr();
		
		if (attributeUser == null) {
			//hSess.setAttribute("user", "fake");
			((HttpServletResponse) response).sendRedirect(httpRequest.getContextPath()+"/login.zul");
			
		} else {
			chain.doFilter(request, response);
		}

	}

	public void init(FilterConfig filterConfig) throws ServletException {
		
		//Get init parameter
        String testParam = filterConfig.getInitParameter("test-param");

        //Print the init parameter
        log.debug("init param: " + testParam);
	}

}
