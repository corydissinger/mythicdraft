package com.cd.mythicdraft.grammar.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.cd.mythicdraft.grammar.MTGODraftParser;
import com.cd.mythicdraft.grammar.MTGODraftParser.APlayerContext;
import com.cd.mythicdraft.grammar.MTGODraftParser.CurrentPlayerContext;
import com.cd.mythicdraft.grammar.MTGODraftParser.EventIdContext;
import com.cd.mythicdraft.grammar.MTGODraftParser.PickContext;
import com.cd.mythicdraft.grammar.MTGODraftParser.SetContext;
import com.cd.mythicdraft.grammar.MTGODraftParser.TimeContext;
import com.cd.mythicdraft.grammar.MTGODraftParserBaseListener;

@Component("mtgoDraftListener")
public class MTGODraftListenerImpl extends MTGODraftParserBaseListener {

	private static final Logger logger = Logger.getLogger(MTGODraftListenerImpl.class);	
	
	private List<String> otherPlayers;
	private String activePlayer;
	private List<String> packSets;
	
	public void reset() {
		otherPlayers = new ArrayList<String>(7);
		activePlayer = "";
		packSets = new ArrayList<String>(3);
	}
	
	@Override
	public void enterEventId(EventIdContext ctx) {
	}

	@Override
	public void enterTime(TimeContext ctx) {
	}

	@Override 
	public void enterCurrentPlayer(CurrentPlayerContext ctx) {
		activePlayer = ctx.getText();
	}
	
	@Override 
	public void enterAPlayer(APlayerContext ctx) { 
		otherPlayers.add(ctx.getText());
	}	
	
	@Override 
	public void enterSet(SetContext ctx) {
		packSets.add(ctx.getText());
	}

	@Override 
	public void enterPackNumber(MTGODraftParser.PackNumberContext ctx) { 
	}	
	
	@Override 
	public void enterPickNumber(MTGODraftParser.PickNumberContext ctx) { 
	}	
	
	@Override 
	public void enterPick(PickContext ctx) { 
	}

	public List<String> getOtherPlayers() {
		return otherPlayers;
	}

	public String getActivePlayer() {
		return activePlayer;
	}

	public List<String> getPackSets() {
		return packSets;
	}
}
