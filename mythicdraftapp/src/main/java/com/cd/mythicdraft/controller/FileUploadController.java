package com.cd.mythicdraft.controller;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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
	@Transactional
	public @ResponseBody JsonUploadStatus processUpload(@RequestParam String name,
										  				@RequestParam Integer wins,
									  					@RequestParam Integer losses,
									  					@RequestParam MultipartFile file,
									  					@RequestParam(required = false) MultipartFile deck) {
		JsonUploadStatus uploadStatus = new JsonUploadStatus();
		
		//Draft logs should be text and should not be huge
		if(!"text/plain".equals(file.getContentType()) || 15000 < file.getSize()){
			uploadStatus.setDraftInvalid(true);
			return uploadStatus;
		}
		
		if(deck != null && (!"text/plain".equals(deck.getContentType()) || 5000 < deck.getSize())) {
			uploadStatus.setDeckInvalid(true);
			return uploadStatus;
		}
		
		try {
			uploadStatus = draftService.addDraft(file.getInputStream(), 
								  			     name,
								  			     wins,
								  			     losses);
			
			if(deck != null) {
				final Integer deckId = draftService.addDeck(uploadStatus.getDraftId(), deck.getInputStream());
				
				if(deckId != null && !deckId.equals(0)) {
					uploadStatus.setDeckInvalid(false);
					uploadStatus.setDeckId(deckId);
				} else {
					uploadStatus.setDeckInvalid(true);
				}
				
			}
			
		} catch (IOException e) {
			uploadStatus.setDraftInvalid(true);
		}
		
		return uploadStatus;
	}

	@RequestMapping(value = "/deck", method = RequestMethod.POST)
	public @ResponseBody JsonUploadStatus processUploadDeck(@RequestParam Integer draftId,
															@RequestParam MultipartFile deck) {
		JsonUploadStatus uploadStatus = new JsonUploadStatus();

		if(deck != null && (!"text/plain".equals(deck.getContentType()) || 5000 < deck.getSize())) {
			uploadStatus.setDeckInvalid(true);
			return uploadStatus;
		}
		
		try {
			final Integer deckId = draftService.addDeck(draftId, deck.getInputStream());
					
			if(deckId != null && !deckId.equals(0)) {
				uploadStatus.setDeckId(deckId);	
			} else {
				uploadStatus.setDeckInvalid(true);
			}			
		} catch (IOException e) {
			uploadStatus.setDeckInvalid(true);
		}
		
		return uploadStatus;
	}
}
