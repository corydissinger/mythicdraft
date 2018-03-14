package com.cd.mythicdraft.rest.json;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class JsonDraft implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String name;
	private Integer eventId;	
	private String eventDate;
	private String created;
	private Integer wins;
	private Integer losses;
	private Integer deckId;
	
	private JsonPlayer activePlayer;
	private List<JsonPlayer> players;
	private List<JsonPack> packs;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getEventId() {
		return eventId;
	}
	public void setEventId(Integer eventId) {
		this.eventId = eventId;
	}
	public String getEventDate() {
		return eventDate;
	}
	public void setEventDate(String eventDate) {
		this.eventDate = eventDate;
	}
	public String getCreated() {
		return created;
	}
	public void setCreated(String created) {
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
	public JsonPlayer getActivePlayer() {
		return activePlayer;
	}
	public void setActivePlayer(JsonPlayer activePlayer) {
		this.activePlayer = activePlayer;
	}
	public List<JsonPlayer> getPlayers() {
		return players;
	}
	public void setPlayers(List<JsonPlayer> players) {
		this.players = players;
	}
	public List<JsonPack> getPacks() {
		return packs;
	}
	public void setPacks(List<JsonPack> packs) {
		this.packs = packs;
	}
	public Integer getDeckId() {
		return deckId;
	}
	public void setDeckId(Integer deckId) {
		this.deckId = deckId;
	}	
	
}
