package com.cd.mythicdraft.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.cd.mythicdraft.model.Card;
import com.cd.mythicdraft.model.Set;

public interface CardDao {

	public Map<String, Card> getCardNameToCardMap(Map<String, String> cardNameToCardSetCode, List<Set> sets);
	
	public List<Set> getAvailableSets();
	
	public void persistSets(final List<Set> sets);

	public void persistCards(final List<Card> cardsInSet);

	public List<Set> getSetsByName(final Collection<String> setNames);
	
	public List<Set> getSetsById(final Collection<Integer> setIds);

	public Map<Integer, Card> getTempCardIdToCardMap(Map<String, Integer> cardNameToTempIdMap, List<Set> sets);

	public Card getCardByName(final String cardName);
	
	public List<Set> addPromoSets(Collection<String> setNames);
}
