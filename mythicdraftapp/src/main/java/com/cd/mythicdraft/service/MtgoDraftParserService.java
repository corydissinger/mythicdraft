package com.cd.mythicdraft.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cd.mythicdraft.domain.RawDraft;
import com.cd.mythicdraft.domain.RawDraftBuilder;
import com.cd.mythicdraft.grammar.MTGODraftLexer;
import com.cd.mythicdraft.grammar.MTGODraftParser;
import com.cd.mythicdraft.grammar.impl.MTGODraftListenerImpl;

@Service(value = "mtgoDraftParserService")
public class MtgoDraftParserService {

	private static final Logger logger = Logger.getLogger(MtgoDraftParserService.class);	
	
	@Autowired
	private MTGODraftListenerImpl mtgoDraftListener;
	
	public RawDraft parse(InputStream mtgoDraft) throws IOException {
		ANTLRInputStream input = new ANTLRInputStream(new InputStreamReader(mtgoDraft, StandardCharsets.UTF_8));
		TokenStream tokens = new CommonTokenStream(new MTGODraftLexer(input));
		MTGODraftParser parser = new MTGODraftParser(tokens);

		ParseTreeWalker walker = new ParseTreeWalker();
		
		mtgoDraftListener.reset();
		walker.walk(mtgoDraftListener, parser.file());
		
		RawDraftBuilder builder = new RawDraftBuilder();
		
		builder.setEventDate(createEventDate())
			.setEventId(Integer.parseInt(mtgoDraftListener.getEventId()))
			.setCardNameToRawCardMap(mtgoDraftListener.getCardNameToTempIdMap())
			.setPackToListOfPickToAvailablePicksMap(mtgoDraftListener.getPackToListOfPickToAvailablePicksMap())
			.setActivePlayer(mtgoDraftListener.getActivePlayer())
			.setOtherPlayers(mtgoDraftListener.getOtherPlayers())
			.setPackSets(mtgoDraftListener.getPackSets())
			.setTempIdToCardNameMap(mtgoDraftListener.getTempIdToCardNameMap())
			.setPackSizes(determinePackSizes());
		
		mtgoDraftListener.cleanup();
		
		return builder.build();
	}
	
	private List<Integer> determinePackSizes() {
		final List<Integer> packSizes = new ArrayList<Integer>(3);
		
		Map<Integer, List<MutablePair<Integer, List<Integer>>>> packPickMap = mtgoDraftListener.getPackToListOfPickToAvailablePicksMap();
		
		for(List<MutablePair<Integer, List<Integer>>> packPicks : packPickMap.values()) {
			packSizes.add(packPicks.size());
		}
		
		return packSizes;
	}

	private Date createEventDate() throws IOException {
		final String eventDateString = mtgoDraftListener.getEventDate();
		
		if(eventDateString.length() < 10){
			throw new IOException();
		}
		
		DateTimeFormatter dtf = DateTimeFormat.forPattern("MM/dd/yyyy h:mm:ss aa");
		DateTime eventDate = dtf.parseDateTime(eventDateString);
		return eventDate.toDate();
	}	
}
