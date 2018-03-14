package com.cd.mythicdraft.model.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
@Table(name = "DRAFT_PACK_AVAILABLE_PICK")
public class DraftPackAvailablePick implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@Column(name = "CARD_ID",
			insertable = false,
			updatable = false)	
	private Integer cardId;
	
	@Column(name = "DRAFT_PACK_PICK_ID",
			insertable = false,
			updatable = false)
	private Integer draftPackPickId;

	@ManyToOne
    @JoinColumn(name = "DRAFT_PACK_PICK_ID")	                
	private DraftPackPick draftPackPick;	
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "CARD_ID")
	private Card card;	
	
	public Integer getCardId() {
		return cardId;
	}

	public void setCardId(Integer cardId) {
		this.cardId = cardId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getDraftPackPickId() {
		return draftPackPickId;
	}

	public void setDraftPackPickId(Integer draftPackPickId) {
		this.draftPackPickId = draftPackPickId;
	}

	public DraftPackPick getDraftPackPick() {
		return draftPackPick;
	}

	public void setDraftPackPick(DraftPackPick draftPackPick) {
		this.draftPackPick = draftPackPick;
	}

	public Card getCard() {
		return card;
	}

	public void setCard(Card card) {
		this.card = card;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
	}	
}
