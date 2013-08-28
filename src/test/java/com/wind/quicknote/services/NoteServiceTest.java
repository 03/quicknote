package com.wind.quicknote.services;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.wind.quicknote.models.NoteNode;

public class NoteServiceTest {
	
	private static NoteService service = null;
	
	@BeforeClass
	public static void beforeClass() {
		System.out.println("beforeClass");
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext_unittest.xml");
		service = (NoteService)ctx.getBean("noteService");
	}
	
	@AfterClass
	public static void afterClass() {
		System.out.println("afterClass");
	}

	@Test
	public void testGetNoteNodeDAO() {
		assertNotNull(service.getNoteNodeDAO());
	}

	@Test
	public void testGetNoteUserDAO() {
		assertNotNull(service.getNoteUserDAO());
	}

	@Test
	public void testFindAllTopics() {
		List<NoteNode> items = service.findAllTopics();
		assertEquals(8 , items.size());
	}

	@Test
	public void testFindChildTopics() {
		List<NoteNode> items = service.findChildTopics(3L);
		assertEquals(2 , items.size());
	}

	//@Test
	public void testFindRootTopic() {
		NoteNode node = service.findRootTopic();
		assertEquals("ROOT" , node.getName());
	}

	@Test
	public void testFindRootTopicByUser() {
		NoteNode node = service.findRootTopicByUser(1L);
		assertEquals("ROOT" , node.getName());
	}

	@Test
	public void testFindAllUsers() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateTopicIcon() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateTopicText() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateTopicName() {
		service.updateTopicName(3, "abc");
	}

	@Test
	public void testRemoveTopic() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddTopic() {
		fail("Not yet implemented");
	}

	@Test
	public void testChangeParentId() {
		fail("Not yet implemented");
	}

}
