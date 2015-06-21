package com.cd.mythicdraft.domain;

public class RawCard {

	private final Integer tempId;
	private final String setCode;
	
	public RawCard(Integer tempId, String setCode) {
		super();
		this.tempId = tempId;
		this.setCode = setCode;
	}
	
	public Integer getTempId() {
		return tempId;
	}
	public String getSetCode() {
		return setCode;
	}
	
}
