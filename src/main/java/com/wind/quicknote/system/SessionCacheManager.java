package com.wind.quicknote.system;

import java.util.HashMap;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;

import com.wind.quicknote.model.NoteNode;

/**
 * SessionCacheManager
 * (key: nodeId | value: node)
 */
public class SessionCacheManager {

	public static final String KEY_USERNOTE_SEARCH_CACHE = "USERNOTE_SEARCH_CACHE";
	
	public static void put(long id, NoteNode value) {
		Session session = Executions.getCurrent().getSession();
		
		HashMap<Long, NoteNode> contentCache = createCacheIfNonExisting(session);
		
		contentCache.put(id, value);
	}

	public static NoteNode get(long id) {
		Session session = Executions.getCurrent().getSession();
		
		HashMap<Long, NoteNode> results = createCacheIfNonExisting(session);
		
		return results.get(id);
	}
	
	private static HashMap<Long, NoteNode> createCacheIfNonExisting(
			Session session) {
		@SuppressWarnings("unchecked")
		HashMap<Long, NoteNode> contentCache = (HashMap<Long, NoteNode>) session.getAttribute(KEY_USERNOTE_SEARCH_CACHE);
		if (contentCache == null) {
			session.setAttribute(KEY_USERNOTE_SEARCH_CACHE,
					contentCache = new HashMap<Long, NoteNode>());
		}
		return contentCache;
	}
	
}
