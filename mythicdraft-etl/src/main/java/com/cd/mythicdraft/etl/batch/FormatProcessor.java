package com.cd.mythicdraft.batch;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cd.mythicdraft.dao.CardDao;
import com.cd.mythicdraft.dao.DraftDao;
import com.cd.mythicdraft.model.Format;
import com.cd.mythicdraft.model.Set;

@Component("formatProcessor")
public class FormatProcessor implements ItemProcessor<Format, Format> {

	@Autowired
	private CardDao cardDao;
	
	@Autowired
	private DraftDao draftDao;	
	
	@Override
	public Format process(Format theFormat) throws Exception {
		List<Integer> setIds = new ArrayList<Integer>();
		
		setIds.add(theFormat.getFirstPack());
		setIds.add(theFormat.getSecondPack());
		setIds.add(theFormat.getThirdPack());
		
		List<Set> theSets = cardDao.getSetsById(setIds);
		
		if(theSets.size() == 1) {
			theFormat.setFirstPackSet(theSets.get(0));
			theFormat.setSecondPackSet(theSets.get(0));
			theFormat.setThirdPackSet(theSets.get(0));
		} else if(theSets.size() == 2) {
			if(theFormat.getFirstPack().equals(theFormat.getSecondPack())) {
				for(Set aSet : theSets) {
					if(aSet.getId().equals(theFormat.getFirstPack())) {
						theFormat.setFirstPackSet(aSet);
						theFormat.setSecondPackSet(aSet);
					} else if(aSet.getId().equals(theFormat.getThirdPack())) {
						theFormat.setThirdPackSet(aSet);
					} 
				}				
			} else {
				for(Set aSet : theSets) {
					if(aSet.getId().equals(theFormat.getFirstPack())) {
						theFormat.setFirstPackSet(aSet);
					} else if(aSet.getId().equals(theFormat.getThirdPack())) {
						theFormat.setSecondPackSet(aSet);						
						theFormat.setThirdPackSet(aSet);
					} 
				}					
			}
		} else {
			for(Set aSet : theSets) {
				if(aSet.getId().equals(theFormat.getFirstPack())) {
					theFormat.setFirstPackSet(aSet);
				} else if(aSet.getId().equals(theFormat.getSecondPack())) {
					theFormat.setSecondPackSet(aSet);
				} else if(aSet.getId().equals(theFormat.getThirdPack())) {
					theFormat.setThirdPackSet(aSet);
				}
			}
		}
		
		Format existingFormat = draftDao.getFormatByPacks(theFormat.getFirstPackSet(), theFormat.getSecondPackSet(), theFormat.getThirdPackSet());
		
		if(existingFormat != null) {
			return null;
		}
		
		return theFormat;
	}

}
