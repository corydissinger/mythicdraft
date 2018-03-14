package com.cd.mythicdraft.model.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "DECK")
public class Deck implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@Column(name = "DRAFT_ID",
			insertable = false,
			updatable = false)
	private Integer draftId;	
	
	@OneToOne(optional = false)
	@JoinColumn(name = "DRAFT_ID")
	private Draft draft;

	@OneToMany(mappedBy = "deck", fetch = FetchType.LAZY)
	private Set<DeckCard> deckCards;	
	
	public void addDeckCard(DeckCard deckCard) {
		if(deckCards == null){
			deckCards = new HashSet<DeckCard>();
		}

		this.deckCards.add(deckCard);		
		deckCard.setDeck(this);
	}	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getDraftId() {
		return draftId;
	}

	public void setDraftId(Integer draftId) {
		this.draftId = draftId;
	}

	public Draft getDraft() {
		return draft;
	}

	public void setDraft(Draft draft) {
		this.draft = draft;
	}

	public Set<DeckCard> getDeckCards() {
		return deckCards;
	}

	public void setDeckCards(Set<DeckCard> deckCards) {
		this.deckCards = deckCards;
	}
}
