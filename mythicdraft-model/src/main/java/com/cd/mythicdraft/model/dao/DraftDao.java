package com.cd.mythicdraft.model.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.cd.mythicdraft.model.exception.DuplicateDraftException;
import com.cd.mythicdraft.model.entity.Deck;
import com.cd.mythicdraft.model.entity.Draft;
import com.cd.mythicdraft.model.entity.DraftPackPick;
import com.cd.mythicdraft.model.entity.Format;
import com.cd.mythicdraft.model.entity.Player;
import com.cd.mythicdraft.model.entity.Set;

public interface DraftDao {
	
	public void addDraft(Draft draft) throws DuplicateDraftException;
	
	public void updateDraft(Draft draft);
	
	public Map<String, Player> getPlayersByName(final Collection<String> playerNames);

	public boolean isDraftAlreadySaved(final Draft draft);

	public Draft getDraftById(final Integer draftId);

	public DraftPackPick getPackByIdAndPick(final Integer draftPackId, final Integer pickId);

	public Collection<Draft> getRecentDrafts(final Integer numberOfDrafts, final Integer pageNumber);
	
	public Integer getRecentDraftPages();	

	public List<Integer> getDistinctMultiverseIdsForDraft(Integer draftId);
	
	public List<Integer> getAllPicksInOrder(Integer draftId);
	
	public Collection<Draft> getDraftsByPlayerId(final Integer playerId);
	
	public Collection<Player> getPlayersSearch(final String searchString);
	
	public Player getPlayerById(final Integer playerId);

	public Integer addDeck(Deck convertRawDeck) throws DuplicateDraftException;

	public Deck getDeckById(Integer deckId);
	
	public Format getFormatByPacks(Set packeOne, Set packTwo, Set packThree);

	public void addFormat(Format format);
}
