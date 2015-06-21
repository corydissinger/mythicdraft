package com.cd.mythicdraft.service;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cd.mythicdraft.dao.DraftDAO;
import com.cd.mythicdraft.model.Draft;

@Service(value = "draftService")
public class DraftService {

	@Autowired
	private DraftDAO draftDao;
	
	@Autowired
	private MtgoDraftParserService mtgoDraftParserService;
	
	@Transactional
	public void addDraft(InputStream mtgoDraftStream) {
		Draft aDraft;
		
		try {
			aDraft = mtgoDraftParserService.parse(mtgoDraftStream);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		draftDao.addDraft(aDraft);
	}
}
