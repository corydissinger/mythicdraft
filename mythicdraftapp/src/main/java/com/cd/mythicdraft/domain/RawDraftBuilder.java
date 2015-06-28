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
	private Map<Integer, String> tempIdToCardNameMap;

	public RawDraft build() {
		return new RawDraft(packToListOfPickToAvailablePicksMap, 
							cardNameToTempIdMap, 
							eventId, 
							eventDate,
							activePlayer,
							otherPlayers,
							packSets,
							tempIdToCardNameMap);
	}
	
	public RawDraftBuilder setPackToListOfPickToAvailablePicksMap(Map<Integer, List<MutablePair<Integer, List<Integer>>>> packToListOfPickToAvailablePicksMap) {
		this.packToListOfPickToAvailablePicksMap = packToListOfPickToAvailablePicksMap;
		return this;
	}
	
	public RawDraftBuilder setCardNameToRawCardMap(Map<String, RawCard> cardNameToTempIdMap) {
		this.cardNameToTempIdMap = cardNameToTempIdMap;
		return this;
	}
	
	public RawDraftBuilder setEventId(Integer eventId) {
		this.eventId = eventId;
		return this;
	}
	
	public RawDraftBuilder setEventDate(Date eventDate) {
		this.eventDate = eventDate;
		return this;
	}

	public RawDraftBuilder setActivePlayer(String activePlayer) {
		this.activePlayer = activePlayer;
		return this;
	}

	public RawDraftBuilder setOtherPlayers(List<String> otherPlayers) {
		this.otherPlayers = otherPlayers;
		return this;
	}

	public RawDraftBuilder setPackSets(List<String> packSets) {
		this.packSets = packSets;
		return this;
	}

	public RawDraftBuilder setTempIdToCardNameMap(Map<Integer, String> tempIdToCardNameMap) {
		this.tempIdToCardNameMap = tempIdToCardNameMap;
		return this;
	}
	
}
