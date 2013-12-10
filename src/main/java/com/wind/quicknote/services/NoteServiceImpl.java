package com.wind.quicknote.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wind.quicknote.dao.NoteNodeDAO;
import com.wind.quicknote.dao.NoteUserDAO;
import com.wind.quicknote.models.NoteNode;
import com.wind.quicknote.models.NoteUser;

@Transactional
@Service("noteService")
public class NoteServiceImpl implements NoteService {
	
	private static Logger log = LoggerFactory.getLogger(NoteServiceImpl.class);
	
	@Value("${constants.testText}")
	private String passcode;
	
	@Autowired
	private NoteNodeDAO noteNodeDAO;
	
	@Autowired
	private NoteUserDAO noteUserDAO;
	
	public void init() {
		log.debug("init");
	}
	
	public void destroy() {
		log.debug("destroy");
	}
	
	public List<NoteNode> findAllTopics() {
		return noteNodeDAO.findAll();
	}
	
	public List<NoteNode> findChildTopics(long id) {
		return noteNodeDAO.findChildren(id);
	}
	
	public NoteNode findRootTopicByUser(long userId) {
		log.debug("findRootTopicByUser -----> " + passcode);
		return noteNodeDAO.findRootByUser(userId);
	}
	
	public List<NoteUser> findAllUsers() {
		return noteUserDAO.findAll();
	}
	
	public void updateTopicIcon(long id, String iconSrc) {
		noteNodeDAO.updateIcon(id, iconSrc);
	}
	
	public void updateTopicText(long id, String text) {
		noteNodeDAO.updateText(id, text);
	}
	
	public void updateTopicName(long id, String name) {
		noteNodeDAO.updateName(id, name);
	}
	
	public void removeTopic(long id) {
		noteNodeDAO.remove(id);
	}
	
	public NoteNode addTopic(long pid, String name, String content, String picUrl) {
		return noteNodeDAO.addChild(pid, name, content, picUrl);
	}
	
	public void changeParentId(long id, long pid) {
		noteNodeDAO.changeParent(id, pid);
	}

	@Override
	public NoteUser addUser(String name, String email, String password) {
		return noteUserDAO.createUser(name, email, password);
	}

	@Override
	public NoteNode getTopic(long id) {
		return noteNodeDAO.findById(id);
	}

	@Override
	public NoteUser findUserByName(String name) {
		return noteUserDAO.findByName(name);
	}
	
}
