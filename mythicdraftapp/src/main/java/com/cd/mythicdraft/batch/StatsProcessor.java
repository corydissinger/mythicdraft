package com.cd.mythicdraft.batch;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cd.mythicdraft.dao.CardDao;
import com.cd.mythicdraft.dao.StatsDao;
import com.cd.mythicdraft.model.Card;
import com.cd.mythicdraft.model.Format;
import com.cd.mythicdraft.model.FormatPickStats;

@Component("draftStatsProcessor")
public class StatsProcessor implements ItemProcessor<Format, List<FormatPickStats>> {

	@Autowired
	private StatsDao statsDao;
	
	@Autowired
	private CardDao cardDao;
	
	@Override
	public List<FormatPickStats> process(Format theFormat) throws Exception {
		List<FormatPickStats> rawStats = statsDao.getFormatPickStats(theFormat);
		List<FormatPickStats> stats = new ArrayList<FormatPickStats>(rawStats.size());
		
		for(FormatPickStats aStatsWithoutCard : rawStats) {
			Card card = cardDao.getCardById(aStatsWithoutCard.getCardId());
			
			FormatPickStats existingStats = statsDao.getFormatPickStats(card, theFormat);
			
			if(existingStats != null) {
				stats.add(existingStats);
			} else {
				aStatsWithoutCard.setCard(card);
				stats.add(aStatsWithoutCard);
			}
		}
		
		return stats;
	}

}
