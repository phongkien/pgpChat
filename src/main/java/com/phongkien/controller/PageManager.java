package com.phongkien.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class PageManager {
	@RequestMapping(value="/error/403")
	public ModelAndView pageNotFound() {
		return new ModelAndView("403");
	}
	
	@RequestMapping(value="/error/session/expired")
	public ModelAndView sessionExpired() {
		return new ModelAndView("sessionExpired");
	}
}
