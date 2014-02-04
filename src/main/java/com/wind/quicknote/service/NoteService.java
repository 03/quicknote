package com.wind.quicknote.service;

import java.util.List;

import javax.jws.WebService;

import com.wind.quicknote.model.NoteNode;
import com.wind.quicknote.model.NoteUser;

@WebService
public interface NoteService {
	
	void init();
	
	void destroy();
	
	// Topic
	List<NoteNode> findAllTopics();
	
	List<NoteNode> findAllTopicsByUser(long userId);
	
	List<NoteNode> findChildTopics(long id);
	
	NoteNode findRootTopicByUser(long userId);
	
	List<NoteUser> findAllUsers();
	
	void updateTopicIcon(long id, String iconSrc);
	
	void updateTopicText(long id, String text);
	
	void updateTopicName(long id, String name);
	
	void removeTopic(long id);
	
	NoteNode findTopic(long id);
	
	NoteNode addTopicAtLast(long pid, String name, String content, String picUrl);
	
	NoteNode addTopic(long pid, int pos, String name, String content, String picUrl);
	
	void changeParentId(long id, long pid);
	
	// User
	NoteUser addUserByEmail(String loginName, String email, String password);
	
	NoteUser addUser(NoteUser user);

	NoteUser findUserByName(String name);

	void updateUser(NoteUser user);
	
	void removeUser(NoteUser user);

	void swapTopicPosition(long pid, int pos1, int pos2);

	boolean isLoginNameAvailable(String name);
	
}
