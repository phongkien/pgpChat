package com.phongkien.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class LoginManager {

	@RequestMapping(value="/login", method={RequestMethod.POST, RequestMethod.GET})
	public ModelAndView login(
			@RequestParam(value = "username", defaultValue = "") String username,
			@RequestParam(value = "password", defaultValue = "") String password) {

		ModelAndView modView;
		
		if (username == null || username.length() == 0) {
			modView = new ModelAndView("login");
		} else {
			modView = new ModelAndView("loginSuccess");
			modView.addObject("name", username);
		}
		
		return modView;
	}
}