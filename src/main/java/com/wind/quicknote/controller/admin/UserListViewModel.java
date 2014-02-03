package com.wind.quicknote.controller.admin;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Div;

import com.wind.quicknote.helper.QUtils;
import com.wind.quicknote.model.NoteUser;
import com.wind.quicknote.model.UserRole;
import com.wind.quicknote.model.UserStatus;
import com.wind.quicknote.service.NoteService;

	@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
	public class UserListViewModel {

		@WireVariable
		private NoteService noteService;
		
		@Command
	    public void submit(@BindingParam("cmp")  Div component) {
			
			noteService.updateUser(selected);
			QUtils.showClientInfo("Save successfully!", component);
			
			// refresh is not required as data binding will apply changes automatically
			// component.invalidate();
	    }
		
		private List<NoteUser> userList = new ArrayList<NoteUser>();
		private NoteUser selected;
		
		private List<UserStatus> userStatusList = QUtils.getUserStatusList();
		private List<UserRole> userRoleList = QUtils.getUserRoleList();

		@Init
	    public void init() {
			userList = noteService.findAllUsers();
	        selected = userList.get(0); // Selected First One
	    }
		
		public List<NoteUser> getUserList() {
			return userList;
		}

		public void setUserList(List<NoteUser> userList) {
			this.userList = userList;
		}

		public NoteUser getSelected() {
			return selected;
		}

		public void setSelected(NoteUser selected) {
			this.selected = selected;
		}

		public List<UserStatus> getUserStatusList() {
			return userStatusList;
		}

		public void setUserStatusList(List<UserStatus> userStatusList) {
			this.userStatusList = userStatusList;
		}

		public List<UserRole> getUserRoleList() {
			return userRoleList;
		}

		public void setUserRoleList(List<UserRole> userRoleList) {
			this.userRoleList = userRoleList;
		}
		
	}

