package com.cd.mythicdraft.dao;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cd.mythicdraft.model.Card;
import com.cd.mythicdraft.model.Draft;

@Repository(value = "draftDao")
public class DraftDAOImpl implements DraftDAO {

	@Autowired
	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sf) {
		sessionFactory = sf;
	}

	@Override
	public void addDraft(Draft draft) {
		Session session = this.sessionFactory.getCurrentSession();
		session.persist(draft);
	}

	@Override
	public Map<String, Card> getCardNameToCardMap(final Map<String, String> cardNameToCardSetCode) {
		Map<String, Card> cardNameToCardMap = new HashMap<String, Card>(cardNameToCardSetCode.size());
		
		return null;
	}

}
