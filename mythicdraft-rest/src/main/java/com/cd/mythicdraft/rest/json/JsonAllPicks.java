package com.cd.mythicdraft.rest.json;

import java.io.Serializable;
import java.util.List;

public class JsonAllPicks implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Integer> allCards;
	private List<Integer> picksInOrder;
	private JsonDraft draftMetaData;
	
	public List<Integer> getAllCards() {
		return allCards;
	}
	public void setAllCards(List<Integer> allCards) {
		this.allCards = allCards;
	}
	public List<Integer> getPicksInOrder() {
		return picksInOrder;
	}
	public void setPicksInOrder(List<Integer> picksInOrder) {
		this.picksInOrder = picksInOrder;
	}
	public JsonDraft getDraftMetaData() {
		return draftMetaData;
	}
	public void setDraftMetaData(JsonDraft draftMetaData) {
		this.draftMetaData = draftMetaData;
	}
	
}
