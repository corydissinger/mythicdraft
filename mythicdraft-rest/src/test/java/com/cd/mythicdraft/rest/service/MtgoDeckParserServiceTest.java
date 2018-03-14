package com.cd.mythicdraft.rest.service;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.cd.mythicdraft.rest.domain.RawDeck;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader=AnnotationConfigContextLoader.class)
public class MtgoDeckParserServiceTest {

	@Configuration
	static class ContextConfiguration {
		@Bean
		public MtgoDeckParserService mtgoDeckParserService() {
			MtgoDeckParserService mtgoDraftParserService = new MtgoDeckParserService();
			return mtgoDraftParserService;
		}
	}
	
	@Autowired
	private MtgoDeckParserService mtgoDeckParserService;
	
	@Test
	public void testTPR() throws Exception{
		InputStream tprDraftStream = loadResource("ORIDECK.txt");
		
		RawDeck aDeck = mtgoDeckParserService.parse(tprDraftStream);
		
		assertTrue(aDeck.getCardNameToTempIdMap().size() > 35);
		assertTrue(!aDeck.getListOfMainDeckCards().isEmpty());
		assertTrue(!aDeck.getListOfSideBoardCards().isEmpty());
		
		System.out.println(aDeck.toString());
	}

	private InputStream loadResource(String resourceName) throws IOException {
		return this.getClass().getResourceAsStream(resourceName);
	}
}
