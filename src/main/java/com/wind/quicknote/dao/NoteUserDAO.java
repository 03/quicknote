package com.wind.quicknote.dao;

import java.util.List;

import com.wind.quicknote.model.NoteUser;

public interface NoteUserDAO extends IGenericDao<NoteUser> {

	List<NoteUser> findAll();
	
	NoteUser findByName(String name);
	
	NoteUser createUser(String username, String email, String password);

	void updateDesc(long userId, String desc);
	
}
