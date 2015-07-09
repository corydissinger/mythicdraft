package com.cd.mythicdraft.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cd.mythicdraft.dao.DraftDAO;
import com.cd.mythicdraft.domain.RawCard;
import com.cd.mythicdraft.domain.RawDraft;
import com.cd.mythicdraft.json.JsonCard;
import com.cd.mythicdraft.json.JsonDraft;
import com.cd.mythicdraft.json.JsonPack;
import com.cd.mythicdraft.json.JsonPackPick;
import com.cd.mythicdraft.json.JsonPlayer;
import com.cd.mythicdraft.model.Card;
import com.cd.mythicdraft.model.Draft;
import com.cd.mythicdraft.model.DraftPack;
import com.cd.mythicdraft.model.DraftPackAvailablePick;
import com.cd.mythicdraft.model.DraftPackPick;
import com.cd.mythicdraft.model.DraftPlayer;
import com.cd.mythicdraft.model.Player;
import com.cd.mythicdraft.model.Set;

@Service(value = "draftService")
public class DraftService {
	
	private DraftDAO draftDao;

    @Autowired
    public void setDraftDao(final DraftDAO draftDao) {
        this.draftDao = draftDao;
    }

    @Autowired
	private MtgoDraftParserService mtgoDraftParserService;
	
	@Transactional
	public void addDraft(final InputStream mtgoDraftStream, final String name) {
		RawDraft aDraft;
		
		try {
			aDraft = mtgoDraftParserService.parse(mtgoDraftStream);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		draftDao.addDraft(convertRawDraft(aDraft, name));
	}

	public JsonDraft getDraftByActivePlayer(final Integer draftId, final Integer playerId) {
		final Draft draft = draftDao.getDraftByActivePlayer(draftId, playerId);
		final JsonDraft jsonDraft = getJsonDraftFromDraft(draft);
		
		return jsonDraft;
	}
	
	public JsonPackPick getPackByIdAndPick(final Integer draftPackId, final Integer pickId) {
		final JsonPackPick jsonPackPick = new JsonPackPick();
		final List<JsonCard> availablePicks = new ArrayList<JsonCard>();
		final JsonCard pick = new JsonCard();
		
		DraftPackPick packPick = draftDao.getPackByIdAndPick(draftPackId, pickId);
		
		for(DraftPackAvailablePick available : packPick.getDraftPackAvailablePicks()) {
			final JsonCard card = new JsonCard();
			
			card.setMultiverseId(available.getCardId());
			
			availablePicks.add(card);
		}
		
		jsonPackPick.setAvailable(availablePicks);
		
		pick.setMultiverseId(packPick.getCardId());
		jsonPackPick.setPick(pick);
		
		return jsonPackPick;
	}
	
	public List<JsonDraft> getRecentDrafts(final Integer numberOfDrafts) {
		return draftDao.getRecentDrafts(numberOfDrafts).stream()
														.map(this::getJsonDraftFromDraft)
														.collect(Collectors.toList());
	}

	private Draft convertRawDraft(RawDraft aRawDraft, String name) {
		Draft draft = new Draft();
		
		draft.setName(name);
		
		for(DraftPlayer aPlayer : createDraftPlayers(aRawDraft)) {
			draft.addDraftPlayer(aPlayer);	
		}

		for(DraftPack aPack : createDraftPacks(aRawDraft)) {
			draft.addDraftPack(aPack);	
		}		
		
		draft.setEventDate(aRawDraft.getEventDate());
		draft.setEventId(aRawDraft.getEventId());
		
		return draft;
	}

	private List<DraftPlayer> createDraftPlayers(RawDraft aRawDraft) {
		List<DraftPlayer> draftPlayers = new ArrayList<DraftPlayer>(8);
		
		List<String> allPlayers = new ArrayList<String>(aRawDraft.getOtherPlayers());
		allPlayers.add(aRawDraft.getActivePlayer());
		
		Map<String, Player> existingPlayers = draftDao.getPlayersByName(allPlayers);
		
		for(String aPlayer : aRawDraft.getOtherPlayers()) {
			DraftPlayer otherPlayer = new DraftPlayer();
			Player player = existingPlayers.get(aPlayer);
			
			if(player == null) {
				player = new Player();
				player.setName(aPlayer);
			}
			
			otherPlayer.setPlayer(player);
			otherPlayer.setIsActivePlayer(false);
			
			draftPlayers.add(otherPlayer);
		}
		
		DraftPlayer activePlayer = new DraftPlayer();
		Player player = existingPlayers.get(aRawDraft.getActivePlayer());
		
		if(player == null) {
			player = new Player();
			player.setName(aRawDraft.getActivePlayer());
		}
		
		activePlayer.setPlayer(player);
		activePlayer.setIsActivePlayer(true);
		
		draftPlayers.add(activePlayer);
		
		return draftPlayers;
	}

	private List<DraftPack> createDraftPacks(RawDraft aRawDraft) {
		List<DraftPack> draftPacks = new ArrayList<DraftPack>(3);
		
		final Map<String, Card> cardNameToCardMap = draftDao.getCardNameToCardMap(createCardNameToCardSetMap(aRawDraft));
		final List<Set> packSets = draftDao.getSetsByName(aRawDraft.getPackSets());
		
		for(int i = 0; i < aRawDraft.getPackSets().size(); i++) {
			String aSetName = aRawDraft.getPackSets().get(i);
			DraftPack draftPack = new DraftPack();
			
			for(Set aSet : packSets) {
				if(aSet.getName().equals(aSetName)){
					draftPack.setSet(aSet);
				}
			}
			
			for(DraftPackPick draftPackPick : createDraftPackPicks(aRawDraft.getPackToListOfPickToAvailablePicksMap().get(i),
																   cardNameToCardMap,
																   aRawDraft.getTempIdToCardNameMap())) {
				
				draftPack.addDraftPackPick(draftPackPick);
			}
			
			draftPack.setSequenceId(i);
			
			draftPacks.add(draftPack);
		}
		
		return draftPacks;
	}

	private List<DraftPackPick> createDraftPackPicks(final List<MutablePair<Integer, List<Integer>>> listOfPicksToAvailablePicks,
													 final Map<String, Card> cardNameToCardMap, 
													 final Map<Integer, String> tempIdToCardNameMap) {
		List<DraftPackPick> packPicks = new ArrayList<DraftPackPick>(listOfPicksToAvailablePicks.size());
		
		for(int i = 0; i < listOfPicksToAvailablePicks.size(); i++) {
			Pair<Integer, List<Integer>> pickToAvailablePicks = listOfPicksToAvailablePicks.get(i);
			DraftPackPick packPick = new DraftPackPick();
			Card pick = cardNameToCardMap.get(tempIdToCardNameMap.get(pickToAvailablePicks.getLeft()));
			
			packPick.setCard(pick);
			packPick.setCardId(pick.getId());
			packPick.setSequenceId(i);
			
			for(Integer availablePickTempId : pickToAvailablePicks.getRight()) {
				DraftPackAvailablePick availablePick = new DraftPackAvailablePick();
				Card available = cardNameToCardMap.get(tempIdToCardNameMap.get(availablePickTempId));
				availablePick.setCard(available);
				availablePick.setDraftPackPick(packPick);
				availablePick.setCardId(available.getId());
				
				packPick.addDraftPackAvailablePick(availablePick);
			}

			packPicks.add(packPick);
		}
		
		return packPicks;
	}

	private Map<String, String> createCardNameToCardSetMap(RawDraft aRawDraft) {
		Map<String, String> cardNameToCardSetMap = new HashMap<String, String>();
		
		for(Map.Entry<String, RawCard> anEntry : aRawDraft.getCardNameToRawCardMap().entrySet()) {
			cardNameToCardSetMap.put(anEntry.getKey(), anEntry.getValue().getSetCode());
		}
		
		return cardNameToCardSetMap;
	}

	private JsonDraft getJsonDraftFromDraft(final Draft draft) {
		final JsonDraft jsonDraft = new JsonDraft();
		final List<JsonPlayer> otherPlayers = new ArrayList<JsonPlayer>();
		final List<JsonPack> jsonPacks = new ArrayList<JsonPack>();		
		final DateTimeFormatter fmt = DateTimeFormat.forPattern("MM/dd/yyyy h:mm:ss aa");
		
		jsonDraft.setEventDate(fmt.print(new DateTime(draft.getEventDate())));
		jsonDraft.setCreated(fmt.print(new DateTime(draft.getCreated())));
		jsonDraft.setEventId(draft.getEventId());
		jsonDraft.setId(draft.getId());
		
		for(DraftPlayer draftPlayer : draft.getDraftPlayers()) {
			Player player = draftPlayer.getPlayer();
			JsonPlayer jsonPlayer = new JsonPlayer();

			jsonPlayer.setId(player.getId());
			jsonPlayer.setName(player.getName());			
			
			if(draftPlayer.getIsActivePlayer()) {
				jsonDraft.setActivePlayer(jsonPlayer);
			} else {
				otherPlayers.add(jsonPlayer);
			}
		}
		
		jsonDraft.setPlayers(otherPlayers);
		
		for(DraftPack pack : draft.getDraftPacks()) {
			JsonPack jsonPack = new JsonPack();
			
			jsonPack.setId(pack.getId());
			jsonPack.setSetCode(pack.getSet().getName());
			
			jsonPacks.add(jsonPack);
		}
		
		jsonDraft.setPacks(jsonPacks);
		
		return jsonDraft;
	}	
	
}
