package com.cd.mythicdraft.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cd.mythicdraft.json.JsonDraft;
import com.cd.mythicdraft.json.JsonPackPick;
import com.cd.mythicdraft.service.DraftService;

@Controller
public class DraftController {

	@Autowired
	private DraftService draftService;
	
	@RequestMapping(value = "/draft/recent")
	public @ResponseBody List<JsonDraft> getRecentDrafts() {
		return draftService.getRecentDrafts();
	}
	
	@RequestMapping(value = "/draft/{draftId}/player/{playerId}",
					method = RequestMethod.GET)
	public @ResponseBody JsonDraft getDraftForActivePlayer(@PathVariable final Integer draftId,
													   	   @PathVariable final Integer playerId) {
		
		return draftService.getDraftByActivePlayer(draftId, playerId);
	}
	
	@RequestMapping(value = "/pack/{packId}/pick/{pickId}",
					method = RequestMethod.GET)	
	public @ResponseBody JsonPackPick getPackByIdAndPick(@PathVariable final Integer packId,
		   	   											 @PathVariable final Integer pickId) {
		
		return draftService.getPackByIdAndPick(packId, pickId);
	}
}


