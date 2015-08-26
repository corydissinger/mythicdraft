package com.cd.mythicdraft.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
@Table(name = "CARD")
public class Card implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "ID")
	private Integer id;	
	
	@Column(name = "NAME")
	private String cardName;
	
	@Column(name = "CMC")
	private Integer cmc;
	
	@Column(name = "IS_CREATURE")
	private Boolean isCreature;
	
	@Column(name = "IS_NON_CREATURE")
	private Boolean isNonCreature;
	
	@Column(name = "SET_ID",
			insertable = false,
			updatable = false)
	private Integer setId;
	
	@ManyToOne
	@JoinColumn(name = "SET_ID")
	private Set set;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCardName() {
		return cardName;
	}

	public void setCardName(String cardName) {
		this.cardName = cardName;
	}

	public Integer getSetId() {
		return setId;
	}

	public void setSetId(Integer setId) {
		this.setId = setId;
	}

	public Set getSet() {
		return set;
	}

	public void setSet(Set set) {
		this.set = set;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
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
}
