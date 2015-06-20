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

import com.cd.mythicdraft.service.MtgoDraftParserService;

@Controller
@RequestMapping("/upload")
public class FileUploadController {

	private static final Logger logger = Logger.getLogger(FileUploadController.class);	
	
	@Autowired
	private MtgoDraftParserService mtgoDraftParserService;
	
	@RequestMapping(method=RequestMethod.POST)
	public @ResponseBody String processUpload(@RequestParam String name,
							  				  @RequestParam MultipartFile file) {
		if(!file.isEmpty()) {
			try {
				mtgoDraftParserService.parse(file.getInputStream());
				
				return "File upload succeeded";
			} catch (IOException e) {
				logger.error("File upload failed!");
			}			
		}
			
		return "Draft upload failed";
	}
	
}
