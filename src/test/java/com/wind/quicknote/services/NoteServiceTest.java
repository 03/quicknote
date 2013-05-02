package com.wind.quicknote.services;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.wind.quicknote.models.NoteNode;
import com.wind.quicknote.services.NoteService;

public class NoteServiceTest {
	
	@BeforeClass
	public static void beforeClass() {
		System.out.println("beforeClass");
	}
	
	@AfterClass
	public static void afterClass() {
		System.out.println("afterClass");
	}
	
	@Test
	public void testFindAllTopics() {
		
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		NoteService service = (NoteService)ctx.getBean("noteService");
		List<NoteNode> items = service.findAllTopics();
		System.out.println(items.size());
	}
	
	@Test
	public void testFindRoot() {
		
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		NoteService service = (NoteService)ctx.getBean("noteService");
		
		NoteNode root = service.findRootTopic();
		System.out.println("root.name---->"+root.getName());
	}

}
