package com.phongkien.model;

public class MessageModel {
	private String from;
	private String to;
	private String message;
	private String id;

	public String getFrom() {
		return from;
	}

	public void setFrom(String fromUser) {
		this.from = fromUser;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String toUser) {
		this.to = toUser;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void copyTo(MessageModel copyTo) {
		copyTo.setFrom(from);
		copyTo.setTo(to);
		copyTo.setMessage(message);
	}
}
