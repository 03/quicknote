package com.wind.quicknote.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wind.quicknote.dao.NoteNodeDAO;
import com.wind.quicknote.dao.NoteUserDAO;
import com.wind.quicknote.model.NoteNode;
import com.wind.quicknote.model.NoteUser;

@Transactional
@Service("noteService")
public class NoteServiceImpl implements NoteService {
	
	private static Logger log = LoggerFactory.getLogger(NoteServiceImpl.class);
	
	@Value("${constants.testText}")
	private String passcode;
	
	@Autowired
	@Qualifier("noteNodeDAO")
	private NoteNodeDAO nodeDAO;
	
	@Autowired
	@Qualifier("noteUserDAO")
	private NoteUserDAO userDAO;
	
	public void init() {
		log.debug("init");
	}
	
	public void destroy() {
		log.debug("destroy");
	}
	
	/*
	 * node DAO
	 */
	public List<NoteNode> findAllTopics() {
		return nodeDAO.findAll();
	}
	
	@Override
	public NoteNode findTopic(long id) {
		return nodeDAO.find(NoteNode.class, id);
	}
	
	public List<NoteNode> findChildTopics(long id) {
		return nodeDAO.findChildren(id);
	}
	
	public NoteNode findRootTopicByUser(long userId) {
		log.debug("Passcode: " + passcode);
		return nodeDAO.findRootByUser(userId);
	}
	
	public void updateTopicIcon(long id, String iconSrc) {
		NoteNode entity = nodeDAO.find(NoteNode.class, id);
		entity.setIcon(iconSrc);
		entity.setUpdated(new Date());
		nodeDAO.merge(entity);
	}
	
	public void updateTopicText(long id, String text) {
		NoteNode entity = nodeDAO.find(NoteNode.class, id);
		entity.setText(text);
		entity.setUpdated(new Date());
		nodeDAO.merge(entity);
	}
	
	public void updateTopicName(long id, String name) {
		NoteNode entity = nodeDAO.find(NoteNode.class, id);
		entity.setName(name);
		entity.setUpdated(new Date());
		nodeDAO.merge(entity);
	}
	
	public void removeTopic(long id) {
		nodeDAO.remove(id);
	}
	
	public NoteNode addTopic(long pid, String name, String content, String picUrl) {
		return nodeDAO.addChild(pid, name, content, picUrl);
	}
	
	public void changeParentId(long id, long pid) {
		nodeDAO.changeParent(id, pid);
	}
	

	/*
	 * user DAO
	 */
	public List<NoteUser> findAllUsers() {
		return userDAO.findAll();
	}
	
	@Override
	public NoteUser addUser(String name, String email, String password) {
		return userDAO.createUser(name, email, password);
	}

	@Override
	public NoteUser findUserByName(String name) {
		return userDAO.findByName(name);
	}

	@Override
	public void updateUser(NoteUser user) {
		userDAO.merge(user);
	}

	@Override
	public void removeUser(NoteUser user) {
		userDAO.delete(user);
	}
}
