package com.cd.mythicdraft.batch;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.cd.mythicdraft.dao.DraftDao;
import com.cd.mythicdraft.model.entity.Draft;
import com.cd.mythicdraft.model.entity.DraftPack;
import com.cd.mythicdraft.model.entity.Format;
import com.cd.mythicdraft.model.entity.Set;

public class DraftFormatProcessor implements ItemProcessor<Draft, Draft> {

	@Autowired
	private DraftDao draftDao;
	
	@Override
	public Draft process(Draft theDraft) throws Exception {
		List<DraftPack> draftPacks = new ArrayList<DraftPack>(3);
		
		theDraft = draftDao.getDraftById(theDraft.getId());		
		
		draftPacks.addAll(theDraft.getDraftPacks());
		
		Set firstPack = draftPacks.get(0).getSet();
		Set secondPack = draftPacks.get(1).getSet();
		Set thirdPack = draftPacks.get(2).getSet();
		
		Format theFormat = draftDao.getFormatByPacks(firstPack, secondPack, thirdPack);
		
		theDraft.setFormatId(theFormat.getId());
		
		return theDraft;
	}

}
