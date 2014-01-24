package com.wind.quicknote.service;

import javax.jws.WebService;

import com.wind.quicknote.model.NoteUserDto;

/**
 * Web service interface
 * 
 *  As best practice, the return type / parameter pass-in 
 *  should be DTO instead of database model
 *
 */
@WebService
public interface NoteServiceWS {
	
	String echo(String message);

	//NoteUser findUserModelByName(String name);
	NoteUserDto findUserByName(String name);
	
}
