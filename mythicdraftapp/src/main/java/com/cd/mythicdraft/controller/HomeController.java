package com.cd.mythicdraft.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cd.mythicdraft.model.Draft;
import com.cd.mythicdraft.service.DraftService;

@Controller
@RequestMapping("/")
public class HomeController {

	@Autowired
	private DraftService draftService;
	
	@RequestMapping(method=RequestMethod.GET)
	public String getHome(HttpServletRequest request) {
		Draft draft = new Draft();
		
		draftService.addDraft(draft);
		return "home";
	}
	
}
