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
		return draftService.getRecentDrafts(10);
	}
	
	@RequestMapping(value = "/draft/{draftId}",
					method = RequestMethod.GET)
	public @ResponseBody JsonDraft getDraftForActivePlayer(@PathVariable final Integer draftId) {
		
		return draftService.getDraftByActivePlayer(draftId);
	}
	
	@RequestMapping(value = "/draft/{draftId}/pack/{packId}/pick/{pickId}",
					method = RequestMethod.GET)	
	public @ResponseBody JsonPackPick getPackByIdAndPick(@PathVariable final Integer draftId,
														 @PathVariable final Integer packId,
		   	   											 @PathVariable final Integer pickId) {
		
		return draftService.getPackByIdAndPick(packId, pickId);
	}
	
}


