package com.cd.mythicdraft.domain;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.MutablePair;

public class RawDraftBuilder {

	//Look at all those right arrows
	private Map<Integer, List<MutablePair<Integer, List<Integer>>>> packToListOfPickToAvailablePicksMap;
	private Map<String, RawCard> cardNameToTempIdMap;
	private Integer eventId;
	private Date eventDate;
	private String activePlayer;
	private List<String> otherPlayers;	
	private List<String> packSets;

	public RawDraft build() {
		return new RawDraft(packToListOfPickToAvailablePicksMap, 
							cardNameToTempIdMap, 
							eventId, 
							eventDate,
							activePlayer,
							otherPlayers,
							packSets);
	}
	
	public void setPackToListOfPickToAvailablePicksMap(
			Map<Integer, List<MutablePair<Integer, List<Integer>>>> packToListOfPickToAvailablePicksMap) {
		this.packToListOfPickToAvailablePicksMap = packToListOfPickToAvailablePicksMap;
	}
	public void setCardNameToRawCardMap(Map<String, RawCard> cardNameToTempIdMap) {
		this.cardNameToTempIdMap = cardNameToTempIdMap;
	}
	public void setEventId(Integer eventId) {
		this.eventId = eventId;
	}
	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}

	public void setActivePlayer(String activePlayer) {
		this.activePlayer = activePlayer;
	}

	public void setOtherPlayers(List<String> otherPlayers) {
		this.otherPlayers = otherPlayers;
	}

	public void setPackSets(List<String> packSets) {
		this.packSets = packSets;
	}
	
}
