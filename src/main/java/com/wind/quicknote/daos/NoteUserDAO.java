package com.wind.quicknote.daos;

import java.util.List;

import com.wind.quicknote.models.NoteUser;

public interface NoteUserDAO {

	List<NoteUser> findAll();
	
	NoteUser findByName(String name);
	
	NoteUser createUser(String userName, String password);
	
	void initUserRoot(long userId);
}
