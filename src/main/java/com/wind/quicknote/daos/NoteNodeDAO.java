package com.wind.quicknote.daos;

import java.util.List;

import com.wind.quicknote.models.NoteNode;

public interface NoteNodeDAO {

	List<NoteNode> findAll();
	
	NoteNode findRootByUser(long userId);

	List<NoteNode> findAllAvailableByUser(long userId);
	
	NoteNode findById(long id);

	void remove(long id);
	
	NoteNode addChild(long pid, String name, String content, String picUrl);
	
	void updateText(long id, String content);
	
	void updateName(long id, String name);

	void updateIcon(long id, String iconSrc);
	
	void changeParent(long id, long pid);

	List<NoteNode> findChildren(long id);

}
