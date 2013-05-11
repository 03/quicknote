package com.wind.quicknote.services;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.wind.quicknote.models.NoteNode;
import com.wind.quicknote.services.NoteService;

public class NoteServiceTest {
	
	private static NoteService service = null;
	
	@BeforeClass
	public static void beforeClass() {
		System.out.println("beforeClass");
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		service = (NoteService)ctx.getBean("noteService");
	}
	
	@AfterClass
	public static void afterClass() {
		System.out.println("afterClass");
	}
	
	@Test
	public void testFindAllTopics() {
		
		List<NoteNode> items = service.findAllTopics();
		System.out.println(items.size());
	}
	
	@Test
	public void testFindRootByUser() {
		
		NoteNode root = service.findRootTopicByUser(1L);
		System.out.println("root.name---->"+root.getName());
	}

	@Test
	public void testUpdateTopicName() {
		
		service.updateTopicName(3, "abc");
	}
	
}
