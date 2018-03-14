package com.cd.mythicdraft.rest.json;

import java.io.Serializable;
import java.util.List;

public class JsonFormat implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private List<String> sets;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public List<String> getSets() {
		return sets;
	}
	public void setThreeSetCode(List<String> sets) {
		this.sets = sets;
	}
}
