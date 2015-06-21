package com.cd.mythicdraft.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cd.mythicdraft.dao.DraftDAO;
import com.cd.mythicdraft.grammar.MTGODraftLexer;
import com.cd.mythicdraft.grammar.MTGODraftParser;
import com.cd.mythicdraft.grammar.impl.MTGODraftListenerImpl;
import com.cd.mythicdraft.model.Draft;
import com.cd.mythicdraft.model.DraftPack;
import com.cd.mythicdraft.model.DraftPlayer;
import com.cd.mythicdraft.model.Player;
import com.cd.mythicdraft.model.Set;

@Service(value = "mtgoDraftParserService")
public class MtgoDraftParserService {

	private static final Logger logger = Logger.getLogger(MtgoDraftParserService.class);	
	
	@Autowired
	private MTGODraftListenerImpl mtgoDraftListener;
	
	@Autowired
	private DraftDAO draftDao;
	
	public Draft parse(InputStream mtgoDraft) throws IOException {
		ANTLRInputStream input = new ANTLRInputStream(new InputStreamReader(mtgoDraft, StandardCharsets.UTF_8));
		TokenStream tokens = new CommonTokenStream(new MTGODraftLexer(input));
		MTGODraftParser parser = new MTGODraftParser(tokens);

		ParseTreeWalker walker = new ParseTreeWalker();
		
		mtgoDraftListener.reset();
		walker.walk(mtgoDraftListener, parser.file());
		
		Draft draft = new Draft();
		
		draft.setDraftPlayers(createDraftPlayers(draft));
		draft.setDraftPacks(createDraftPacks(draft));
		draft.setEventDate(createEventDate());
		draft.setEventId(Integer.parseInt(mtgoDraftListener.getEventId()));
		
		mtgoDraftListener.cleanup();
		
		return draft;
	}

	private List<DraftPlayer> createDraftPlayers(Draft aDraft) {
		List<DraftPlayer> draftPlayers = new ArrayList<DraftPlayer>(8);
		
		for(String aPlayer : mtgoDraftListener.getOtherPlayers()) {
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
		player.setName(mtgoDraftListener.getActivePlayer());
		
		activePlayer.setDraft(aDraft);
		activePlayer.setPlayer(player);
		activePlayer.setIsActivePlayer(true);
		
		draftPlayers.add(activePlayer);
		
		return draftPlayers;
	}

	private List<DraftPack> createDraftPacks(Draft aDraft) {
		List<DraftPack> draftPacks = new ArrayList<DraftPack>(3);
		
		for(int i = 0; i < mtgoDraftListener.getPackSets().size(); i++) {
			String aSetName = mtgoDraftListener.getPackSets().get(i);
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
	
	private Date createEventDate() {
		DateTimeFormatter dtf = DateTimeFormat.forPattern("MM/dd/yyyy h:mm:ss aa");
		DateTime eventDate = dtf.parseDateTime(mtgoDraftListener.getEventDate());
		return eventDate.toDate();
	}	
}
