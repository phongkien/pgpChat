package com.phongkien;

import java.util.Hashtable;

import com.phongkien.model.UserModel;

public class UserVault {
	private Hashtable<String, UserModel> userMap = new Hashtable<String, UserModel>();
	private static UserVault _self;
	private static Object object = new Object();
	
	private UserVault() {
		
	}
	
	public static UserVault getInstance() {
		synchronized(UserVault.object) {
			if (_self == null) {
				_self = new UserVault();
			}
			
			return _self;
		}
	}
	
	public boolean addUser(String userName, UserModel model) {
		boolean success = false;
		
		if (!userMap.containsKey(userName)) {
			userMap.put(userName, model);
			success = true;
		}
		
		return success;
	}
	
	public void removeUser(String userName) {
		if (userMap.contains(userName)) {
			userMap.remove(userName);
		}
	}
	
	public void updateUser(String userName, UserModel model) {
		userMap.put(userName, model);
	}
	
	public UserModel getUser(String userName) {
		return userMap.get(userName);
	}
}
