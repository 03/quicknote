package com.wind.quicknote.daos;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.wind.quicknote.models.NoteNode;
import com.wind.quicknote.models.NoteUser;


/**
 * @author Luke
 * 
 *         This class provides functionality to access the {@code Note} model
 *         storage system
 * 
 */
@Component("noteUser") 
public class NoteUserDAOImpl extends CommonDAO implements NoteUserDAO {

	@SuppressWarnings("unchecked")
	public List<NoteUser> findAll() {
        return getHibernateTemplate().find("from NoteUser");
	}

	@SuppressWarnings("unchecked")
	@Override
	public NoteUser findByName(String name) {
		NoteUser example = new NoteUser();
        example.setName(name);
        
        List<NoteUser> list = getHibernateTemplate().findByExample(example);
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
        
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
		
		getHibernateTemplate().save(user);
		return user;
	}

}
