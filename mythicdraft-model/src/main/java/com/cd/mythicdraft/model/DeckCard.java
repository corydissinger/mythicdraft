package com.cd.mythicdraft.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "DECK_CARD")
public class DeckCard implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@Column(name = "DECK_ID",
			insertable = false,
			updatable = false)
	private Integer deckId;
	
	@Column(name = "CARD_ID",
			insertable = false,
			updatable = false)
	private Integer cardId;		
	
	@Column(name = "IS_MAIN_DECK")
	private Boolean isMainDeck;
	
	@Column(name = "COUNT")
	private Integer count;

	@ManyToOne(optional = false)
	@JoinColumn(name = "DECK_ID")
	private Deck deck;	
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "CARD_ID")
	private Card card;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getDeckId() {
		return deckId;
	}

	public void setDeckId(Integer deckId) {
		this.deckId = deckId;
	}

	public Integer getCardId() {
		return cardId;
	}

	public void setCardId(Integer cardId) {
		this.cardId = cardId;
	}

	public Boolean getIsMainDeck() {
		return isMainDeck;
	}

	public void setIsMainDeck(Boolean isMainDeck) {
		this.isMainDeck = isMainDeck;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Deck getDeck() {
		return deck;
	}

	public void setDeck(Deck deck) {
		this.deck = deck;
	}

	public Card getCard() {
		return card;
	}

	public void setCard(Card card) {
		this.card = card;
	}	
}
