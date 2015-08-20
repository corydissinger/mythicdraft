package com.cd.mythicdraft.domain;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.MutablePair;

public class RawDeckBuilder {

	private List<MutablePair<Integer, Integer>> listOfMainDeckCards;
	private List<MutablePair<Integer, Integer>> listOfSideBoardCards;
	
	private Map<String, Integer> cardNameToTempIdMap;

	public RawDeck build() {
		return new RawDeck(listOfMainDeckCards, 
						   listOfSideBoardCards, 
						   cardNameToTempIdMap);
	}	
	
	public RawDeckBuilder setListOfMainDeckCards(List<MutablePair<Integer, Integer>> listOfMainDeckCards) {
		this.listOfMainDeckCards = listOfMainDeckCards;
		return this;
	}

	public RawDeckBuilder setListOfSideBoardCards(List<MutablePair<Integer, Integer>> listOfSideBoardCards) {
		this.listOfSideBoardCards = listOfSideBoardCards;
		return this;
	}

	public RawDeckBuilder setCardNameToTempIdMap(Map<String, Integer> cardNameToTempIdMap) {
		this.cardNameToTempIdMap = cardNameToTempIdMap;
		return this;
	}

	
	
}
