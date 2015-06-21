package com.cd.mythicdraft.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cd.mythicdraft.dao.DraftDAO;
import com.cd.mythicdraft.domain.RawCard;
import com.cd.mythicdraft.domain.RawDraft;
import com.cd.mythicdraft.model.Card;
import com.cd.mythicdraft.model.Draft;
import com.cd.mythicdraft.model.DraftPack;
import com.cd.mythicdraft.model.DraftPlayer;
import com.cd.mythicdraft.model.Player;
import com.cd.mythicdraft.model.Set;

@Service(value = "draftService")
public class DraftService {

	@Autowired
	private DraftDAO draftDao;
	
	@Autowired
	private MtgoDraftParserService mtgoDraftParserService;
	
	@Transactional
	public void addDraft(InputStream mtgoDraftStream) {
		RawDraft aDraft;
		
		try {
			aDraft = mtgoDraftParserService.parse(mtgoDraftStream);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		draftDao.addDraft(convertRawDraft(aDraft));
	}
	
	private Draft convertRawDraft(RawDraft aRawDraft) {
		Draft draft = new Draft();
		
		draft.setDraftPlayers(createDraftPlayers(draft, aRawDraft));
		draft.setDraftPacks(createDraftPacks(draft, aRawDraft));
		
		return draft;
	}

	private List<DraftPlayer> createDraftPlayers(Draft aDraft, RawDraft aRawDraft) {
		List<DraftPlayer> draftPlayers = new ArrayList<DraftPlayer>(8);
		
		for(String aPlayer : aRawDraft.getOtherPlayers()) {
			DraftPlayer otherPlayer = new DraftPlayer();
			Player player = new Player();
			player.setName(aPlayer);
			
			otherPlayer.setDraft(aDraft);
			otherPlayer.setPlayer(player);
			otherPlayer.setIsActivePlayer(false);
			
			draftPlayers.add(otherPlayer);
		}
		
		DraftPlayer activePlayer = new DraftPlayer();
		Player player = new Player();
		player.setName(aRawDraft.getActivePlayer());
		
		activePlayer.setDraft(aDraft);
		activePlayer.setPlayer(player);
		activePlayer.setIsActivePlayer(true);
		
		draftPlayers.add(activePlayer);
		
		return draftPlayers;
	}

	private List<DraftPack> createDraftPacks(Draft aDraft, RawDraft aRawDraft) {
		List<DraftPack> draftPacks = new ArrayList<DraftPack>(3);
		final Map<String, Card> cardNameToCardMap = draftDao.getCardNameToCardMap(createCardNameToCardSetMap(aRawDraft));
		
		for(int i = 0; i < aRawDraft.getPackSets().size(); i++) {
			String aSetName = aRawDraft.getPackSets().get(i);
			DraftPack draftSet = new DraftPack();
			Set set = new Set();
			
			set.setName(aSetName);
			
			draftSet.setDraft(aDraft);
			draftSet.setSet(set);
			draftSet.setSequenceId(i);
			
			draftPacks.add(draftSet);
		}
		
		return draftPacks;
	}

	private Map<String, String> createCardNameToCardSetMap(RawDraft aRawDraft) {
		for(Map.Entry<String, RawCard > anEntry : aRawDraft.getCardNameToRawCardMap().entrySet()) {
			
		}
		
		return null;
	}	
}
