package com.cd.mythicdraft.batch;

import java.math.BigDecimal;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cd.mythicdraft.dao.StatsDao;
import com.cd.mythicdraft.model.Card;
import com.cd.mythicdraft.model.Format;
import com.cd.mythicdraft.model.FormatPickStats;

@Component("draftStatsProcessor")
public class StatsProcessor implements ItemProcessor<ImmutablePair<Card, Format>, FormatPickStats> {

	@Autowired
	private StatsDao statsDao;
	
	@Override
	public FormatPickStats process(ImmutablePair<Card, Format> aCardAndFormat) throws Exception {
		Card theCard = aCardAndFormat.getLeft();
		Format theFormat = aCardAndFormat.getRight();
		
		final BigDecimal theAverage = statsDao.getCardFormatAverage(theCard, theFormat);
		
		FormatPickStats stats = statsDao.getFormatPickStats(theCard, theFormat);
		
		if(theAverage != null) {
			if(stats == null || stats.getId() == null || stats.getId() < 1) {
				stats = new FormatPickStats();				
			}
			
			stats.setAvgPick(theAverage);
			stats.setCard(theCard);
			stats.setFormat(theFormat);
			
			return stats;
		}
		
		return null;
	}

}
