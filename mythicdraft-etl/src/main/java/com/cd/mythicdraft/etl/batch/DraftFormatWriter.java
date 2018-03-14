package com.cd.mythicdraft.batch;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.cd.mythicdraft.dao.DraftDao;
import com.cd.mythicdraft.model.entity.Draft;

public class DraftFormatWriter implements ItemWriter<Draft> {

	@Autowired
	private DraftDao draftDao;

	@Override
	public void write(List<? extends Draft> theDrafts) throws Exception {
		for(Draft aDraft : theDrafts) {
			draftDao.updateDraft(aDraft);
		}
	}

}
