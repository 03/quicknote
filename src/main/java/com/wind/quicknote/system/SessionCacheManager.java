package com.wind.quicknote.system;

import java.util.HashMap;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;

/**
 * Cache content
 */
public class SessionCacheManager {

	public static final String KEY_USERNOTE_SEARCH_CACHE = "USERNOTE_SEARCH_CACHE";
	
	public static void put(long id, String value) {
		Session session = Executions.getCurrent().getSession();
		
		HashMap<Long, String> contentCache = createCacheIfNonExisting(session);
		
		contentCache.put(id, value);
	}

	public static String get(long id) {
		Session session = Executions.getCurrent().getSession();
		
		HashMap<Long, String> results = createCacheIfNonExisting(session);
		
		return results.get(id);
	}
	
	private static HashMap<Long, String> createCacheIfNonExisting(
			Session session) {
		@SuppressWarnings("unchecked")
		HashMap<Long, String> contentCache = (HashMap<Long, String>) session.getAttribute(KEY_USERNOTE_SEARCH_CACHE);
		if (contentCache == null) {
			session.setAttribute(KEY_USERNOTE_SEARCH_CACHE,
					contentCache = new HashMap<Long, String>());
		}
		return contentCache;
	}
}
