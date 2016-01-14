package com.cd.mythicdraft.batch;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.cd.mythicdraft.dao.StatsDao;
import com.cd.mythicdraft.model.FormatPickStats;

public class StatsWriter implements ItemWriter<FormatPickStats> {

	@Autowired
	private StatsDao statsDao;

	@Override
	public void write(List<? extends FormatPickStats> theFormatPickStats) throws Exception {
		for(FormatPickStats aFormatPickStats : theFormatPickStats) {
			statsDao.addFormatPickStats(aFormatPickStats);
		}
	}

}
