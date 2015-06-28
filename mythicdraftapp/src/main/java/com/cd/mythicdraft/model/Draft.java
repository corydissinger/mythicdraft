package com.cd.mythicdraft.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
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
	
	@OneToMany(mappedBy = "draft")
	private List<DraftPlayer> draftPlayers;
	
	@OneToMany(mappedBy = "draft")
	private List<DraftPack> draftPacks;
	
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

	public List<DraftPlayer> getDraftPlayers() {
		return draftPlayers;
	}

	public void addDraftPlayer(DraftPlayer draftPlayer) {
		if(draftPlayers == null){
			draftPlayers = new ArrayList<DraftPlayer>();
		}

		this.draftPlayers.add(draftPlayer);		
		draftPlayer.setDraft(this);
	}

	public List<DraftPack> getDraftPacks() {
		return draftPacks;
	}

	public void addDraftPack(DraftPack draftPack) {
		if(draftPacks == null){
			draftPacks = new ArrayList<DraftPack>();
		}		
		
		this.draftPacks.add(draftPack);
		draftPack.setDraft(this);		
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
	}	
}
