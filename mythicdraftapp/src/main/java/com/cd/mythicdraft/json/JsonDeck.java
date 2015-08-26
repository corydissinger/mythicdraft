package com.cd.mythicdraft.json;

import java.io.Serializable;
import java.util.List;

public class JsonDeck implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private List<JsonCard> mainDeckCards;
	private List<JsonCard> sideBoardCards;
	private Integer draftId;
	
	public List<JsonCard> getMainDeckCards() {
		return mainDeckCards;
	}
	public void setMainDeckCards(List<JsonCard> mainDeckCards) {
		this.mainDeckCards = mainDeckCards;
	}
	public List<JsonCard> getSideBoardCards() {
		return sideBoardCards;
	}
	public void setSideBoardCards(List<JsonCard> sideBoardCards) {
		this.sideBoardCards = sideBoardCards;
	}
	public Integer getDraftId() {
		return draftId;
	}
	public void setDraftId(Integer draftId) {
		this.draftId = draftId;
	}
}
