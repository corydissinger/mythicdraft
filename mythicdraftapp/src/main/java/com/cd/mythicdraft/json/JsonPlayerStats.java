package com.cd.mythicdraft.json;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class JsonPlayerStats implements Serializable {

	private static final long serialVersionUID = 1L;

	private JsonPlayer player;
	private List<JsonDraft> drafts;
	private BigDecimal winPercentage;
	public JsonPlayer getPlayer() {
		return player;
	}
	public void setPlayer(JsonPlayer player) {
		this.player = player;
	}
	public List<JsonDraft> getDrafts() {
		return drafts;
	}
	public void setDrafts(List<JsonDraft> drafts) {
		this.drafts = drafts;
	}
	public BigDecimal getWinPercentage() {
		return winPercentage;
	}
	public void setWinPercentage(BigDecimal winPercentage) {
		this.winPercentage = winPercentage;
	}
}
