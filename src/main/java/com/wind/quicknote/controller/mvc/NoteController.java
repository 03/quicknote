package com.wind.quicknote.controller.mvc;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wind.quicknote.model.NoteNodeDto;
import com.wind.quicknote.model.NoteUser;
import com.wind.quicknote.service.NoteService;

/*
 * 
 * http://www.mkyong.com/spring-mvc/spring-3-mvc-and-json-example/
 * http://www.mkyong.com/spring-mvc/spring-3-mvc-contentnegotiatingviewresolver-example/
 * http://www.gethifi.com/blog/browser-rest-http-accept-headers
 * 
 */
@Controller
public class NoteController {
	
	private static Logger log = LoggerFactory.getLogger(NoteController.class);
	
	@Autowired
	@Qualifier("noteService")
	private NoteService service;
	
	@Value("${constants.testText}")
	private String passcode;

	/**
	 * 
	 * @param name
	 * @param model
	 * @return
	 * 
	 * i.e:
	 * http://localhost:8000/qnote/mvc/node/helloworld
	 * http://localhost:8000/qnote/mvc/node/helloworld.json
	 * 
	 */
	@RequestMapping(value = "/node/{name}", method = RequestMethod.GET)
	public String getNode(@PathVariable String name, ModelMap model) {

		NoteNodeDto entity = new NoteNodeDto();
		entity.setName(name);
		entity.setCreated(new Date());
		
		model.addAttribute("model", entity);
		
		return "listNode";
	}

	/**
	 * Return json string by default
	 * @param name
	 * @return
	 * 
	 */
	@RequestMapping(value = "/nodeAsJson/{name}", method = RequestMethod.GET)
	public @ResponseBody NoteNodeDto getNoteAsJson(@PathVariable String name) {

		NoteNodeDto entity = new NoteNodeDto();
		entity.setName(name);
		
		return entity;
	}
	
	/**
	 * 
	 * @param name
	 * @param model
	 * @return
	 * 
	 * i.e: http://localhost:8000/qnote/mvc/user/luke
	 */
	@RequestMapping(value = "/user/{name}", method = RequestMethod.GET)
	public String getUser(@PathVariable String name, ModelMap model) {
		
		log.debug("Get user: " + name);
		System.out.println("passcode[" + passcode + "] -> name: " + name);

		NoteUser entity = service.findUserByName(name);
		model.addAttribute("model", entity);
		
		return "listUser";
	}


	/**
	 * 
	 * @param request
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 * 
	 * i.e: http://localhost:8000/qnote/mvc/params/passing?param1=0099
	 */
	@RequestMapping("/params/passing")
	public void sendIncidentNotification(final HttpServletRequest request)
			throws JsonGenerationException, JsonMappingException, IOException {

		String param1 = request.getParameter("param1");
		System.out.println("param1 is: " + param1);
	}


}