package com.cd.mythicdraft.batch;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.cd.mythicdraft.dao.DraftDao;
import com.cd.mythicdraft.model.Draft;
import com.cd.mythicdraft.model.DraftPack;
import com.cd.mythicdraft.model.Format;
import com.cd.mythicdraft.model.Set;

public class DraftFormatProcessor implements ItemProcessor<Integer, Draft> {

	@Autowired
	private DraftDao draftDao;
	
	@Override
	public Draft process(Integer aDraftWithoutAFormat) throws Exception {
		Draft theDraft = draftDao.getDraftById(aDraftWithoutAFormat);
		List<DraftPack> draftPacks = new ArrayList<DraftPack>(3);
		
		draftPacks.addAll(theDraft.getDraftPacks());
		
		Set firstPack = draftPacks.get(0).getSet();
		Set secondPack = draftPacks.get(1).getSet();
		Set thirdPack = draftPacks.get(2).getSet();
		
		Format aFormat = draftDao.getFormatByPacks(firstPack, secondPack, thirdPack);
		
		theDraft.setFormatId(aFormat.getId());
		
		return theDraft;
	}

}
