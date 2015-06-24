package com.cd.mythicdraft.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
	@Transactional(readOnly = true)	
	public Map<String, Card> getCardNameToCardMap(final Map<String, String> cardNameToCardSetCode) {
		Map<String, Card> cardNameToCardMap = new HashMap<String, Card>(cardNameToCardSetCode.size());
		
		return null;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Set> getAvailableSets() {
		Session session = getCurrentSession();
		
		return session.createCriteria(Set.class).list();
	}

	@Override
	@Transactional
	public void persistSets(List<Set> sets) {
		Session session = getCurrentSession();
		
		for(Set newSet : sets) {
			session.save(newSet);			
		}
	}

	@Override
	@Transactional	
	public void persistCards(List<Card> cardsInSet) {
		Session session = getCurrentSession();
		
		for(Card aCard : cardsInSet) {
			session.save(aCard);
		}
	}

}
