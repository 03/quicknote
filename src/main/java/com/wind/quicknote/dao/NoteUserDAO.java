package com.wind.quicknote.dao;

import java.util.List;

import com.wind.quicknote.model.NoteUser;

public interface NoteUserDAO {

	List<NoteUser> findAll();
	
	NoteUser findByName(String name);
	
	NoteUser createUser(String username, String email, String password);
	
}
