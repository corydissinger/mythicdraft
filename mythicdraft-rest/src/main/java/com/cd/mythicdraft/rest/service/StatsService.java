package com.cd.mythicdraft.rest.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cd.mythicdraft.model.dao.StatsDao;
import com.cd.mythicdraft.rest.json.JsonCard;
import com.cd.mythicdraft.rest.json.JsonFormat;
import com.cd.mythicdraft.rest.json.JsonFormatPickStats;
import com.cd.mythicdraft.rest.json.JsonFormatStats;
import com.cd.mythicdraft.model.entity.Format;
import com.cd.mythicdraft.model.entity.FormatPickStats;

@Service("statsService")
public class StatsService {

	@Autowired
	private StatsDao statsDao;
	
	public List<JsonFormat> getFormats() {
		return statsDao.getFormats()
				   .stream()
				   .map(this::getJsonFormatFromFormat)
				   .collect(Collectors.toList());
	}

	public JsonFormatStats getFormatStats(int formatId) {
		JsonFormatStats stats = new JsonFormatStats();
		
		stats.setFormat(getJsonFormatFromFormat(statsDao.getFormat(formatId)));
		stats.setSampleSize(statsDao.getFormatSampleSize(formatId));
		stats.setFormatPickStats(statsDao.getFormatPickStats(formatId)
				   .stream()
				   .map(this::getJsonFormatStatsFromFormatPickStats)
				   .collect(Collectors.toList()));
		
		return stats;
	}	
	
	private JsonFormat getJsonFormatFromFormat(final Format aFormat) {
		final JsonFormat aJsonFormat = new JsonFormat();
		
		aJsonFormat.setId(aFormat.getId());
		
		final List<String> sets = new ArrayList<String>(3);
		
		sets.add(aFormat.getFirstPackSet().getName());
		sets.add(aFormat.getSecondPackSet().getName());
		sets.add(aFormat.getThirdPackSet().getName());
		
		aJsonFormat.setThreeSetCode(sets);
		
		return aJsonFormat;
	}

	private JsonFormatPickStats getJsonFormatStatsFromFormatPickStats(final FormatPickStats aFormatPickStats) {
		final JsonFormatPickStats aJsonFormatPickStats = new JsonFormatPickStats();
		
		final JsonCard aJsonCard = new JsonCard();
		
		aJsonCard.setName(aFormatPickStats.getCard().getCardName());
		
		aJsonFormatPickStats.setAvgPick(aFormatPickStats.getAvgPick());
		aJsonFormatPickStats.setId(aFormatPickStats.getId());
		aJsonFormatPickStats.setCard(aJsonCard);
		
		return aJsonFormatPickStats;
	}
}
