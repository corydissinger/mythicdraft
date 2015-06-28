package com.cd.mythicdraft.domain;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.MutablePair;

public class RawDraft {

	private final Map<Integer, List<MutablePair<Integer, List<Integer>>>> packToListOfPickToAvailablePicksMap;
	private final Map<String, RawCard> cardNameToRawCardMap;
	private final Integer eventId;
	private final Date eventDate;
	private final String activePlayer;
	private final List<String> otherPlayers;
	private final List<String> packSets;
	private final Map<Integer, String> tempIdToCardNameMap;
	
	public RawDraft(final Map<Integer, List<MutablePair<Integer, List<Integer>>>> packToListOfPickToAvailablePicksMap,
					final Map<String, RawCard> cardNameToTempIdMap, 
					final Integer eventId,
					final Date eventDate,
					final String activePlayer,
					final List<String> otherPlayers,
					final List<String> packSets,
					final Map<Integer, String> tempIdToCardNameMap) {
		this.packToListOfPickToAvailablePicksMap = packToListOfPickToAvailablePicksMap;
		this.cardNameToRawCardMap = cardNameToTempIdMap;
		this.eventId = eventId;
		this.eventDate = eventDate;
		this.activePlayer = activePlayer;
		this.otherPlayers = otherPlayers;		
		this.packSets = packSets;
		this.tempIdToCardNameMap = tempIdToCardNameMap;
	}

	public Map<Integer, List<MutablePair<Integer, List<Integer>>>> getPackToListOfPickToAvailablePicksMap() {
		return packToListOfPickToAvailablePicksMap;
	}
	
	public Map<String, RawCard> getCardNameToRawCardMap() {
		return cardNameToRawCardMap;
	}

	public Integer getEventId() {
		return eventId;
	}

	public Date getEventDate() {
		return eventDate;
	}

	public String getActivePlayer() {
		return activePlayer;
	}

	public List<String> getOtherPlayers() {
		return otherPlayers;
	}

	public List<String> getPackSets() {
		return packSets;
	}

	public Map<Integer, String> getTempIdToCardNameMap() {
		return tempIdToCardNameMap;
	}
	
}
