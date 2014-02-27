package com.wind.quicknote.dao;

import java.util.List;

import com.wind.quicknote.model.NoteUser;

public interface NoteUserDAO extends IGenericDao<NoteUser> {

	List<NoteUser> findAll();
	
	NoteUser findByName(String name);
	
	NoteUser createStandardUser(String username, String email, String password);
	
	NoteUser createPremiumUser(String username, String email, String password);
	
	NoteUser createUser(NoteUser user);

}
