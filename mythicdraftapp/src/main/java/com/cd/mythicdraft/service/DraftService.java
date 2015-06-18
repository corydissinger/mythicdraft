package com.cd.mythicdraft.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cd.mythicdraft.dao.DraftDAO;
import com.cd.mythicdraft.model.Draft;

@Service(value = "draftService")
public class DraftService {

	@Autowired
	private DraftDAO draftDao;
	
	@Transactional
	public void addDraft(Draft aDraft) {
		draftDao.addDraft(aDraft);
	}
}
