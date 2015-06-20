package com.cd.mythicdraft.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.cd.mythicdraft.model.DraftPlayers.DraftPlayersId;

@Entity
@Table(name = "DRAFT_PLAYERS")
@IdClass(DraftPlayersId.class)
public class DraftPlayers implements Serializable {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings(value = { "unused", "serial" })
	class DraftPlayersId implements Serializable {
		private Integer playerId;
		private Integer draftId;
		private Boolean isActivePlayer;		
	}
	
	@Id
	@Column(name = "PLAYER_ID",
		    insertable = false,
		    updatable = false)
	private Integer playerId;
	
	@Id
	@Column(name = "DRAFT_ID",
		    insertable = false,
		    updatable = false)
	private Integer draftId;
	
	@Id
	@Column(name = "IS_ACTIVE_PLAYER")
	private Boolean isActivePlayer;

	@ManyToOne(optional = false,
			   cascade = CascadeType.ALL)
	@JoinColumn(name = "DRAFT_ID")
	private Draft draft;
	
	@OneToOne(optional = false,
			  cascade = CascadeType.ALL)
	@JoinColumn(name = "PLAYER_ID")
	private Player player;
	
	public Integer getPlayerId() {
		return playerId;
	}

	public void setPlayerId(Integer playerId) {
		this.playerId = playerId;
	}

	public Integer getDraftId() {
		return draftId;
	}

	public void setDraftId(Integer draftId) {
		this.draftId = draftId;
	}

	public Boolean getIsActivePlayer() {
		return isActivePlayer;
	}

	public void setIsActivePlayer(Boolean isActivePlayer) {
		this.isActivePlayer = isActivePlayer;
	}

	public Draft getDraft() {
		return draft;
	}

	public void setDraft(Draft draft) {
		this.draft = draft;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
	
}
