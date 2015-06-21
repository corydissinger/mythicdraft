package com.cd.mythicdraft.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Date;

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
		
		builder.setEventDate(createEventDate());
		builder.setEventId(Integer.parseInt(mtgoDraftListener.getEventId()));
		builder.setCardNameToRawCardMap(mtgoDraftListener.getCardNameToTempIdMap());
		builder.setPackToListOfPickToAvailablePicksMap(mtgoDraftListener.getPackToListOfPickToAvailablePicksMap());
		builder.setActivePlayer(mtgoDraftListener.getActivePlayer());
		builder.setOtherPlayers(mtgoDraftListener.getOtherPlayers());
		builder.setPackSets(mtgoDraftListener.getPackSets());
		
		mtgoDraftListener.cleanup();
		
		return builder.build();
	}
	
	private Date createEventDate() {
		DateTimeFormatter dtf = DateTimeFormat.forPattern("MM/dd/yyyy h:mm:ss aa");
		DateTime eventDate = dtf.parseDateTime(mtgoDraftListener.getEventDate());
		return eventDate.toDate();
	}	
}
