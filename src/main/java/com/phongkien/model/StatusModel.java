package com.phongkien.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class StatusModel {
	private String statusText;
	private String redirectPage;

	@JsonSerialize
	public String getStatusText() {
		return statusText;
	}

	public void setStatusText(String statusText) {
		this.statusText = statusText;
	}

	public String getRedirectPage() {
		return redirectPage;
	}

	public void setRedirectPage(String redirectPage) {
		this.redirectPage = redirectPage;
	}
}
