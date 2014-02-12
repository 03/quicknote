package com.wind.quicknote.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wind.quicknote.dao.NoteNodeDAO;
import com.wind.quicknote.dao.NoteUserDAO;
import com.wind.quicknote.model.NoteUser;
import com.wind.quicknote.model.NoteUserDto;

/**
 * Restful ws
 * 
 * http://www.dreamsyssoft.com/blog/blog.php?/archives/7-Simple-REST-Web-Service-in-Java-with-Spring-and-CXF.html
 * http://www.mkyong.com/webservices/jax-rs/jax-rs-pathparam-example/
 * 
 */
@Path("/json/note")
@Consumes("application/json")
@Produces("application/json")
@Transactional
@Service("noteServiceRS")
public class NoteServiceRSImpl implements NoteServiceRS {
	
	private static Logger log = LoggerFactory.getLogger(NoteServiceRSImpl.class);
	
	@Autowired
	@Qualifier("noteUserDAO")
	private NoteUserDAO userDAO;
	
	@Autowired
	@Qualifier("noteNodeDAO")
	private NoteNodeDAO nodeDAO;
	
	@GET
	@Path("/echo/{msg}")
	public String echo(@PathParam("msg") String message) {
		log.debug("echo");
		return "echo: " + message;
	}
	
	@GET
	@Path("/secho")
	public String silentEcho() {
		log.debug("silent echo");
		return "silent echo - testing";
	}

	@GET
	@Path("/user/{name}")
	public NoteUserDto findUserByName(@PathParam("name") String name) {
		
		NoteUserDto dto = new NoteUserDto();
		NoteUser model = userDAO.findByName(name);
		if(model != null) {
			BeanUtils.copyProperties(model, dto);
		}
		
		return dto;
	}

	@POST
	@Path("/adduser/")
	public NoteUserDto addUser(NoteUserDto user) {
		
		NoteUser model = userDAO.createUser(user.getLoginName(), user.getEmail(), user.getPassword());
		if(model != null) {
			BeanUtils.copyProperties(model, user);
		}
		
		return user;
	}
	
}
