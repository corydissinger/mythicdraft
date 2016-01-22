package com.cd.mythicdraft.dao;

import java.math.BigDecimal;
import java.util.List;

import com.cd.mythicdraft.model.Card;
import com.cd.mythicdraft.model.Format;
import com.cd.mythicdraft.model.FormatPickStats;

public interface StatsDao {

	public void addFormatPickStats(final FormatPickStats aFormatPickStats);
	
	public BigDecimal getCardFormatAverage(final Card aCard, final Format aFormat);
	
	public FormatPickStats getFormatPickStats(final Card aCard, final Format aFormat);

	public List<Format> getFormats();

	public Format getFormat(int formatId);

	public List<FormatPickStats> getFormatPickStats(int formatId);
}
