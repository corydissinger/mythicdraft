package com.cd.mythicdraft.model.entity;

import java.io.Serializable;
import java.util.HashSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
@Table(name = "DRAFT_PACK")
public class DraftPack implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@Column(name = "DRAFT_ID",
			insertable = false,
			updatable = false)
	private Integer draftId;

	@Column(name = "SET_ID",
			insertable = false,
			updatable = false)	
	private Integer setId;
	
	@Column(name = "SEQUENCE_ID")
	private Integer sequenceId;
	
	@Column(name = "PACK_SIZE")
	private Integer packSize;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "DRAFT_ID")
	private Draft draft;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "SET_ID")
	private Set set;
	
	@OneToMany(cascade = CascadeType.ALL, 
			   mappedBy = "draftPack")
	private java.util.Set<DraftPackPick> draftPackPicks;	

	public Integer getDraftId() {
		return draftId;
	}

	public void setDraftId(Integer draftId) {
		this.draftId = draftId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Integer getPackSize() {
		return packSize;
	}

	public void setPackSize(Integer packSize) {
		this.packSize = packSize;
	}

	public Draft getDraft() {
		return draft;
	}

	public void setDraft(Draft draft) {
		if(!draft.getDraftPacks().contains(this)) {
			draft.addDraftPack(this);
		}
		
		this.draft = draft;
	}

	public Set getSet() {
		return set;
	}

	public void setSet(Set set) {
		this.set = set;
	}	

	public java.util.Set<DraftPackPick> getDraftPackPicks() {
		return draftPackPicks;
	}

	public void addDraftPackPick(DraftPackPick draftPackPick) {
		if(draftPackPicks == null){
			draftPackPicks = new HashSet<DraftPackPick>();
		}		

		this.draftPackPicks.add(draftPackPick);		
		draftPackPick.setDraftPack(this);
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
	}	
}
