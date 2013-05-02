package com.wind.quicknote.services.daos;

import java.util.List;

import com.wind.quicknote.models.NoteUser;

public interface NoteUserDAO {

	public List<NoteUser> findAll();
	
	public NoteUser findUserByName(String name);
}
