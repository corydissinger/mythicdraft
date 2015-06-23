package com.cd.mythicdraft.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.cd.mythicdraft.dao.DraftDAO;
import com.cd.mythicdraft.model.Set;

@Service("mtgJsonService")
public class MtgJsonService implements ApplicationListener<ContextRefreshedEvent> {

	private static final Logger logger = Logger.getLogger(MtgJsonService.class);	
	
	private static final String MTG_JSON_COM_ROOT = "http://mtgjson.com/json/";
	private static final String SET_CODES_JSON = MTG_JSON_COM_ROOT + "SetCodes.json";
	
	@Autowired
	private DraftDAO draftDao;
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent contextRefreshed) {
		RestTemplate restTemplate = new RestTemplate();
		
		try {
			List<Set> unknownSets = checkSetsToUpdate(restTemplate);
			
			logger.debug(unknownSets.toString());
			
			draftDao.persistSets(unknownSets);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private List<Set> checkSetsToUpdate(RestTemplate restTemplate) throws Exception {
		String[] allSets = restTemplate.getForObject(SET_CODES_JSON, String[].class, Collections.EMPTY_MAP);
		List<String> unknownSetsList = removeSpecialSets(allSets);
		
		List<Set> knownSets = draftDao.getAvailableSets();
		
		for(Set aSet : knownSets) {
			unknownSetsList.remove(aSet.getName());
		}
		
		List<Set> setsToAdd = new ArrayList<Set>(unknownSetsList.size());
		
		for(String unknownSet : unknownSetsList) {
			Set newSet = new Set();
			newSet.setName(unknownSet);
			setsToAdd.add(newSet);
		}
		
		return setsToAdd; 
	}	
	
	private List<String> removeSpecialSets(String [] allSets) {
		List<String> relevantSets = new ArrayList<String>();
		
		for(int i = 0; i < allSets.length; i++){
			String aSet = allSets[i];
			
			if(aSet.length() < 4) {
				relevantSets.add(aSet);
			}
		}
		
		return relevantSets;
	}
}
