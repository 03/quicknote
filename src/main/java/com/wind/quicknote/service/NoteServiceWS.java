package com.wind.quicknote.service;

import java.util.List;

import javax.jws.WebService;

import com.wind.quicknote.model.NoteNodeDto;
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
	NoteUserDto findUserByName(String name);
	List<NoteNodeDto> findAllTopicsByUser(long userId);
	
}
