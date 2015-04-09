package com.phongkien.controller;

import java.security.Principal;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.phongkien.UserVault;
import com.phongkien.model.StatusModel;
import com.phongkien.model.UserModel;
import com.phongkien.utils.UtilsFunctions;

@RestController
public class LoginManager {
	@RequestMapping(value={"/home"}, method=RequestMethod.GET)
	public ModelAndView home() {
		return new ModelAndView("home");
	}
	
	@RequestMapping(value="/login/custom")
	public ModelAndView login() {
		return new ModelAndView("login");
	}
	
	@RequestMapping(value="/logout")
	public ModelAndView logout(Principal principal) {
		if (principal != null) {
			UserVault.getInstance().removeUser(principal.getName());
		}
		return new ModelAndView("logoutSuccess");
	}
	
	@RequestMapping(value="/login/failure")
	public @ResponseBody StatusModel loginFailure() {
		StatusModel status = new StatusModel();
		status.setStatusText("Invalid username and/or password");
		return status;
	}
	
	@RequestMapping(value="/login/success")
	public @ResponseBody StatusModel loginSuccess(Principal principal) {
		String userName = principal.getName();
		UserModel userModel = new UserModel();
		userModel.setUsername(userName);
		userModel.setUserId(UtilsFunctions.generateUserId());
		userModel.setPassPhrase(UtilsFunctions.generateRandomKey(64));
		
		StatusModel status = new StatusModel();
		status.setStatusText("Login Success");
		
		UserVault.getInstance().updateUser(userName, userModel);
		
		return status;
	}
}