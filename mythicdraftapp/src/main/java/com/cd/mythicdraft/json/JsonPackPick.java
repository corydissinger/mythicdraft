package com.cd.mythicdraft.json;

import java.io.Serializable;
import java.util.List;

public class JsonPackPick implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private JsonCard pick;
	private List<JsonCard> available;
	
	public JsonCard getPick() {
		return pick;
	}
	public void setPick(JsonCard pick) {
		this.pick = pick;
	}
	public List<JsonCard> getAvailable() {
		return available;
	}
	public void setAvailable(List<JsonCard> available) {
		this.available = available;
	}
	
}
