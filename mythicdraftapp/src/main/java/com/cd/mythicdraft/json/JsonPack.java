package com.cd.mythicdraft.json;

import java.io.Serializable;

public class JsonPack implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String setCode;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getSetCode() {
		return setCode;
	}
	public void setSetCode(String setCode) {
		this.setCode = setCode;
	}
}
