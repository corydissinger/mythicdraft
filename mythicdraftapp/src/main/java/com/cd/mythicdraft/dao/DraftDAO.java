package com.cd.mythicdraft.dao;

import java.util.List;
import java.util.Map;

import com.cd.mythicdraft.model.Card;
import com.cd.mythicdraft.model.Draft;
import com.cd.mythicdraft.model.Set;

public interface DraftDAO {
	
	public void addDraft(Draft draft);
	
	public Map<String, Card> getCardNameToCardMap(final Map<String, String> cardNameToCardSetCode);
	
	public List<Set> getAvailableSets();
}
