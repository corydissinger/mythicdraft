package com.cd.mythicdraft.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import com.cd.mythicdraft.dao.CardDao;
import com.cd.mythicdraft.json.MtgJsonCard;
import com.cd.mythicdraft.json.MtgJsonSet;
import com.cd.mythicdraft.model.Card;
import com.cd.mythicdraft.model.Color;
import com.cd.mythicdraft.model.Set;

@Service("mtgJsonService")
public class MtgJsonService implements ApplicationListener<ContextRefreshedEvent> {

	private static final Logger logger = Logger.getLogger(MtgJsonService.class);	
	
	private static final String MTG_JSON_COM_ROOT = "http://mtgjson.com/json/";
	private static final String DOT_JSON = ".json";	
	private static final String SET_CODES_JSON = MTG_JSON_COM_ROOT + "SetCodes" + DOT_JSON;

	@Autowired
	private Environment environment;
	
	@Autowired
	private CardDao cardDao;
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent contextRefreshed) {
		RestTemplate restTemplate = new RestTemplate();
		
		try {
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
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private List<Set> checkSetsToUpdate(RestTemplate restTemplate) throws Exception {
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
	
	private List<Card> getAllCardsInSet(Set set, RestTemplate restTemplate) {
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
