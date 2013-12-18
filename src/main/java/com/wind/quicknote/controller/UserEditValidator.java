package com.wind.quicknote.controller;

import java.util.Map;

import org.zkoss.bind.Property;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.AbstractValidator;

public class UserEditValidator extends AbstractValidator {
	
	public void validate(ValidationContext ctx) {
		//all the bean properties
		Map<String,Property> beanProps = ctx.getProperties(ctx.getProperty().getBase());
		
		String newPassword = (String)ctx.getValidatorArg("newPassword");
		validatePasswords(ctx, (String)(String)beanProps.get("password").getValue(), (String)ctx.getValidatorArg("oldPassword"), newPassword);
		validateNewPasswords(ctx, newPassword, (String)ctx.getValidatorArg("retypedPassword"));
		
		validateEmail(ctx, (String)beanProps.get("email").getValue());
	}
	
	private void validatePasswords(ValidationContext ctx, String password, String retype, String newPassword) {
		
		if(retype == null && newPassword == null) {
			// no need to change password
			return;
		}
		
		if(retype != null && (!password.equals(retype))) {
			// user input the old password and doesn't match
			this.addInvalidMessage(ctx, "currentPassword", "Your old password is wrong!");
		}
		
	}
	
	private void validateNewPasswords(ValidationContext ctx, String password, String retype) {	
		if(password != null && retype != null && (!password.equals(retype))) {
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
