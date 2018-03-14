package com.cd.mythicdraft.rest.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Service;

import com.cd.mythicdraft.rest.domain.RawDraft;
import com.cd.mythicdraft.rest.domain.RawDraftBuilder;
import com.cd.mythicdraft.rest.grammar.impl.MTGODraftListenerImpl;

@Service(value = "mtgoDraftParserService")
public class MtgoDraftParserService implements BeanFactoryAware {

	private static final Logger logger = Logger.getLogger(MtgoDraftParserService.class);	
	
	private BeanFactory beanFactory;
	
	public RawDraft parse(InputStream mtgoDraft) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(mtgoDraft, StandardCharsets.UTF_8));
		
		MTGODraftListenerImpl mtgoDraftListener = beanFactory.getBean(MTGODraftListenerImpl.class);
		mtgoDraftListener.reset();
		
		for(String line = br.readLine(); line != null; line = br.readLine()){
			mtgoDraftListener.handleLine(line);
		}
		
		RawDraftBuilder builder = new RawDraftBuilder();
		
		builder.setEventDate(createEventDate(mtgoDraftListener.getEventDate()))
			.setEventId(Integer.parseInt(mtgoDraftListener.getEventId()))
			.setCardNameToRawCardMap(mtgoDraftListener.getCardNameToTempIdMap())
			.setPackToListOfPickToAvailablePicksMap(mtgoDraftListener.getPackToListOfPickToAvailablePicksMap())
			.setActivePlayer(mtgoDraftListener.getActivePlayer())
			.setOtherPlayers(mtgoDraftListener.getOtherPlayers())
			.setPackSets(mtgoDraftListener.getPackSets())
			.setTempIdToCardNameMap(mtgoDraftListener.getTempIdToCardNameMap())
			.setPackSizes(determinePackSizes(mtgoDraftListener.getPackToListOfPickToAvailablePicksMap()));
		
		mtgoDraftListener.cleanup();
		
		return builder.build();
	}
	
	private List<Integer> determinePackSizes(Map<Integer, List<MutablePair<Integer, List<Integer>>>> packPickMap) {
		final List<Integer> packSizes = new ArrayList<Integer>(3);
		
		for(List<MutablePair<Integer, List<Integer>>> packPicks : packPickMap.values()) {
			packSizes.add(packPicks.size());
		}
		
		return packSizes;
	}

	private Date createEventDate(String rawDate) throws IOException {
		if(rawDate.length() < 10){
			throw new IOException();
		}
		
		DateTimeFormatter dtf = DateTimeFormat.forPattern("MM/dd/yyyy h:mm:ss aa");
		DateTime eventDate = dtf.parseDateTime(rawDate);
		return eventDate.toDate();
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;		
	}	
}
