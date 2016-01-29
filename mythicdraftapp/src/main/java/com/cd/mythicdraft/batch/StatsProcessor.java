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
public class StatsProcessor implements ItemProcessor<Object[], FormatPickStats> {

	@Autowired
	private StatsDao statsDao;
	
	@Override
	public FormatPickStats process(Object[] aCardAndFormat) throws Exception {
		Integer theCardId = (Integer)aCardAndFormat[0];
		Integer theFormatId = (Integer)aCardAndFormat[0];
		
		Card theCard = new Card();
		Format theFormat = new Format();
		
		theCard.setId(theCardId);
		theFormat.setId(theFormatId);
		
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
