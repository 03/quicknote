package com.wind.quicknote.service;

import com.wind.quicknote.model.NoteUserDto;

/**
 * Web service (Restful) interface
 * 
 *  As best practice, the return type / parameter pass-in 
 *  should be DTO instead of database model
 *
 */
public interface NoteServiceRS {
	
	String echo(String message);
	String silentEcho();
	NoteUserDto findUserByName(String name);
	NoteUserDto addUser(NoteUserDto user);
	
}
