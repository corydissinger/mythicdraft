package com.cd.mythicdraft.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.cd.mythicdraft.model.DraftSets.DraftSetsId;

@Entity
@Table(name = "DRAFT_SETS")
@IdClass(DraftSetsId.class)
public class DraftSets implements Serializable {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings(value = { "unused", "serial" })
	class DraftSetsId implements Serializable {
		private Integer draftId;
		private Integer setId;
		private Integer sequenceId;		
	}	
	
	@Id
	@Column(name = "DRAFT_ID",
			insertable = false,
			updatable = false)
	private Integer draftId;

	@Id
	@Column(name = "SET_ID",
			insertable = false,
			updatable = false)	
	private Integer setId;
	
	@Id
	@Column(name = "SEQUENCE_ID")
	private Integer sequenceId;
	
	@ManyToOne(optional = false,
			   cascade = CascadeType.ALL)
	@JoinColumn(name = "DRAFT_ID")
	private Draft draft;
	
	@ManyToOne(optional = false,
			   cascade = CascadeType.ALL)
	@JoinColumn(name = "SET_ID")
	private Set set;

	public Integer getDraftId() {
		return draftId;
	}

	public void setDraftId(Integer draftId) {
		this.draftId = draftId;
	}

	public Integer getSetId() {
		return setId;
	}

	public void setSetId(Integer setId) {
		this.setId = setId;
	}

	public Integer getSequenceId() {
		return sequenceId;
	}

	public void setSequenceId(Integer sequenceId) {
		this.sequenceId = sequenceId;
	}

	public Draft getDraft() {
		return draft;
	}

	public void setDraft(Draft draft) {
		this.draft = draft;
	}

	public Set getSet() {
		return set;
	}

	public void setSet(Set set) {
		this.set = set;
	}	
	
}
