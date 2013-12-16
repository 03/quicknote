package com.wind.quicknote.model;

import java.util.Date;

import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionListener;

@RevisionEntity(QNoteRevisionListener.class)
public class QNoteRevisionListener implements RevisionListener {

	@Override
	public void newRevision(Object entity) {

		QNoteRevEntity ent = (QNoteRevEntity) entity;
		
		ent.setUserName("Helen");
		ent.setIpAddr("127.0.0.1");
		ent.setUpdated(new Date());
	}

}
