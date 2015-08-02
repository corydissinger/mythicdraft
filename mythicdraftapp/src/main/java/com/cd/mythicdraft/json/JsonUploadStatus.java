package com.cd.mythicdraft.json;

import java.io.Serializable;

public class JsonUploadStatus implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private boolean isDraftDuplicate = false;
	private boolean isDraftInvalid = false;
	
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
}
