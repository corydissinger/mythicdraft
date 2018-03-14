package com.cd.mythicdraft.rest.domain;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.MutablePair;

public class RawDeck {

	private final List<MutablePair<Integer, Integer>> listOfMainDeckCards;
	private final List<MutablePair<Integer, Integer>> listOfSideBoardCards;
	
	private final Map<String, Integer> cardNameToTempIdMap;

	public RawDeck(final List<MutablePair<Integer, Integer>> listOfMainDeckCards,
				   final List<MutablePair<Integer, Integer>> listOfSideBoardCards,
			   	   final Map<String, Integer> cardNameToTempIdMap) {
		
		this.listOfMainDeckCards = listOfMainDeckCards;
		this.listOfSideBoardCards = listOfSideBoardCards;
		this.cardNameToTempIdMap = cardNameToTempIdMap;
	}

	public List<MutablePair<Integer, Integer>> getListOfMainDeckCards() {
		return listOfMainDeckCards;
	}

	public List<MutablePair<Integer, Integer>> getListOfSideBoardCards() {
		return listOfSideBoardCards;
	}

	public Map<String, Integer> getCardNameToTempIdMap() {
		return cardNameToTempIdMap;
	}
	
}
