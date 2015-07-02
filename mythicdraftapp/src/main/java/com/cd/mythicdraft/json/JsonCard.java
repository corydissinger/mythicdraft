package com.cd.mythicdraft.json;

import java.io.Serializable;

public class JsonCard implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer multiverseId;

	public Integer getMultiverseId() {
		return multiverseId;
	}

	public void setMultiverseId(Integer multiverseId) {
		this.multiverseId = multiverseId;
	}
	
}
