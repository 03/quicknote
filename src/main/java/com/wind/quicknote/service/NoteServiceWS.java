package com.wind.quicknote.service;

import javax.jws.WebService;

import com.wind.quicknote.model.NoteUser;

@WebService
public interface NoteServiceWS {
	
	String echo(String message);

	NoteUser findUserByName(String name);
	
}
