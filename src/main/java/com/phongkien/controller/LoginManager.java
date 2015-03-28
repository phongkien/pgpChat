package com.phongkien.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.phongkien.model.StatusModel;

@RestController
public class LoginManager {

	@RequestMapping(value={"/home", "/"}, method=RequestMethod.GET)
	public ModelAndView home() {
		return new ModelAndView("home");
	}
	
	@RequestMapping(value="/login/custom")
	public ModelAndView login() {
		return new ModelAndView("login");
	}
	
	@RequestMapping(value="/logout")
	public ModelAndView logout() {
		return new ModelAndView("logoutSuccess");
	}
	
	@RequestMapping(value="/login/failure")
	public @ResponseBody StatusModel loginFailure() {
		StatusModel status = new StatusModel();
		status.setStatusText("Invalid username and/or password");
		return status;
	}
	
	@RequestMapping(value="/login/success")
	public @ResponseBody StatusModel loginSuccess() {
		StatusModel status = new StatusModel();
		status.setStatusText("Login Success");
		return status;
	}
}