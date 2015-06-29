package com.cd.mythicdraft.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cd.mythicdraft.dao.DraftDAO;
import com.cd.mythicdraft.model.Draft;

@Controller
@RequestMapping("/draft")
public class DraftController {

	@Autowired
	private DraftDAO draftDao;
	
	@RequestMapping(value = "/{draftId}/player/{playerId}",
					method = RequestMethod.GET)
	public @ResponseBody Draft getDraftForActivePlayer(@PathVariable Integer draftId,
													   @PathVariable Integer playerId) {
		
		return draftDao.getDraftByActivePlayer(draftId, playerId);
	}
	
}
