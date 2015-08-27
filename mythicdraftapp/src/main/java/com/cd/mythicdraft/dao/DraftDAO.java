package com.cd.mythicdraft.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.cd.mythicdraft.exception.DuplicateDraftException;
import com.cd.mythicdraft.model.Card;
import com.cd.mythicdraft.model.Deck;
import com.cd.mythicdraft.model.Draft;
import com.cd.mythicdraft.model.DraftPackPick;
import com.cd.mythicdraft.model.Player;
import com.cd.mythicdraft.model.Set;

public interface DraftDAO {
	
	public void addDraft(Draft draft) throws DuplicateDraftException;
	
	public Map<String, Player> getPlayersByName(final Collection<String> playerNames);

	public boolean isDraftAlreadySaved(final Draft draft);

	public Draft getDraftById(final Integer draftId);

	public DraftPackPick getPackByIdAndPick(final Integer draftPackId, final Integer pickId);

	public Collection<Draft> getRecentDrafts(final Integer numberOfDrafts);

	public List<Integer> getDistinctMultiverseIdsForDraft(Integer draftId);
	
	public List<Integer> getAllPicksInOrder(Integer draftId);
	
	public Collection<Draft> getDraftsByPlayerId(final Integer playerId);
	
	public Collection<Player> getPlayersSearch(final String searchString);
	
	public Player getPlayerById(final Integer playerId);

	public Integer addDeck(Deck convertRawDeck) throws DuplicateDraftException;

	public Deck getDeckById(Integer deckId);
}
