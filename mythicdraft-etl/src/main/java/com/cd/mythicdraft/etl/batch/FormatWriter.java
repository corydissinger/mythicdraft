package com.cd.mythicdraft.batch;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.cd.mythicdraft.dao.DraftDao;
import com.cd.mythicdraft.model.entity.Format;

public class FormatWriter implements ItemWriter<Format> {

	@Autowired
	private DraftDao draftDao;

	private List<Format> addedFormats;
	
	@Override
	public void write(List<? extends Format> theFormats) throws Exception {
		for(Format aFormat : theFormats) {
			if(addedFormats.contains(aFormat)) {
				continue;
			}
			
			draftDao.addFormat(aFormat);
			
			addedFormats.add(aFormat);
		}
	}
	
	@BeforeStep
	public void initList() {
		addedFormats = new ArrayList<Format>();
	}
	
	@AfterStep
	public void clearList() {
		addedFormats.clear();
		addedFormats = null;
	}

}
