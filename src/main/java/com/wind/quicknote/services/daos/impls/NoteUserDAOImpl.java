package com.wind.quicknote.services.daos.impls;

import java.util.List;

import org.springframework.stereotype.Component;

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
}
