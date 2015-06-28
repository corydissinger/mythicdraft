package com.cd.mythicdraft.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.cd.mythicdraft.model.DraftPlayer.DraftPlayerId;

@Entity
@Table(name = "DRAFT_PLAYER")
@IdClass(DraftPlayerId.class)
public class DraftPlayer implements Serializable {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings(value = { "unused", "serial" })
	public static class DraftPlayerId implements Serializable {
		private Integer playerId;
		private Integer draftId;
		private Boolean isActivePlayer;
		
		public DraftPlayerId() {}
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

	@ManyToOne(optional = false)
	@JoinColumn(name = "DRAFT_ID",
		    	insertable = false,
		    	updatable = false)
	private Draft draft;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "PLAYER_ID",
		    	insertable = false,
		    	updatable = false)
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

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		DraftPlayer other = (DraftPlayer) obj;
		
		if(other.getDraft() == draft &&
		   other.getIsActivePlayer() == isActivePlayer &&
		   other.getPlayer() == player) {
			return true;
		}
		
		return false;
	}	
	
}
