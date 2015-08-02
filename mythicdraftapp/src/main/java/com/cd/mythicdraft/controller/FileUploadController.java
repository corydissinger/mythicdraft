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

import com.cd.mythicdraft.json.JsonUploadStatus;
import com.cd.mythicdraft.service.DraftService;

@Controller
@RequestMapping("/upload")
public class FileUploadController {

	private static final Logger logger = Logger.getLogger(FileUploadController.class);	
	
	@Autowired
	private DraftService draftService;
	
	@RequestMapping(method=RequestMethod.POST)
	public @ResponseBody JsonUploadStatus processUpload(@RequestParam String name,
										  				@RequestParam Integer wins,
									  					@RequestParam Integer losses,
									  					@RequestParam MultipartFile file) {
		JsonUploadStatus uploadStatus = new JsonUploadStatus();
		
		//Draft logs should be text and should not be huge
		if(!"text/plain".equals(file.getContentType()) || 15000 < file.getSize()){
			uploadStatus.setDraftInvalid(true);
			return uploadStatus;
		}
		
		try {
			uploadStatus = draftService.addDraft(file.getInputStream(), 
								  			     name,
								  			     wins,
								  			     losses);
		} catch (IOException e) {
			uploadStatus.setDraftInvalid(true);
		}
		
		return uploadStatus;
	}
	
}
