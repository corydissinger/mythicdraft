package com.cd.mythicdraft.rest.json;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MtgJsonSet {

	private String code;
	private List<MtgJsonCard> cards;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public List<MtgJsonCard> getCards() {
		return cards;
	}
	public void setCards(List<MtgJsonCard> cards) {
		this.cards = cards;
	}
	
}
