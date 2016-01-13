package com.cd.mythicdraft.dao;

import java.math.BigDecimal;

import com.cd.mythicdraft.model.Card;
import com.cd.mythicdraft.model.Format;
import com.cd.mythicdraft.model.FormatPickStats;

public interface StatsDao {

	public void addFormat(final Format aFormat);
	
	public void addFormatPickStats(final FormatPickStats aFormatPickStats);
	
	public BigDecimal getCardFormatAverage(final Card aCard, final Format aFormat);
}
