package com.cd.mythicdraft.batch;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.cd.mythicdraft.dao.StatsDao;
import com.cd.mythicdraft.model.entity.FormatPickStats;

public class StatsWriter implements ItemWriter<List<FormatPickStats>> {

	@Autowired
	private StatsDao statsDao;

	@Override
	public void write(List<? extends List<FormatPickStats>> theFormatPickStats) throws Exception {
		for(List<FormatPickStats> aFormatPickStatsList : theFormatPickStats) {
			for(FormatPickStats stats : aFormatPickStatsList) {
				if(stats.getCard() == null){
					int i = 0;
					i++;
				}
				
				statsDao.addFormatPickStats(stats);				
			}
		}
	}

}
