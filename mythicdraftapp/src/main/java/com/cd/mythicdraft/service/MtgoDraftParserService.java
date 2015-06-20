package com.cd.mythicdraft.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cd.mythicdraft.grammar.MTGODraftLexer;
import com.cd.mythicdraft.grammar.MTGODraftParser;
import com.cd.mythicdraft.grammar.impl.MTGODraftListenerImpl;
import com.cd.mythicdraft.model.Draft;
import com.cd.mythicdraft.model.DraftPlayers;
import com.cd.mythicdraft.model.Player;

@Service(value = "mtgoDraftParserService")
public class MtgoDraftParserService {

	private static final Logger logger = Logger.getLogger(MtgoDraftParserService.class);	
	
	@Autowired
	private MTGODraftListenerImpl mtgoDraftListener;
	
	public Draft parse(InputStream mtgoDraft) throws IOException {
		ANTLRInputStream input = new ANTLRInputStream(mtgoDraft);
		TokenStream tokens = new CommonTokenStream(new MTGODraftLexer(input));
		MTGODraftParser parser = new MTGODraftParser(tokens);

		ParseTreeWalker walker = new ParseTreeWalker();
		
		//logger.debug(parser.time().getText());		
		
		mtgoDraftListener.reset();
		walker.walk(mtgoDraftListener, parser.file());
		
		Draft draft = new Draft();
		
		draft.setDraftPlayers(createDraftPlayers(draft));
		
		return draft;
	}
	
	private List<DraftPlayers> createDraftPlayers(Draft aDraft) {
		List<DraftPlayers> draftPlayers = new ArrayList<DraftPlayers>(8);
		
		for(String aPlayer : mtgoDraftListener.getOtherPlayers()) {
			DraftPlayers otherPlayer = new DraftPlayers();
			Player player = new Player();
			player.setName(aPlayer);
			
			otherPlayer.setDraft(aDraft);
			otherPlayer.setPlayer(player);
			otherPlayer.setIsActivePlayer(false);
			
			draftPlayers.add(otherPlayer);
		}
		
		DraftPlayers activePlayer = new DraftPlayers();
		Player player = new Player();
		player.setName(mtgoDraftListener.getActivePlayer());
		
		activePlayer.setDraft(aDraft);
		activePlayer.setPlayer(player);
		activePlayer.setIsActivePlayer(true);
		
		draftPlayers.add(activePlayer);
		
		return draftPlayers;
	}
	
}
