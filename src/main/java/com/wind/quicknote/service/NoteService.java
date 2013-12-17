package com.wind.quicknote.service;

import java.util.List;

import com.wind.quicknote.model.NoteNode;
import com.wind.quicknote.model.NoteUser;

public interface NoteService {
	
	public void init();
	
	public void destroy();
	
	// Topic
	public List<NoteNode> findAllTopics();
	
	public List<NoteNode> findChildTopics(long id);
	
	public NoteNode findRootTopicByUser(long userId);
	
	public List<NoteUser> findAllUsers();
	
	public void updateTopicIcon(long id, String iconSrc);
	
	public void updateTopicText(long id, String text);
	
	public void updateTopicName(long id, String name);
	
	public void removeTopic(long id);
	
	public NoteNode findTopic(long id);
	
	public NoteNode addTopic(long pid, String name, String content, String picUrl);
	
	public void changeParentId(long id, long pid);
	
	// User
	public NoteUser addUser(String name, String email, String password);

	public NoteUser findUserByName(String name);

	public void updateUser(NoteUser user);
	
	public void removeUser(NoteUser user);
	
}
