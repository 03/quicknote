package com.wind.quicknote.services;

import java.util.List;

import com.wind.quicknote.daos.NoteNodeDAO;
import com.wind.quicknote.daos.NoteUserDAO;
import com.wind.quicknote.models.NoteNode;
import com.wind.quicknote.models.NoteUser;

public interface NoteService {
	
	public void init();
	
	public void destroy();
	
	public NoteNodeDAO getNoteNodeDAO();

	public NoteUserDAO getNoteUserDAO();

	public List<NoteNode> findAllTopics();
	
	public List<NoteNode> findChildTopics(long id);
	
	public NoteNode findRootTopic();
	
	public NoteNode findRootTopicByUser(long userId);
	
	public List<NoteUser> findAllUsers();
	
	public void updateTopicIcon(long id, String iconSrc);
	
	public void updateTopicText(long id, String text);
	
	public void updateTopicName(long id, String name);
	
	public void removeTopic(long id);
	
	public NoteNode addTopic(long pid, String name, String content, String picUrl);
	
	public void changeParentId(long id, long pid);
	
}
