package com.wind.quicknote.service;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.wind.quicknote.model.NoteUser;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext.xml",
		"/dataAccessContext.xml", "/wsContext.xml" })
public class NoteServiceWSTest {

	@Autowired
	@Qualifier("noteClientWS")
	private NoteServiceWS client;

	@Test
	public void testEcho() {
		String message = client.echo("Hello you!");
		System.out.println("Result from server: " + message);
		assertNotNull(message);
	}
	
	@Test
	public void testFindUserByName() {
		NoteUser entity = client.findUserByName("zk");
		System.out.println("Result from server: " + entity);
		assertNotNull(entity);
	}

}
