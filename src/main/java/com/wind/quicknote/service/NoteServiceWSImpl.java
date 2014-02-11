package com.wind.quicknote.service;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wind.quicknote.dao.NoteNodeDAO;
import com.wind.quicknote.dao.NoteUserDAO;
import com.wind.quicknote.model.NoteNode;
import com.wind.quicknote.model.NoteNodeDto;
import com.wind.quicknote.model.NoteUser;
import com.wind.quicknote.model.NoteUserDto;

@Transactional
@Service("noteServiceWS")
@WebService(endpointInterface = "com.wind.quicknote.service.NoteServiceWS")
public class NoteServiceWSImpl implements NoteServiceWS {
	
	private static Logger log = LoggerFactory.getLogger(NoteServiceWSImpl.class);
	
	@Autowired
	@Qualifier("noteUserDAO")
	private NoteUserDAO userDAO;
	
	@Autowired
	@Qualifier("noteNodeDAO")
	private NoteNodeDAO nodeDAO;
	
	/*@Override
	public NoteUser findUserModelByName(String name) {
		return userDAO.findByName(name);
	}*/
	
	@Override
	public String echo(String message) {
		log.debug("echo");
		return "echo: " + message;
	}

	@Override
	public NoteUserDto findUserByName(String name) {
		
		NoteUserDto dto = new NoteUserDto();
		NoteUser model = userDAO.findByName(name);
		if(model != null) {
			// copy fields, see: http://stackoverflow.com/questions/5079458/copy-specific-fields-by-using-beanutils-copyproperties
			/*BeanUtils.copyProperties(model, dto, new String[] {"role", "status"});
			dto.setRole(model.getRole());
			dto.setStatus(model.getStatus());*/
			
			BeanUtils.copyProperties(model, dto);
		}
		
		return dto;
	}

	@Override
	public List<NoteNodeDto> findAllTopicsByUser(long userId) {
		
		List<NoteNodeDto> dtos = new ArrayList<NoteNodeDto>();
		List<NoteNode> nodes = nodeDAO.findMatchedTopicsByUser(userId, null);
		
		for(NoteNode node : nodes) {
			NoteNodeDto dto = prepareParentDto(node.getParent());
			dtos.add(dto);
		}
		
		return dtos;
	}
	
	private NoteNodeDto prepareParentDto(NoteNode node) {
		NoteNodeDto dto = new NoteNodeDto();
		if(node == null)
			return dto;
		
		// ignore children at this stage
		BeanUtils.copyProperties(node, dto, new String[] {"parent", "children"});
		dto.setParent(prepareParentDto(node.getParent()));
		return dto;
	}
	
}
