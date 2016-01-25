package com.cd.mythicdraft.service;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
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
import org.springframework.util.CollectionUtils;

import com.cd.mythicdraft.dao.CardDao;
import com.cd.mythicdraft.dao.DraftDao;
import com.cd.mythicdraft.domain.RawCard;
import com.cd.mythicdraft.domain.RawDeck;
import com.cd.mythicdraft.domain.RawDraft;
import com.cd.mythicdraft.exception.DuplicateDraftException;
import com.cd.mythicdraft.json.JsonAllPicks;
import com.cd.mythicdraft.json.JsonCard;
import com.cd.mythicdraft.json.JsonDeck;
import com.cd.mythicdraft.json.JsonDraft;
import com.cd.mythicdraft.json.JsonPack;
import com.cd.mythicdraft.json.JsonPackPick;
import com.cd.mythicdraft.json.JsonPlayer;
import com.cd.mythicdraft.json.JsonPlayerStats;
import com.cd.mythicdraft.json.JsonRecentDrafts;
import com.cd.mythicdraft.json.JsonUploadStatus;
import com.cd.mythicdraft.model.Card;
import com.cd.mythicdraft.model.Deck;
import com.cd.mythicdraft.model.DeckCard;
import com.cd.mythicdraft.model.Draft;
import com.cd.mythicdraft.model.DraftPack;
import com.cd.mythicdraft.model.DraftPackAvailablePick;
import com.cd.mythicdraft.model.DraftPackPick;
import com.cd.mythicdraft.model.DraftPlayer;
import com.cd.mythicdraft.model.Format;
import com.cd.mythicdraft.model.IgnorableCards;
import com.cd.mythicdraft.model.Player;
import com.cd.mythicdraft.model.Set;

@Service(value = "draftService")
@Transactional
public class DraftService {

	private static final Logger logger = Logger.getLogger(DraftService.class);	
	
	private DraftDao draftDao;

    @Autowired
    public void setDraftDao(final DraftDao draftDao) {
        this.draftDao = draftDao;
    }
    
	private CardDao cardDao;

    @Autowired
    public void setCardDao(final CardDao cardDao) {
        this.cardDao = cardDao;
    }    

    @Autowired
	private MtgoDraftParserService mtgoDraftParserService;
    
    @Autowired
	private MtgoDeckParserService mtgoDeckParserService;    
	
	public JsonUploadStatus addDraft(final InputStream mtgoDraftStream, 
						 			 final String name, 
						 			 final Integer wins, 
						 			 final Integer losses) {
		RawDraft aDraft = null;
		JsonUploadStatus uploadStatus = new JsonUploadStatus();
		
		try {
			aDraft = mtgoDraftParserService.parse(mtgoDraftStream);
		} catch (IOException e) {
			uploadStatus.setDraftInvalid(true);
		}

		uploadStatus.setDraftInvalid(isDraftInvalid(aDraft));
		
		Draft theDraft = convertRawDraft(aDraft, name, wins, losses);
		
		if(aDraft != null && !uploadStatus.isDraftInvalid()) {
			try {
				draftDao.addDraft(theDraft);
				uploadStatus.setDraftId(theDraft.getId());
			} catch (DuplicateDraftException e) {
				uploadStatus.setDraftDuplicate(true);
			}
		}		
		
		return uploadStatus;
	}

	public JsonDraft getDraftById(final Integer draftId) {
		final Draft draft = draftDao.getDraftById(draftId);
		final JsonDraft jsonDraft = getJsonDraftFromDraft(draft);
		
		final Deck deck = draft.getDeck();
		
		if(deck != null) {
			jsonDraft.setDeckId(deck.getId());			
		} 
		
		return jsonDraft;
	}
	
	public JsonPackPick getPackByIdAndPick(final Integer draftId,
										   final Integer draftPackId, 
										   final Integer pickId) {
		
		final JsonPackPick jsonPackPick = new JsonPackPick();
		final List<JsonCard> availablePicks = new ArrayList<JsonCard>();
		final JsonCard pick = new JsonCard();
		int cardCount = 0;
		
		DraftPackPick packPick = draftDao.getPackByIdAndPick(draftPackId, pickId);
		
		for(DraftPackAvailablePick available : packPick.getDraftPackAvailablePicks()) {
			final String id = available.getCardId() + "_" + cardCount++; 
			final JsonCard card = new JsonCard();
			
			card.setMultiverseId(available.getCardId());
			card.setId(id);
			
			availablePicks.add(card);
		}
		
		final String pickKey = packPick.getCardId()  + "_" + cardCount;
		final JsonCard pickedCard = new JsonCard();
		
		pickedCard.setMultiverseId(packPick.getCardId());
		pickedCard.setId(packPick.getCardId()  + "_" + cardCount);
		
		availablePicks.add(pickedCard);
		
		jsonPackPick.setPick(pickKey);

		long seed = System.nanoTime();
		
		Collections.shuffle(availablePicks, new Random(seed));
		Collections.shuffle(availablePicks, new Random(seed));		
		
		jsonPackPick.setAvailable(availablePicks);		
		
		jsonPackPick.setDraftMetaData(getJsonDraftFromDraft(draftDao.getDraftById(draftId)));
		
		return jsonPackPick;
	}
	
	public JsonRecentDrafts getRecentDrafts(final Integer numberOfDrafts, final Integer pageNumber) {
		List<JsonDraft> recentDrafts = draftDao.getRecentDrafts(numberOfDrafts, pageNumber)
											   .stream()
											   .map(this::getJsonDraftFromDraft)
											   .collect(Collectors.toList()); 
		
		final JsonRecentDrafts jsonRecentDrafts = new JsonRecentDrafts();
		
		jsonRecentDrafts.setRecentDrafts(recentDrafts);
		jsonRecentDrafts.setTotalPages(draftDao.getRecentDraftPages());
		
		return jsonRecentDrafts; 
	}
	
	public JsonPlayerStats getDraftsByPlayerId(final Integer playerId) {
		final JsonPlayerStats stats = new JsonPlayerStats();
		final List<JsonDraft> drafts = draftDao.getDraftsByPlayerId(playerId)
											   .stream()
											   .map(this::getJsonDraftFromDraft)
											   .collect(Collectors.toList()); 
		
		stats.setDrafts(drafts);
		
		Player player = draftDao.getPlayerById(playerId);
		JsonPlayer jsonPlayer = getJsonPlayerFromPlayer(player);
		
		stats.setPlayer(jsonPlayer);
		
		Integer gamesPlayed = 0;
		Integer gamesWon = 0;
		
		for(JsonDraft draft : drafts) {
			gamesPlayed += draft.getWins() + draft.getLosses();
			gamesWon += draft.getWins();
		}
		
		stats.setWinPercentage(BigDecimal.valueOf(gamesWon).divide(BigDecimal.valueOf(gamesPlayed), 2, RoundingMode.DOWN));
		
		return stats;
	}	
	
	public JsonAllPicks getAllPicks(final Integer draftId) {
		final JsonAllPicks picks = new JsonAllPicks();
		
		final List<Integer> allCards = draftDao.getDistinctMultiverseIdsForDraft(draftId);
		final List<Integer> picksInOrder = draftDao.getAllPicksInOrder(draftId);
		
		picks.setAllCards(allCards);
		picks.setPicksInOrder(picksInOrder);
		
		picks.setDraftMetaData(getJsonDraftFromDraft(draftDao.getDraftById(draftId)));
		
		return picks;
	}
	
	public List<JsonPlayer> getPlayersSearch(final String searchString) {
		return draftDao.getPlayersSearch(searchString)
				.stream()
				.map(this::getJsonPlayerFromPlayer)
				.collect(Collectors.toList());
	}

	public Integer addDeck(int draftId, InputStream deck) {
		final Draft draft = draftDao.getDraftById(draftId);
		
		final List<Set> sets = new ArrayList<Set>(3);
		RawDeck rawDeck;
		
		for(DraftPack aPack : draft.getDraftPacks()) {
			sets.add(aPack.getSet());
		}
		
		try {
			rawDeck = mtgoDeckParserService.parse(deck);
		} catch (IOException e) {
			logger.error(e.getMessage());
			return 0;
		}
		
		try {
			Map<Integer, Card> tempIdToCardMap = cardDao.getTempCardIdToCardMap(rawDeck.getCardNameToTempIdMap(), sets);
			
			if(isDeckInvalid(draftId, tempIdToCardMap)) {
				logger.error("Deck was marked as invalid with the following cards: ");
				logger.error(tempIdToCardMap.toString());
				logger.error("Draft id: " + draft.getId());				
				return 0;
			}
			
			return draftDao.addDeck(convertRawDeck(rawDeck, draft, tempIdToCardMap));
		} catch(DuplicateDraftException e) {
			return 0;
		}
	}		
	
	public JsonDeck getDeckById(Integer deckId) {
		final JsonDeck deck = new JsonDeck();
		final List<JsonCard> mainDeckCards = new ArrayList<JsonCard>();
		final List<JsonCard> sideBoardCards = new ArrayList<JsonCard>();
		int uniqueCardCount = 0;
		
		Deck theDeck = draftDao.getDeckById(deckId);
		
		for(DeckCard aCard : theDeck.getDeckCards()) {
			final Card theCard = aCard.getCard();
			final Integer multiverseId = theCard.getId();
			final String id = multiverseId + "_" + uniqueCardCount++;
			
			final JsonCard jsonCard = new JsonCard();
			
			jsonCard.setId(id);
			jsonCard.setMultiverseId(multiverseId);
			jsonCard.setCmc(theCard.getCmc());
			jsonCard.setColor(theCard.getColor());
			jsonCard.setIsCreature(theCard.getIsCreature());
			jsonCard.setIsNonCreature(theCard.getIsNonCreature());
			jsonCard.setCount(aCard.getCount());
			
			if(aCard.getIsMainDeck()) {
				mainDeckCards.add(jsonCard);
			} else {
				sideBoardCards.add(jsonCard);
			}
		}
		
		deck.setMainDeckCards(mainDeckCards);
		deck.setSideBoardCards(sideBoardCards);
		deck.setDraftId(theDeck.getDraftId());
		deck.setDraftMetaData(getJsonDraftFromDraft(theDeck.getDraft()));
		
		return deck;
	}	
	
	public Deck convertRawDeck(final RawDeck rawDeck, final Draft draft, final Map<Integer, Card> tempCardIdToCardMap) {
		Deck deck = new Deck();
		
		for(DeckCard aDeckCard : createDeckCards(rawDeck, true, tempCardIdToCardMap)) {
			deck.addDeckCard(aDeckCard);
		}
		
		for(DeckCard aDeckCard : createDeckCards(rawDeck, false, tempCardIdToCardMap)) {
			deck.addDeckCard(aDeckCard);
		}
		
		deck.setDraft(draft);
		deck.setDraftId(draft.getId());
		
		return deck;
	}

	public List<DeckCard> createDeckCards(final RawDeck rawDeck, 
										   final boolean isMainDeck, 
										   final Map<Integer, Card> tempCardIdToCardMap) {
		
		final List<MutablePair<Integer, Integer>> listOfCards;
		
		if(isMainDeck) {
			listOfCards = rawDeck.getListOfMainDeckCards();
		} else {
			listOfCards = rawDeck.getListOfSideBoardCards();
		}
		
		List<DeckCard> deckCards = new ArrayList<DeckCard>(listOfCards.size());
		
		for(MutablePair<Integer, Integer> cardIdToCardCount : listOfCards) {
			DeckCard deckCard = new DeckCard();
			Card card = tempCardIdToCardMap.get(cardIdToCardCount.getLeft());
			
			//Edge case for basics lands in small sets and special promo cards in large sets
			if(card == null) {
				for(Map.Entry<String, Integer> nameToTempId : rawDeck.getCardNameToTempIdMap().entrySet()) {
					if(nameToTempId.getValue().equals(cardIdToCardCount.getLeft())) {
						card = cardDao.getCardByName(nameToTempId.getKey());
						if(card != null) {
							break;	
						}						
					}
				}
			}
			
			if(card == null) {
				continue;
			}
			
			deckCard.setIsMainDeck(isMainDeck);
			deckCard.setCard(card);
			deckCard.setCardId(card.getId());
			deckCard.setCount(cardIdToCardCount.getRight());
			
			deckCards.add(deckCard);
		}
		
		return deckCards;
	}

	public Draft convertRawDraft(RawDraft aRawDraft, String name, Integer wins, Integer losses) {
		Draft draft = new Draft();
		
		draft.setName(name);
		
		for(DraftPlayer aPlayer : createDraftPlayers(aRawDraft)) {
			draft.addDraftPlayer(aPlayer);	
		}

		List<Set> theSets = createDraftPacks(aRawDraft, draft);
		
		draft.setEventDate(aRawDraft.getEventDate());
		draft.setEventId(aRawDraft.getEventId());
		draft.setWins(wins);
		draft.setLosses(losses);
		draft.setFormatId(getFormatId(theSets));
		
		return draft;
	}

	public Integer getFormatId(List<Set> sets) {
		Set packOne;
		Set packTwo;
		Set packThree;
		
		if(sets.size() == 1){
			packOne = sets.get(0);
			packTwo = sets.get(0);
			packThree = sets.get(0);			
		} else {
			packOne = sets.get(0);
			packTwo = sets.get(1);
			packThree = sets.get(2);			
		}
		
		Format theFormat = draftDao.getFormatByPacks(packOne, packTwo, packThree);
		
		if(theFormat == null) {
			theFormat = new Format();
			
			theFormat.setFirstPackSet(packOne);
			theFormat.setSecondPackSet(packTwo);
			theFormat.setThirdPackSet(packThree);
			
			theFormat.setFirstPack(packOne.getId());
			theFormat.setSecondPack(packTwo.getId());
			theFormat.setThirdPack(packThree.getId());
			
			draftDao.addFormat(theFormat);
		}
		
		return theFormat.getId();
	}

	public List<DraftPlayer> createDraftPlayers(RawDraft aRawDraft) {
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

	public List<Set> createDraftPacks(RawDraft aRawDraft, Draft aDraft) {
		List<DraftPack> draftPacks = new ArrayList<DraftPack>(3);

		//First determine existence of promo packs
		List<Set> packSets = cardDao.getSetsByName(aRawDraft.getPackSets());
		
		if(CollectionUtils.isEmpty(packSets)) {
			packSets = cardDao.addPromoSets(aRawDraft.getPackSets());
		}
		
		final Map<String, Card> cardNameToCardMap = cardDao.getCardNameToCardMap(createCardNameToCardSetMap(aRawDraft), packSets);
		
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
			draftPack.setPackSize(aRawDraft.getPackSizes().get(i));
			
			draftPacks.add(draftPack);
		}
		
		for(DraftPack aDraftPack : draftPacks) {
			aDraft.addDraftPack(aDraftPack);			
		}
		
		return packSets;
	}

	public List<DraftPackPick> createDraftPackPicks(final List<MutablePair<Integer, List<Integer>>> listOfPicksToAvailablePicks,
													 final Map<String, Card> cardNameToCardMap, 
													 final Map<Integer, String> tempIdToCardNameMap) {
		List<DraftPackPick> packPicks = new ArrayList<DraftPackPick>(listOfPicksToAvailablePicks.size());
		
		for(int i = 0; i < listOfPicksToAvailablePicks.size(); i++) {
			Pair<Integer, List<Integer>> pickToAvailablePicks = listOfPicksToAvailablePicks.get(i);
			DraftPackPick packPick = new DraftPackPick();
			Integer tempId = pickToAvailablePicks.getLeft();
			String cardName = tempIdToCardNameMap.get(tempId);
			Card pick = cardNameToCardMap.get(cardName);
			
			packPick.setCard(pick);
			packPick.setCardId(pick.getId());
			packPick.setSequenceId(i);
			
			for(Integer availablePickTempId : pickToAvailablePicks.getRight()) {
				DraftPackAvailablePick availablePick = new DraftPackAvailablePick();
				Card available = cardNameToCardMap.get(tempIdToCardNameMap.get(availablePickTempId));
				
				//Part of the hack to facilitate split cards. They'll have two entries and only one will work
				if(available == null) {
					continue;
				}
				
				availablePick.setCard(available);
				availablePick.setDraftPackPick(packPick);
				availablePick.setCardId(available.getId());
				
				packPick.addDraftPackAvailablePick(availablePick);
			}

			packPicks.add(packPick);
		}
		
		return packPicks;
	}

	public Map<String, String> createCardNameToCardSetMap(RawDraft aRawDraft) {
		Map<String, String> cardNameToCardSetMap = new HashMap<String, String>();
		
		for(Map.Entry<String, RawCard> anEntry : aRawDraft.getCardNameToRawCardMap().entrySet()) {
			cardNameToCardSetMap.put(anEntry.getKey(), anEntry.getValue().getSetCode());
		}
		
		return cardNameToCardSetMap;
	}

	public JsonDraft getJsonDraftFromDraft(final Draft draft) {
		final JsonDraft jsonDraft = new JsonDraft();
		final List<JsonPlayer> otherPlayers = new ArrayList<JsonPlayer>();
		final List<JsonPack> jsonPacks = new ArrayList<JsonPack>();		
		final DateTimeFormatter fmt = DateTimeFormat.forPattern("MM/dd/yyyy h:mm:ss aa");
		
		jsonDraft.setEventDate(fmt.print(new DateTime(draft.getEventDate())));
		jsonDraft.setCreated(fmt.print(new DateTime(draft.getCreated())));
		jsonDraft.setEventId(draft.getEventId());
		jsonDraft.setId(draft.getId());
		jsonDraft.setName(draft.getName());
		jsonDraft.setWins(draft.getWins());
		jsonDraft.setLosses(draft.getLosses());
		
		for(DraftPlayer draftPlayer : draft.getDraftPlayers()) {
			Player player = draftPlayer.getPlayer();
			JsonPlayer jsonPlayer = getJsonPlayerFromPlayer(player);
			
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
			jsonPack.setPackSize(pack.getPackSize());
			
			jsonPacks.add(jsonPack);
		}
		
		jsonDraft.setPacks(jsonPacks);
		
		final Deck deck = draft.getDeck();
		
		if(deck != null) {
			jsonDraft.setDeckId(deck.getId());
		}
		
		return jsonDraft;
	}	
	
	public JsonPlayer getJsonPlayerFromPlayer(final Player aPlayer) {
		final JsonPlayer jsonPlayer = new JsonPlayer();
		
		jsonPlayer.setId(aPlayer.getId());
		jsonPlayer.setName(aPlayer.getName());
		
		return jsonPlayer;
	}

	public boolean isDraftInvalid(RawDraft aDraft) {
		if(aDraft.getPackSets().isEmpty() || 
		   aDraft.getPackSets().size() < 3) {
			return true;
		}
		
		//Simplest way I could think of identifying an incomplete draft.
		//If there aren't exactly 3 last picks, this file is bogus.
		int lastPicks = 0;
		Map<Integer, List<MutablePair<Integer, List<Integer>>>> pickMapList = aDraft.getPackToListOfPickToAvailablePicksMap();
		
		for(Map.Entry<Integer, List<MutablePair<Integer, List<Integer>>>> entry : pickMapList.entrySet()) {
			List<MutablePair<Integer, List<Integer>>> listOfPackPicks = entry.getValue();
			
			if(listOfPackPicks == null || listOfPackPicks.contains(null)){
				return true;
			}			
			
			for(MutablePair<Integer, List<Integer>> packPicks : listOfPackPicks) {
				List<Integer> available = packPicks.getRight();
				
				if(CollectionUtils.isEmpty(available)) {
					lastPicks++;
				}
			}
			
		}
		
		if(lastPicks != 3) {
			return true;
		}
		
		return false;
	}

	public boolean isDeckInvalid(Integer draftId, Map<Integer, Card> tempIdToCardMap) {
		final List<Integer> allDraftPicks = draftDao.getDistinctMultiverseIdsForDraft(draftId);
		final Collection<Card> distinctDeckCards = tempIdToCardMap.values();
		
		for(Card deckCard : distinctDeckCards) {
			if(IgnorableCards.isIgnorable(deckCard.getCardName(), deckCard.getSet().getName())) {
				continue;
			}
			
			if(!allDraftPicks.contains(deckCard.getId())) {
				logger.error("The card that was marked as invalid is: " + deckCard.toString());
				return true;
			}
		}
		
		return false;
	}

}
