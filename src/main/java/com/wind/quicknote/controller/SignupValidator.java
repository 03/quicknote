package com.wind.quicknote.controller;

import java.util.Map;

import org.zkoss.bind.Property;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.AbstractValidator;

import com.wind.quicknote.helper.SpringBeanUtil;
import com.wind.quicknote.service.NoteService;

public class SignupValidator extends AbstractValidator {
	
	private static NoteService noteService = SpringBeanUtil.getBean("noteService", NoteService.class);
	
	public void validate(ValidationContext ctx) {
		//all the bean properties
		Map<String,Property> beanProps = ctx.getProperties(ctx.getProperty().getBase());
		
		validateLoginNameIsAvailable(ctx, (String)beanProps.get("loginName").getValue());
		validatePasswords(ctx, (String)beanProps.get("password").getValue(), (String)ctx.getValidatorArg("retypedPassword"));
		validateEmail(ctx, (String)beanProps.get("email").getValue());
	}
	
	private void validateLoginNameIsAvailable(ValidationContext ctx, String loginName) {
		
		if(!noteService.isLoginNameAvailable(loginName)) {
			this.addInvalidMessage(ctx, "loginname", "Sorry the name has been taken.");			
		}
		
	}

	private void validatePasswords(ValidationContext ctx, String password, String retype) {	
		if(password == null || retype == null || (!password.equals(retype))) {
			this.addInvalidMessage(ctx, "password", "Your passwords do not match!");
		}
	}
	
	private void validateEmail(ValidationContext ctx, String email) {
		if(email == null) return;
		if("".equals(email.trim())) return;
		
		if(!email.matches(".+@.+\\.[a-z]+")) {
			this.addInvalidMessage(ctx, "email", "Please enter a valid email!");			
		}
	}
	
}
