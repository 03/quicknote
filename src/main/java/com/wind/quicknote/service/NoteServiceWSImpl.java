package com.wind.quicknote.service;

import javax.jws.WebService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wind.quicknote.dao.NoteUserDAO;
import com.wind.quicknote.model.NoteUser;

@Transactional
@Service("noteServiceWS")
@WebService(endpointInterface = "com.wind.quicknote.service.NoteServiceWS")
public class NoteServiceWSImpl implements NoteServiceWS {
	
	private static Logger log = LoggerFactory.getLogger(NoteServiceWSImpl.class);
	
	@Value("${constants.testText}")
	private String passcode;
	
	@Autowired
	@Qualifier("noteUserDAO")
	private NoteUserDAO userDAO;
	
	@Override
	public String echo(String message) {
		log.debug("echo");
		return "passcode[" + passcode + "] -> echo: " + message;
	}
	
	@Override
	public NoteUser findUserByName(String name) {
		return userDAO.findByName(name);
	}
}
