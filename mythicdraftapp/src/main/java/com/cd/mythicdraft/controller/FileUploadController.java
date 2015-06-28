package com.cd.mythicdraft.controller;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.cd.mythicdraft.service.DraftService;

@Controller
@RequestMapping("/upload")
public class FileUploadController {

	private static final Logger logger = Logger.getLogger(FileUploadController.class);	
	
	@Autowired
	private DraftService draftService;
	
	@RequestMapping(method=RequestMethod.POST)
	public @ResponseBody String processUpload(@RequestParam String name,
							  				  @RequestParam MultipartFile file) {
		
		if(!file.isEmpty()) {
			try {
				draftService.addDraft(file.getInputStream(), name);
				
				return "File upload succeeded";
			} catch (IOException e) {
				logger.error("File upload failed!");
				throw new RuntimeException(e);
			}			
		}
			
		return "Draft upload failed";
	}
	
}
