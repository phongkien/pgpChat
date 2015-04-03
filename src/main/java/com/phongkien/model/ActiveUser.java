package com.phongkien.model;

public class ActiveUser {
	private String userName;

	public ActiveUser() {
		this(null);
	}
	
	public ActiveUser(String userName) {
		setUserName(userName);
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
