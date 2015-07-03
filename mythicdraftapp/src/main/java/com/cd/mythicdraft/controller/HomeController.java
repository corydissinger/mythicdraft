package com.cd.mythicdraft.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class HomeController {

	private static final Logger logger = Logger.getLogger(HomeController.class);	
	
	@RequestMapping(method=RequestMethod.GET)
	public String getHome(HttpServletRequest request) {
		return "home";
	}
	
}
