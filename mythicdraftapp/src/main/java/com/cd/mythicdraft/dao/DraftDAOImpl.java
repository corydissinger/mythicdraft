package com.cd.mythicdraft.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cd.mythicdraft.model.Card;
import com.cd.mythicdraft.model.Draft;
import com.cd.mythicdraft.model.Set;

@Repository(value = "draftDao")
public class DraftDAOImpl extends AbstractDAO implements DraftDAO {

	@Override
	public void addDraft(Draft draft) {
		Session session = getCurrentSession();
		session.persist(draft);
	}

	@Override
	public Map<String, Card> getCardNameToCardMap(final Map<String, String> cardNameToCardSetCode) {
		Map<String, Card> cardNameToCardMap = new HashMap<String, Card>(cardNameToCardSetCode.size());
		
		return null;
	}

	@Override
	public List<Set> getAvailableSets() {
		Session session = getCurrentSession();
		
		return session.createCriteria(Set.class).list();
	}

}
