package com.wind.quicknote.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.cxf.frontend.ClientProxyFactoryBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.wind.quicknote.model.NoteNodeDto;
import com.wind.quicknote.model.NoteUserDto;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext.xml",
		"/dataAccessContext.xml", "/wsContext.xml" })
public class NoteServiceWSTest {

	@Autowired
	//@Qualifier("noteClientWS")
	@Qualifier("noteServiceWS")
	private NoteServiceWS client;

	@Test
	public void testEcho() {
		String message = client.echo("Hello you!");
		System.out.println("Result from server: " + message);
		assertNotNull(message);
	}

	@Test
	public void testFindUserByName() {
		NoteUserDto entity = client.findUserByName("zk");
		assertNotNull(entity);
	}
	
	@Test
	public void testFindAllTopicsByUser() {
		List<NoteNodeDto> dtos = client.findAllTopicsByUser(1L);
		assertNotNull(dtos);
		assertTrue(dtos.size() > 0);
	}

	public static void main(String[] args) {

		ClientProxyFactoryBean factory = new ClientProxyFactoryBean();
		if (args != null && args.length > 0 && !"".equals(args[0])) {
			factory.setAddress(args[0]);
		} else {
			factory.setAddress("http://localhost:8000/qnote/services/ws");
		}
		NoteServiceWS client = factory.create(NoteServiceWS.class);
		NoteUserDto dto = client.findUserByName("zk");
		System.out.println("Desc: " + dto.getDesc());

		System.exit(0);
	}

}
