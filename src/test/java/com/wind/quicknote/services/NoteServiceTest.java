package com.wind.quicknote.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.wind.quicknote.models.NoteNode;
import com.wind.quicknote.models.NoteUser;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext.xml", 
								  "/dataAccessContext.xml"})
public class NoteServiceTest {
	
	
	@Autowired
	@Qualifier("noteService")
	private NoteService service;
	
	@BeforeClass
	public static void beforeClass() {
		/*ClassPathXmlApplicationContext ctx = newClassPathXmlApplicationContext("applicationContext_test.xml" );
        service = (NoteService)ctx.getBean("noteService" );*/
	}
	
	@AfterClass
	public static void afterClass() {
		System.out.println("afterClass ends.");
	}

	@Test
	public void testFindAllTopics() {
		List<NoteNode> items = service.findAllTopics();
		assertTrue(items.size() > 0);
	}

	@Test
	public void testFindChildTopics() {
		List<NoteNode> items = service.findChildTopics(3L);
		assertEquals(2 , items.size());
	}

	@Test
	public void testFindRootTopicByUser() {
		NoteNode node = service.findRootTopicByUser(1L);
		assertEquals("ROOT" , node.getName());
	}

	@Test
	public void testFindAllUsers() {
		List<NoteUser> userlist = service.findAllUsers();
		assertTrue(userlist.size() > 0);
	}

	/*@Test
	public void testUpdateTopicIcon() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateTopicText() {
		fail("Not yet implemented");
	}*/

	@Test
	public void testUpdateTopicName() {
		
		long id = 3L;
		String oldValue = "third topic";
		String newValue = "new third topic";
		
		NoteNode note = service.getTopic(id);
		assertEquals(oldValue, note.getName());
		
		service.updateTopicName(id, newValue);
		note = service.getTopic(id);
		assertEquals(newValue, note.getName());
		
		service.updateTopicName(id, oldValue);
		note = service.getTopic(id);
		assertEquals(oldValue, note.getName());
	}

	/*@Test
	public void testRemoveTopic() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddTopic() {
		fail("Not yet implemented");
	}*/

	@Test
	public void testChangeParentId() {
		
		long id = 8L;
		long oldParentId = 3L;
		long newParentId = 4L;
		
		NoteNode note = service.getTopic(id);
		assertEquals(oldParentId, note.getParent().getId());
		
		service.changeParentId(id, newParentId);
		note = service.getTopic(id);
		assertEquals(newParentId, note.getParent().getId());
		
		service.changeParentId(id, oldParentId);
		note = service.getTopic(id);
		assertEquals(oldParentId, note.getParent().getId());
		
	}

	@Test
	public void testAddUser() {
		NoteUser user = service.addUser("Kevin Abbort", "send@mail.com", "abcdefg");
		assertNotNull(user.getId());
	}
	
}
