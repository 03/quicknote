package com.wind.quicknote.system.oauth2;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;

public class OAuthTokenServlet extends HttpServlet {

	private static final long serialVersionUID = -4344231559606900244L;

	public void init() throws ServletException {}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String type = request.getRequestURI().substring(
				request.getContextPath().length());
		String code = request.getParameter("code");
		if(StringUtils.isBlank(code)) {
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.println("<h2>Login was unsuccessful!</h2>");
			return;
		}
		
		try {
			
			String resourceBody = "";
			String site = "";
			
			if (type.contains(OAuthSite.FACEBOOK.getSiteName())) {
				
				site = OAuthSite.FACEBOOK.getSiteName();
				resourceBody = OAuthClientFacebook.accessResource(OAuthClientGoogle.getAccessToken(code));
				/* {
				    "id": "7654321",
				    "first_name": "W",
				    "gender": "female",
				    "last_name": "PY",
				    "link": "https:\/\/www.facebook.com\/app_scoped_user_id\/7654321\/",
				    "locale": "en_US",
				    "name": "W PY",
				    "timezone": 10,
				    "updated_time": "2014-06-04T05:16:15+0000",
				    "verified": false
				} */
				
				// sendRedirect is for testing purpose only, use forward instead
				// response.sendRedirect("/qnote/login.zul?email="+email);
				
			} else if (type.contains(OAuthSite.GOOGLE.getSiteName())) {
				
				site = OAuthSite.GOOGLE.getSiteName();
				resourceBody = OAuthClientGoogle.accessResource(OAuthClientGoogle.getAccessToken(code));
				/* {
					 "id": "1234567",
					 "email": "bxf@gmail.com",
					 "verified_email": true,
					 "name": "BX F",
					 "given_name": "BX",
					 "family_name": "F",
					 "link": "https://plus.google.com/1234567",
					 "picture": "https://lh3.googleusercontent.com/XXXXX/4252rscbv5M/photo.jpg",
					 "gender": "male",
					 "locale": "en"
				} */
				
			} else {
				// Others
			}
			
			/**
			 * TODO: use site to further distinguish user with same email address
			 */
			request.getSession().setAttribute("oauthsite", site);
			request.getSession().setAttribute("oauthuser", resourceBody);
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/login.zul");
			dispatcher.forward(request,response);
			
		} catch (OAuthSystemException e) {
			e.printStackTrace();
		} catch (OAuthProblemException e) {
			e.printStackTrace();
		}

	}

	public void destroy() {
		// do nothing.
	}
}