package com.cd.mythicdraft.json;

import java.io.Serializable;

import com.cd.mythicdraft.model.Color;

public class JsonCard implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	private Integer multiverseId;
	private Color color;
	private Integer cmc;
	private Boolean isCreature;
	private Boolean isNonCreature;
	private Integer count;
	private String name;
	
	public Integer getMultiverseId() {
		return multiverseId;
	}

	public void setMultiverseId(Integer multiverseId) {
		this.multiverseId = multiverseId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Integer getCmc() {
		return cmc;
	}

	public void setCmc(Integer cmc) {
		this.cmc = cmc;
	}

	public Boolean getIsCreature() {
		return isCreature;
	}

	public void setIsCreature(Boolean isCreature) {
		this.isCreature = isCreature;
	}

	public Boolean getIsNonCreature() {
		return isNonCreature;
	}

	public void setIsNonCreature(Boolean isNonCreature) {
		this.isNonCreature = isNonCreature;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}	
	
}
