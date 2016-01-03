package com.cd.mythicdraft.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.cd.mythicdraft.config.ApplicationProperties;

@Controller
@RequestMapping("/")
public class HomeController {

	private static final Logger logger = Logger.getLogger(HomeController.class);	
	
	@Autowired
	private ApplicationProperties appProperties;
	
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView getHome(HttpServletRequest request, 
								@RequestParam(required = false, 
											  value = "development",
											  defaultValue = "false") boolean development) {
		
		ModelAndView model = new ModelAndView("home");
		
		model.addObject("development", development);
		model.addObject("projectVersion", appProperties.getProjectVersion());
		
		return model;
	}
	
}
