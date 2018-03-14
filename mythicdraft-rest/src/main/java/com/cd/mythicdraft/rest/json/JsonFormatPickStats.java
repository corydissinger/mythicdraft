package com.cd.mythicdraft.rest.json;

import java.io.Serializable;
import java.math.BigDecimal;

public class JsonFormatPickStats implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private BigDecimal avgPick;
	private JsonCard card;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public BigDecimal getAvgPick() {
		return avgPick;
	}
	public void setAvgPick(BigDecimal avgPick) {
		this.avgPick = avgPick;
	}
	public JsonCard getCard() {
		return card;
	}
	public void setCard(JsonCard card) {
		this.card = card;
	}
}
