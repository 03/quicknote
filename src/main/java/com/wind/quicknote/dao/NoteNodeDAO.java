package com.wind.quicknote.dao;

import java.util.List;

import com.wind.quicknote.model.NoteNode;

public interface NoteNodeDAO extends IGenericDao<NoteNode>{

	List<NoteNode> findAll();
	
	NoteNode findRootByUser(long userId);

	List<NoteNode> findAllAvailableByUser(long userId);
	
	void remove(long id);
	
	NoteNode addChild(long pid, String name, String content, String picUrl);
	
	void changeParent(long id, long pid);

	List<NoteNode> findChildren(long id);

}
