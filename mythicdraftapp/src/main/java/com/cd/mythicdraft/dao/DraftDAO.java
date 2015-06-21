package com.cd.mythicdraft.dao;

import java.util.Map;

import com.cd.mythicdraft.model.Card;
import com.cd.mythicdraft.model.Draft;

public interface DraftDAO {
	
	public void addDraft(Draft draft);
	
	public Map<String, Card> getCardNameToCardMap(final Map<String, String> cardNameToCardSetCode);
}
