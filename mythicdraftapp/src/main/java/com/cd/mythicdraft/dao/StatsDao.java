package com.cd.mythicdraft.dao;

import java.math.BigDecimal;
import java.util.List;

import com.cd.mythicdraft.model.Card;
import com.cd.mythicdraft.model.Format;
import com.cd.mythicdraft.model.FormatPickStats;

public interface StatsDao {

	void addFormatPickStats(final FormatPickStats aFormatPickStats);
	
	BigDecimal getCardFormatAverage(final Card aCard, final Format aFormat);
	
	FormatPickStats getFormatPickStats(final Card aCard, final Format aFormat);

	List<Format> getFormats();

	Format getFormat(int formatId);

	List<FormatPickStats> getFormatPickStats(int formatId);

	List<FormatPickStats> getFormatPickStats(Format theFormat);

	Integer getFormatSampleSize(int formatId);
}
