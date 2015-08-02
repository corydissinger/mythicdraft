package com.cd.mythicdraft.json;

import java.io.Serializable;
import java.util.List;

public class JsonPackPick implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private JsonDraft draftMetaData;
	private String pick;
	private List<JsonCard> available;
	
	public JsonDraft getDraftMetaData() {
		return draftMetaData;
	}
	public void setDraftMetaData(JsonDraft draftMetaData) {
		this.draftMetaData = draftMetaData;
	}
	public String getPick() {
		return pick;
	}
	public void setPick(String pick) {
		this.pick = pick;
	}
	public List<JsonCard> getAvailable() {
		return available;
	}
	public void setAvailable(List<JsonCard> available) {
		this.available = available;
	}
	
}
