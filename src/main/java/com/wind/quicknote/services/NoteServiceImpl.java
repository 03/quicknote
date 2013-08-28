package com.wind.quicknote.services;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.wind.quicknote.daos.NoteNodeDAO;
import com.wind.quicknote.daos.NoteUserDAO;
import com.wind.quicknote.models.NoteNode;
import com.wind.quicknote.models.NoteUser;

@Component("noteService")
@Transactional(propagation=Propagation.REQUIRED)
public class NoteServiceImpl implements NoteService {
	
	private NoteNodeDAO noteNodeDAO;
	private NoteUserDAO noteUserDAO;
	
	public void init() {
		System.out.println("init");
	}
	
	public void destroy() {
		System.out.println("destroy");
	}
	
	public NoteNodeDAO getNoteNodeDAO() {
		return noteNodeDAO;
	}

	@Resource(name="noteNode")
	public void setNoteNodeDAO(NoteNodeDAO noteNodeDAO) {
		this.noteNodeDAO = noteNodeDAO;
	}

	public NoteUserDAO getNoteUserDAO() {
		return noteUserDAO;
	}

	@Resource(name="noteUser")
	public void setNoteUserDAO(NoteUserDAO noteUserDAO) {
		this.noteUserDAO = noteUserDAO;
	}

	//@Transactional
	public List<NoteNode> findAllTopics() {
		return noteNodeDAO.findAll();
	}
	
	public List<NoteNode> findChildTopics(long id) {
		return noteNodeDAO.findChildren(id);
	}
	
	public NoteNode findRootTopic() {
		return noteNodeDAO.findRoot();
	}
	
	public NoteNode findRootTopicByUser(long userId) {
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
	
}
