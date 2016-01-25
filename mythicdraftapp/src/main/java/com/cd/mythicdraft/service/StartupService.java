package com.cd.mythicdraft.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import com.cd.mythicdraft.dao.CardDao;
import com.cd.mythicdraft.json.MtgJsonCard;
import com.cd.mythicdraft.json.MtgJsonSet;
import com.cd.mythicdraft.model.Card;
import com.cd.mythicdraft.model.Color;
import com.cd.mythicdraft.model.Set;

@Service("startupService")
@Transactional
public class StartupService implements ApplicationListener<ContextRefreshedEvent> {

	private static final Logger logger = Logger.getLogger(StartupService.class);	
	
	private static final String MTG_JSON_COM_ROOT = "http://mtgjson.com/json/";
	private static final String DOT_JSON = ".json";	
	private static final String SET_CODES_JSON = MTG_JSON_COM_ROOT + "SetCodes" + DOT_JSON;

	@Autowired
	private Environment environment;
	
	@Autowired
	private CardDao cardDao;
	
	@Autowired
	private JobLauncher jobLauncher;
	
	@Autowired
	private Job statsJob;	
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent contextRefreshed) {
		try {
			//Get sets from mtgjson.com
			updateSets();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void updateSets() throws Exception {
		RestTemplate restTemplate = new RestTemplate();		
		
		final List<Set> unknownSets = checkSetsToUpdate(restTemplate);
		
		cardDao.persistSets(unknownSets);
		
		for(Set unknownSet : unknownSets) {
			List<Card> cardsInSet = getAllCardsInSet(unknownSet, restTemplate);
			
			//Skips weird sets like RQS with no multiverse ID
			if(CollectionUtils.isEmpty(cardsInSet)) {
				continue;
			}
			
			cardDao.persistCards(cardsInSet);
		}
	}

	public List<Set> checkSetsToUpdate(RestTemplate restTemplate) throws Exception {
		String[] allSets = restTemplate.getForObject(SET_CODES_JSON, String[].class, Collections.EMPTY_MAP);
		List<String> unknownSetsList = removeSpecialSets(allSets);
		
		final List<Set> knownSets;
		
		if(Boolean.parseBoolean(environment.getProperty("force.card.update"))) {
			knownSets = new ArrayList<Set>(0);
		} else {
			knownSets = cardDao.getAvailableSets();
		}
		
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
	
	public List<String> removeSpecialSets(String [] allSets) {
		List<String> relevantSets = new ArrayList<String>();
		
		for(int i = 0; i < allSets.length; i++){
			String aSet = allSets[i];
			
			if(aSet.length() < 4) {
				relevantSets.add(aSet);
			}
		}
		
		return relevantSets;
	}
	
	public List<Card> getAllCardsInSet(Set set, RestTemplate restTemplate) {
		List<Card> cardsInSet = new ArrayList<Card>();
		
		MtgJsonSet setJson = restTemplate.getForObject(MTG_JSON_COM_ROOT + set.getName() + DOT_JSON, 
													   MtgJsonSet.class, 
													   Collections.EMPTY_MAP);
		
		List<Integer> crappySplitCardsThatSouldNotBeDuplicated = new ArrayList<Integer>();
		
		for(MtgJsonCard aCard : setJson.getCards()) {			
			//Cards that don't have multiverse IDs aren't worth the effort
			if(aCard.getMultiverseId() == null || aCard.getMultiverseId() < 1) {
				continue;
			} else if(aCard.getNames() != null && aCard.getNames().length > 1) {
				
				if(crappySplitCardsThatSouldNotBeDuplicated.contains(aCard.getMultiverseId()))
					continue;
				
				crappySplitCardsThatSouldNotBeDuplicated.add(aCard.getMultiverseId());
			}
			
			Card newCard = new Card();
			
			newCard.setId(aCard.getMultiverseId());
			newCard.setCardName(aCard.getName());
			newCard.setSet(set);
			
			//Color madness
			if(aCard.getColors() == null || aCard.getColors().length == 0) {
				newCard.setColor(Color.COLORLESS);
			} else if(aCard.getColors().length > 1) {
				newCard.setColor(Color.GOLD);
			} else {
				final Color theColor = Color.getColorFromString(aCard.getColors()[0]);
				newCard.setColor(theColor);
			}
			
			//Type hell
			final String [] types = aCard.getTypes();
			
			//We'll lump lands in with things that happen to have no types, whatever they are
			if(types == null || types.length == 0 || ArrayUtils.contains(types, "Land")) {
				newCard.setIsCreature(false);
				newCard.setIsNonCreature(false);
			} else if(ArrayUtils.contains(types, "Creature")) {
				newCard.setIsCreature(true);
				newCard.setIsNonCreature(false);				
			} else {
				newCard.setIsCreature(false);
				newCard.setIsNonCreature(true);				
			}
			
			//CMC is easy
			newCard.setCmc(aCard.getCmc() != null ? aCard.getCmc() : 0);
			
			cardsInSet.add(newCard);
		}
		
		return cardsInSet;
	}
}
