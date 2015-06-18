package com.cd.mythicdraft.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cd.mythicdraft.service.DraftService;

@Controller
@RequestMapping("/")
public class HomeController {

	private static final Logger logger = Logger.getLogger(HomeController.class);	
	
	@Autowired
	private DraftService draftService;
	
	@RequestMapping(method=RequestMethod.GET)
	public String getHome(HttpServletRequest request) {
		return "home";
	}
	
}
