package com.wind.quicknote.services.daos.impls;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.wind.quicknote.models.NoteNode;
import com.wind.quicknote.models.NoteUser;
import com.wind.quicknote.services.daos.NoteUserDAO;


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
	public NoteUser findUserByName(String name) {
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
	public NoteUser createNewUser(String userName, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void initUserRootNoteNode(long userId) {

		NoteNode node = new NoteNode();
		node.setContent("Root(invisible) of user["+String.valueOf(userId)+"]");
		node.setName("Root");
		node.setOwnerId(String.valueOf(userId));
		node.setCreated(new Date());
		node.setParent(null);
		
		getHibernateTemplate().save(node);
		
	}
}
