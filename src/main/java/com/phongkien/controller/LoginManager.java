package com.phongkien.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.phongkien.model.LoginStatusModel;
import com.phongkien.model.UserModel;

@RestController
public class LoginManager {

	@RequestMapping(value="/", method=RequestMethod.GET)
	public ModelAndView home() {
		return new ModelAndView("login");
	}
	
	@RequestMapping(value="/login", method={RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody LoginStatusModel login(@RequestBody UserModel user) {

		LoginStatusModel loginStatus = new LoginStatusModel();
		
		if (user == null || user.getUsername() == null || user.getUsername().length() == 0) {
			loginStatus.setStatusText("Please enter username and password");
		} else {
			final String username = user.getUsername();
			final String password = user.getPassword();
			
			//TODO for testing only.
			if (username.equals("phong") && password.equals("testing")) {
				loginStatus.setStatusText("Success");
				loginStatus.setRedirectPage("/");
			} else {
				loginStatus.setStatusText("Invalid username and/or password.");
			}
		}
		
		return loginStatus;
	}
}