package com.wind.quicknote.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.wind.quicknote.model.NoteNode;
import com.wind.quicknote.model.NoteUser;


/**
 * @author Luke
 * 
 *         This class provides functionality to access the {@code Note} model
 *         storage system
 * 
 */
@Repository("noteUserDAO") 
public class NoteUserDAOImpl extends GenericDao<NoteUser> implements NoteUserDAO {

	@Override
	public NoteUser findByName(String name) {
		
		Map<String, Object> sqlParams = new HashMap<String, Object> ();
		sqlParams.put("name", name);
		List<NoteUser> records = search("from NoteUser where name=:name", sqlParams);
		
		if (records.size() > 0) 
			return records.get(0);
		return null;
	}

	@Override
	public NoteUser createUser(String username, String email, String password) {
		
		// Create root note
		NoteNode node = new NoteNode();
		node.setText("Root(invisible) of user["+username+"]");
		node.setName("Root");
		node.setCreated(new Date());
		node.setParent(null);
		
		// Create user
		NoteUser user = new NoteUser();
		user.setName(username);
		user.setEmail(email);
		user.setPassword(password);
		user.setCreated(new Date());
		user.setRootnote(node);
		
		save(user);
		return user;
	}

	@Override
	public void updateDesc(long userId, String desc) {

		NoteUser user = (NoteUser) findById(NoteUser.class, userId);
		user.setDesc(desc);
	}

	@Override
	public List<NoteUser> findAll() {
		return findAll(NoteUser.class);
	}

}
