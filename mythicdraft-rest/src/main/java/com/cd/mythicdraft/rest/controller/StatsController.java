package com.cd.mythicdraft.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cd.mythicdraft.rest.json.JsonFormat;
import com.cd.mythicdraft.rest.json.JsonFormatStats;
import com.cd.mythicdraft.rest.service.StatsService;

@Controller
public class StatsController {

	@Autowired
	private StatsService statsService;
	
	@RequestMapping(value = "/formats")
	public @ResponseBody List<JsonFormat> getFormats() {
		return statsService.getFormats();
	}
	
	@RequestMapping(value = "/format/{formatId}/stats")
	public @ResponseBody JsonFormatStats getFormatStats(@PathVariable final int formatId) {
		return statsService.getFormatStats(formatId);
	}	
}
