package com.cd.mythicdraft.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.cd.mythicdraft.model.Card;
import com.cd.mythicdraft.model.Draft;
import com.cd.mythicdraft.model.Player;
import com.cd.mythicdraft.model.Set;

public interface DraftDAO {
	
	public void addDraft(Draft draft);
	
	public Map<String, Card> getCardNameToCardMap(final Map<String, String> cardNameToCardSetCode);
	
	public List<Set> getAvailableSets();
	
	public void persistSets(final List<Set> sets);

	public void persistCards(final List<Card> cardsInSet);

	public List<Set> getSetsByName(final Collection<String> setNames);

	public Map<String, Player> getPlayersByName(final Collection<String> playerNames);

	public boolean isDraftAlreadySaved(final Draft draft);

	public Draft getDraftByActivePlayer(final Integer draftId, final Integer activePlayerId);
}
