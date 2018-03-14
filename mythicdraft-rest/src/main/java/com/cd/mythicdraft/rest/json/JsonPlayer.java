package com.cd.mythicdraft.rest.json;

import java.io.Serializable;

public class JsonPlayer implements Serializable {

	private static final long serialVersionUID = 6447114249542382740L;
	
	private Integer id;
	private String name;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
