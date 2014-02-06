package com.wind.quicknote.dao;

import java.util.List;

import com.wind.quicknote.model.NoteNode;

public interface NoteNodeDAO extends IGenericDao<NoteNode>{

	List<NoteNode> findAll();
	
	NoteNode findRootByUser(long userId);
	
//	NoteNode findRootSkeleton(long userId);

	List<NoteNode> findMatchedTopicsByUser(long userId, String keyword);
	
	void remove(long id);
	
	/**
	 * Add child at last position to parent
	 * 
	 * @param pid
	 * @param name
	 * @param content
	 * @param picUrl
	 * @return
	 */
	NoteNode addChild(long pid, String name, String content, String picUrl);
	
	/**
	 * Add child at given position to parent
	 * 
	 * @param pid
	 * @param pos
	 * @param name
	 * @param text
	 * @param picUrl
	 * @return
	 */
	NoteNode addChild(long pid, int pos, String name, String text, String picUrl);
	
	void changeParent(long id, long pid);

	List<NoteNode> findChildren(long id);

	void swapPosition(long pid, int pos1, int pos2);

}
