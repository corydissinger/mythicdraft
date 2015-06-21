package com.cd.mythicdraft.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
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
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name   = "NAME",
			length = 20)	
	private String name;
	
	@Column(name   = "EVENT_ID")
	private Integer eventId;	
	
	@Column(name = "EVENT_DATE")
	@Type(type = "timestamp")
	private Date eventDate;
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<DraftPlayer> draftPlayers;
	
	@OneToMany(cascade = CascadeType.ALL)
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

	public void setDraftPlayers(List<DraftPlayer> draftPlayers) {
		this.draftPlayers = draftPlayers;
	}

	public List<DraftPack> getDraftPacks() {
		return draftPacks;
	}

	public void setDraftPacks(List<DraftPack> draftPacks) {
		this.draftPacks = draftPacks;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
	}	
}
