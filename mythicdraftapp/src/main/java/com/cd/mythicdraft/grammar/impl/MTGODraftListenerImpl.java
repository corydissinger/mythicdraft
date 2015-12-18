package com.cd.mythicdraft.grammar.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.cd.mythicdraft.domain.RawCard;

@Scope("prototype")
@Component("mtgoDraftListener")
public class MTGODraftListenerImpl {

	private static final Logger logger = Logger.getLogger(MTGODraftListenerImpl.class);	
	
	private static final String SELECTED = "--> ";
	private static final String EVENT = "Event #: ";
	private static final String TIME = "Time:    ";
	private static final String PLAYERS = "Players:";
	
	private List<String> otherPlayers;
	private String activePlayer;
	private List<String> packSets;
	private String eventDate;
	private String eventId;
	
	private Map<String, RawCard> cardNameToRawCardMap;
	private Map<Integer, String> tempIdToCardNameMap;
	private int uniqueCardCount = 0;
	private int currentPackNumber = 0;
	
	//Kill me
	private Map<Integer, List<MutablePair<Integer, List<Integer>>>> packToListOfPickToAvailablePicksMap;
	
	private List<MutablePair<Integer, List<Integer>>> currentListOfPicks;
	private MutablePair<Integer, List<Integer>> currentPairOfPickToAvailablePicks;
	private ArrayList<Integer> currentAvailablePicks;

	//Crappy flags
	private boolean isHandlingPlayers = false;
	private boolean isHandlingPackPick = false;
	
	public void handleLine(String line) {
		if(line.startsWith("--:") || line.startsWith("\r") || line.startsWith("\n") || line.isEmpty()) {
			isHandlingPlayers = false;
			isHandlingPackPick = false;
			return;
		}
		
		if(isHandlingPlayers) {
			if(line.startsWith(SELECTED)) {
				activePlayer = line.split(SELECTED)[1];
			} else {
				otherPlayers.add(line.trim());
			}
		} else if(isHandlingPackPick){
			if(line.startsWith(SELECTED)) {
				Integer cardId = getCardIdAndAddCardName(line.split(SELECTED)[1].trim()).get(0);
				
				currentPairOfPickToAvailablePicks.setLeft(cardId);				
			} else {
				currentAvailablePicks.addAll(getCardIdAndAddCardName(line.trim()));				
			}
		} else {
			if (line.startsWith(EVENT)){
				eventId = line.split(EVENT)[1];
			} else if (line.startsWith(TIME)){
				eventDate = line.split(TIME)[1];
			} else if (line.startsWith(PLAYERS)){
				isHandlingPlayers = true;
				return;
			} else if(line.startsWith("------")) {
				final String setCode = line.split("------")[1];
				packSets.add(setCode);
			} else if(line.startsWith("Pack ")) {
				final String packNumberPick = line.split("Pack ")[1];
				currentPackNumber = Integer.parseInt(packNumberPick.substring(0, 1)) - 1;
				currentListOfPicks = packToListOfPickToAvailablePicksMap.get(currentPackNumber);
				currentPairOfPickToAvailablePicks = new MutablePair<Integer, List<Integer>>();
				currentAvailablePicks = new ArrayList<Integer>();
				currentPairOfPickToAvailablePicks.setRight(currentAvailablePicks);
				currentListOfPicks.add(currentPairOfPickToAvailablePicks);
				
				isHandlingPackPick = true;
			}
		}
	}	
	
	public void reset() {
		otherPlayers = new ArrayList<String>(7);
		activePlayer = "";
		packSets = new ArrayList<String>(3);
		eventDate = "";
		eventId = "";
		cardNameToRawCardMap = new HashMap<String, RawCard>();
		uniqueCardCount = 0;
		currentPackNumber = 0;
		
		packToListOfPickToAvailablePicksMap = new HashMap<Integer, List<MutablePair<Integer, List<Integer>>>>(3);
		tempIdToCardNameMap = new HashMap<Integer, String>();
		
		for(int i = 0; i < 3; i++) {
			List<MutablePair<Integer, List<Integer>>> listOfPackPicks = new ArrayList<MutablePair<Integer, List<Integer>>>(15);
			packToListOfPickToAvailablePicksMap.put(i, listOfPackPicks);
		}
	}
	
	public void cleanup() {
		cardNameToRawCardMap = null;
		packToListOfPickToAvailablePicksMap = null;
		currentListOfPicks = null;
		currentPairOfPickToAvailablePicks = null;
		currentAvailablePicks = null;
		tempIdToCardNameMap = null;
	}

	public List<String> getOtherPlayers() {
		return otherPlayers;
	}

	public String getActivePlayer() {
		return activePlayer;
	}

	public List<String> getPackSets() {
		return packSets;
	}

	public String getEventDate() {
		return eventDate;
	}

	public String getEventId() {
		return eventId;
	}

	public Map<String, RawCard> getCardNameToTempIdMap() {
		return cardNameToRawCardMap;
	}

	public Map<Integer, List<MutablePair<Integer, List<Integer>>>> getPackToListOfPickToAvailablePicksMap() {
		return packToListOfPickToAvailablePicksMap;
	}	
	
	private List<Integer> getCardIdAndAddCardName(String aRawCardName) {
		List<Integer> tempIds = new ArrayList<Integer>(2);
		//Sinister hack. The split cards, unfortunately, are inconsistently entered with either the left or right half.
		String [] cardNames = aRawCardName.split("/");
		final List<RawCard> rawCards = new ArrayList<RawCard>(cardNames.length);
		
		for(String cardName : cardNames) {
			RawCard rawCard = cardNameToRawCardMap.get(cardName);
			
			if(rawCard != null) {						
				rawCards.add(rawCard);
				tempIds.add(rawCard.getTempId());
			}
		}		
		
		if(CollectionUtils.isEmpty(rawCards)) {
			for(String cardName : cardNames) {
				RawCard rawCard = new RawCard(uniqueCardCount, packSets.get(currentPackNumber));
				cardNameToRawCardMap.put(cardName, rawCard);
				tempIdToCardNameMap.put(uniqueCardCount++, cardName);
				tempIds.add(rawCard.getTempId());
			}
		}
		
		return tempIds;
	}

	public Map<Integer, String> getTempIdToCardNameMap() {
		return tempIdToCardNameMap;
	}

}
