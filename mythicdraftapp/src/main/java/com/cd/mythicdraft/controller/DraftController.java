package com.cd.mythicdraft.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cd.mythicdraft.json.JsonDraft;
import com.cd.mythicdraft.service.DraftService;

@Controller
@RequestMapping("/draft")
public class DraftController {

	@Autowired
	private DraftService draftService;
	
	@RequestMapping(value = "/{draftId}/player/{playerId}",
					method = RequestMethod.GET)
	public @ResponseBody JsonDraft getDraftForActivePlayer(@PathVariable Integer draftId,
													   @PathVariable Integer playerId) {
		
		return draftService.getDraftByActivePlayer(draftId, playerId);
	}
	
}
