package com.wind.quicknote.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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
@SuppressWarnings({ "unchecked" })
@Repository("noteUserDAO") 
public class NoteUserDAOImpl implements NoteUserDAO {

	@SuppressWarnings("rawtypes")
	@Autowired
	private IGenericDao genericDao;
	
	public List<NoteUser> findAll() {
		System.out.println("---> "+ genericDao);
		return genericDao.search("from NoteUser");
	}

	@Override
	public NoteUser findByName(String name) {
		
		Map<String, String> sqlParams = new HashMap<String, String> ();
		sqlParams.put("name", name);
		List<NoteUser> records = genericDao.search("from NoteUser where name=:name", sqlParams);
		
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
		
		genericDao.save(user);
		return user;
	}

}
