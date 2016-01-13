package com.cd.mythicdraft.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "FORMAT_PICK_STATS")
public class FormatPickStats implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "ID")
	private Integer id;
	
	@Column(name = "AVG_PICK")
	private BigDecimal avgPick;
	
	@Column(name = "FORMAT_ID",
			insertable = false,
			updatable = false)
	private Integer formatId;	
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "FORMAT_ID")
	private Format format;
	
	@Column(name = "CARD_ID",
			insertable = false,
			updatable = false)
	private Integer cardId;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "CARD_ID")
	private Card card;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public BigDecimal getAvgPick() {
		return avgPick;
	}

	public void setAvgPick(BigDecimal avgPick) {
		this.avgPick = avgPick.setScale(1, RoundingMode.HALF_UP);
	}

	public Integer getFormatId() {
		return formatId;
	}

	public void setFormatId(Integer formatId) {
		this.formatId = formatId;
	}

	public Format getFormat() {
		return format;
	}

	public void setFormat(Format format) {
		this.format = format;
	}

	public Integer getCardId() {
		return cardId;
	}

	public void setCardId(Integer cardId) {
		this.cardId = cardId;
	}

	public Card getCard() {
		return card;
	}

	public void setCard(Card card) {
		this.card = card;
	}	
}
