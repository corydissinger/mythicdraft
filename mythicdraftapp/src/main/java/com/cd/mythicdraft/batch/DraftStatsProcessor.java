package com.cd.mythicdraft.batch;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cd.mythicdraft.dao.StatsDao;
import com.cd.mythicdraft.model.Card;
import com.cd.mythicdraft.model.Format;
import com.cd.mythicdraft.model.FormatPickStats;

@Component("draftStatsProcessor")
public class DraftStatsProcessor implements ItemProcessor<ImmutablePair<Card, Format>, FormatPickStats> {

	@Autowired
	private StatsDao statsDao;
	
	@Override
	public FormatPickStats process(ImmutablePair<Card, Format> aCardAndFormat) throws Exception {
		return null;
	}

}
