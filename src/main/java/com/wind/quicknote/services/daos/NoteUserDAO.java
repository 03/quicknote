package com.wind.quicknote.services.daos;

import java.util.List;

import com.wind.quicknote.models.NoteUser;

public interface NoteUserDAO {

	List<NoteUser> findAll();
	
	NoteUser findUserByName(String name);
	
	NoteUser createNewUser(String userName, String password);
	
	void initUserRootNoteNode(long userId);
}
