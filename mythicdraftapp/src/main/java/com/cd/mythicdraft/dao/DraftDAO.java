package com.cd.mythicdraft.dao;

import com.cd.mythicdraft.model.Draft;

public interface DraftDAO {
	
	public boolean getDuplicateHash(String md5Hash);
	
	public void addDraft(Draft draft);
}
