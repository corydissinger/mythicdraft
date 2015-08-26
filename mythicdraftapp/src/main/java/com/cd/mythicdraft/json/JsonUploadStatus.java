package com.cd.mythicdraft.json;

import java.io.Serializable;

public class JsonUploadStatus implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private boolean isDraftDuplicate = false;
	private boolean isDraftInvalid = false;
	private boolean isDeckInvalid = false;
	private int draftId = 0;
	private int deckId = 0;
	
	public boolean isDraftDuplicate() {
		return isDraftDuplicate;
	}
	public void setDraftDuplicate(boolean isDraftDuplicate) {
		this.isDraftDuplicate = isDraftDuplicate;
	}
	public boolean isDraftInvalid() {
		return isDraftInvalid;
	}
	public void setDraftInvalid(boolean isDraftInvalid) {
		this.isDraftInvalid = isDraftInvalid;
	}
	public boolean isDeckInvalid() {
		return isDeckInvalid;
	}
	public void setDeckInvalid(boolean isDeckInvalid) {
		this.isDeckInvalid = isDeckInvalid;
	}
	public int getDraftId() {
		return draftId;
	}
	public void setDraftId(int draftId) {
		this.draftId = draftId;
	}
	public int getDeckId() {
		return deckId;
	}
	public void setDeckId(int deckId) {
		this.deckId = deckId;
	}
}
