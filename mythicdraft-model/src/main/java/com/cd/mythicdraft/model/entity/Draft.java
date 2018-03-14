package com.cd.mythicdraft.model.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "DRAFT")
public class Draft implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(name   = "NAME",
			length = 20)	
	private String name;
	
	@Column(name   = "EVENT_ID")
	private Integer eventId;	
	
	@Column(name = "EVENT_DATE")
	@Type(type = "timestamp")
	private Date eventDate;
	
	@Column(name = "CREATED")
	@Temporal(TemporalType.TIMESTAMP)
	private Date created;	
	
	@Column(name = "WINS")
	private Integer wins;

	@Column(name = "LOSSES")	
	private Integer losses;
	
	@OneToMany(mappedBy = "draft", fetch = FetchType.LAZY)
	@Fetch(FetchMode.JOIN)	
	private java.util.Set<DraftPlayer> draftPlayers;
	
	@OneToMany(mappedBy = "draft", fetch = FetchType.LAZY)
	@Fetch(FetchMode.JOIN)
	@OrderBy("sequenceId ASC")
	private java.util.Set<DraftPack> draftPacks;
	
	@OneToOne(mappedBy = "draft", optional = true)
	private Deck deck;

	//Attempted to make this ManyToOne, but Hibernate is a garbage JPA provider that I need to move away from
	@Column(name = "FORMAT_ID")
	private Integer formatId;

	public Draft() {}
	
	public Draft(Integer draftId) {
		id = draftId;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getEventId() {
		return eventId;
	}

	public void setEventId(Integer eventId) {
		this.eventId = eventId;
	}

	public Date getEventDate() {
		return eventDate;
	}

	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public java.util.Set<DraftPlayer> getDraftPlayers() {
		return draftPlayers;
	}

	public void addDraftPlayer(DraftPlayer draftPlayer) {
		if(draftPlayers == null){
			draftPlayers = new HashSet<DraftPlayer>();
		}

		this.draftPlayers.add(draftPlayer);		
		draftPlayer.setDraft(this);
	}

	public java.util.Set<DraftPack> getDraftPacks() {
		return draftPacks;
	}

	public void addDraftPack(DraftPack draftPack) {
		if(draftPacks == null){
			draftPacks = new HashSet<DraftPack>();
		}		
		
		this.draftPacks.add(draftPack);
		draftPack.setDraft(this);		
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Integer getWins() {
		return wins;
	}

	public void setWins(Integer wins) {
		this.wins = wins;
	}

	public Integer getLosses() {
		return losses;
	}

	public void setLosses(Integer losses) {
		this.losses = losses;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

	public Deck getDeck() {
		return deck;
	}

	public void setDeck(Deck deck) {
		this.deck = deck;
	}

	public Integer getFormatId() {
		return formatId;
	}

	public void setFormatId(Integer formatId) {
		this.formatId = formatId;
	}

}
