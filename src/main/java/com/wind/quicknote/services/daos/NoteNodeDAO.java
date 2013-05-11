package com.wind.quicknote.services.daos;

import java.util.List;

import com.wind.quicknote.models.NoteNode;

public interface NoteNodeDAO {

	List<NoteNode> findAll();
	
	NoteNode findRootNode();

	List<NoteNode> findAllAvailable();

	void remove(long id);
	
	NoteNode addChild(long pid, String name, String content, String picUrl);
	
	void updateTopicText(long id, String content);
	
	void updateTopicName(long id, String name);

	void updateTopicIcon(long id, String iconSrc);
	
	void changeParentId(long id, long pid);

	String findTopicText(long id);

	List<NoteNode> findChildTopics(long id);

	NoteNode findRootNodeByUser(long userId);

}
