package com.cd.mythicdraft.grammar.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.cd.mythicdraft.domain.RawCard;
import com.cd.mythicdraft.grammar.MTGODraftParser;
import com.cd.mythicdraft.grammar.MTGODraftParser.APlayerContext;
import com.cd.mythicdraft.grammar.MTGODraftParser.AvailablePickContext;
import com.cd.mythicdraft.grammar.MTGODraftParser.CurrentPlayerContext;
import com.cd.mythicdraft.grammar.MTGODraftParser.EventIdContext;
import com.cd.mythicdraft.grammar.MTGODraftParser.PackContext;
import com.cd.mythicdraft.grammar.MTGODraftParser.PickContext;
import com.cd.mythicdraft.grammar.MTGODraftParser.SetContext;
import com.cd.mythicdraft.grammar.MTGODraftParser.TimeContext;
import com.cd.mythicdraft.grammar.MTGODraftParserBaseListener;

@Component("mtgoDraftListener")
public class MTGODraftListenerImpl extends MTGODraftParserBaseListener {

	private static final Logger logger = Logger.getLogger(MTGODraftListenerImpl.class);	
	
	private List<String> otherPlayers;
	private String activePlayer;
	private List<String> packSets;
	private String eventDate;
	private String eventId;
	
	private Map<String, RawCard> cardNameToRawCardMap;
	private int uniqueCardCount = 0;
	private int currentPackNumber = 0;
	
	//Kill me
	private Map<Integer, List<MutablePair<Integer, List<Integer>>>> packToListOfPickToAvailablePicksMap;
	
	private List<MutablePair<Integer, List<Integer>>> currentListOfPicks;
	private MutablePair<Integer, List<Integer>> currentPairOfPickToAvailablePicks;
	private ArrayList<Integer> currentAvailablePicks;
	
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
	}
	
	@Override
	public void enterEventId(EventIdContext ctx) {
		eventId = ctx.getText();
	}

	@Override
	public void enterTime(TimeContext ctx) {
		eventDate = ctx.getText();
	}

	@Override 
	public void enterCurrentPlayer(CurrentPlayerContext ctx) {
		activePlayer = ctx.getText();
	}
	
	@Override 
	public void enterAPlayer(APlayerContext ctx) { 
		otherPlayers.add(ctx.getText());
	}	
	
	@Override 
	public void enterSet(SetContext ctx) {
		packSets.add(ctx.getText());
	}

	@Override 
	public void enterPack(PackContext ctx) { 
		
	}
	
	@Override 
	public void exitPack(PackContext ctx) { 
		
	}	
	
	@Override 
	public void enterPackNumber(MTGODraftParser.PackNumberContext ctx) { 
		//Packs are 1, 2, 3 but we don't like 1-based indices
		currentPackNumber = Integer.parseInt(ctx.getText()) - 1;
		
		currentListOfPicks = packToListOfPickToAvailablePicksMap.get(currentPackNumber);
	}	
	
	@Override 
	public void enterPickNumber(MTGODraftParser.PickNumberContext ctx) {
		currentPairOfPickToAvailablePicks = new MutablePair<Integer, List<Integer>>();
		currentAvailablePicks = new ArrayList<Integer>();
		currentPairOfPickToAvailablePicks.setRight(currentAvailablePicks);
		currentListOfPicks.add(currentPairOfPickToAvailablePicks);
	}	
	
	@Override 
	public void enterPick(PickContext ctx) {
		Integer cardId = getCardIdAndAddCardName(ctx.getText());
	
		currentPairOfPickToAvailablePicks.setLeft(cardId);
	}
	
	@Override 
	public void enterAvailablePick(AvailablePickContext ctx) {
		Integer cardId = getCardIdAndAddCardName(ctx.getText());
		
		currentAvailablePicks.add(cardId);
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
	
	private Integer getCardIdAndAddCardName(String aCardName) {
		RawCard rawCard = cardNameToRawCardMap.get(aCardName);
		
		if(rawCard == null) {
			rawCard = new RawCard(uniqueCardCount++, packSets.get(currentPackNumber));
			cardNameToRawCardMap.put(aCardName, rawCard);
		}
		
		return rawCard.getTempId();
	}

}
