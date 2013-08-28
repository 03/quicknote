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
	public NoteUser createUser(String userName, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void initUserRoot(long userId) {

		NoteNode node = new NoteNode();
		node.setText("Root(invisible) of user["+String.valueOf(userId)+"]");
		node.setName("Root");
		node.setOwnerId(String.valueOf(userId));
		node.setCreated(new Date());
		node.setParent(null);
		
		getHibernateTemplate().save(node);
		
	}
}
