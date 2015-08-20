package com.cd.mythicdraft.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.springframework.stereotype.Service;

import com.cd.mythicdraft.domain.RawDeck;
import com.cd.mythicdraft.domain.RawDeckBuilder;

@Service(value = "mtgoDeckParserService")
public class MtgoDeckParserService {
	
	public RawDeck parse(InputStream mtgoDeck) throws IOException {
		RawDeckBuilder builder = new RawDeckBuilder();
		Map<String, Integer> cardNameToTempIdMap = new HashMap<String, Integer>();
		List<MutablePair<Integer, Integer>> listOfMainDeckCards = new ArrayList<MutablePair<Integer, Integer>>();
		List<MutablePair<Integer, Integer>> listOfSideBoardCards = new ArrayList<MutablePair<Integer, Integer>>();
		
		boolean isMainDeck = true;
		int uniqueCardCount = 0;
		
		List<String> fileLines = IOUtils.readLines(mtgoDeck, StandardCharsets.UTF_8);
		
		for(String aLine : fileLines) {
			System.out.println(aLine);
			
			final String[] lineParts = aLine.split(" ");

			if(lineParts.length == 1) {
				isMainDeck = false;
				continue;
			}

			final StringBuilder cardNameBuilder = new StringBuilder();
			final Integer cardCount = Integer.parseInt(lineParts[0]);
			
			for(int i = 1; i < lineParts.length; i++) {
				cardNameBuilder.append(lineParts[i]);
				
				if(i - 1 != lineParts.length) {
					cardNameBuilder.append(" ");
				}
			}
			
			//Figure out the temporary card ID and jazz
			final String cardName = cardNameBuilder.toString();
			final Integer cardId;
			
			if(cardNameToTempIdMap.containsKey(cardName)) {
				cardId = cardNameToTempIdMap.get(cardName);
			} else {
				cardNameToTempIdMap.put(cardName, uniqueCardCount);
				cardId = uniqueCardCount++;
			}
			
			//Figure out which list it should be in
			final MutablePair<Integer, Integer> newCardIdToCount = new MutablePair<Integer, Integer>(cardId, cardCount);
			
			if(isMainDeck) {
				listOfMainDeckCards.add(newCardIdToCount);
			} else {
				listOfSideBoardCards.add(newCardIdToCount);
			}
		}
		
		builder.setCardNameToTempIdMap(cardNameToTempIdMap)
			.setListOfMainDeckCards(listOfMainDeckCards)
			.setListOfSideBoardCards(listOfSideBoardCards);
		
		return builder.build();		
	}
}
