package com.cd.mythicdraft.controller;

import java.io.IOException;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/upload")
public class FileUploadController {

	private static final Logger logger = Logger.getLogger(FileUploadController.class);	
	
	@RequestMapping(method=RequestMethod.POST)
	public @ResponseBody String processUpload(@RequestParam String name,
							  				  @RequestParam MultipartFile file) throws IOException {
		if(!file.isEmpty()) {
			byte [] fileBytes = file.getBytes();
			
			final String md5Hash = DigestUtils.md5Hex(fileBytes);
			
			logger.debug("New file hash=" + md5Hash);
			
			return "File upload succeeded";
		} else {
			return "Draft upload failed";
		}
	}
	
}
