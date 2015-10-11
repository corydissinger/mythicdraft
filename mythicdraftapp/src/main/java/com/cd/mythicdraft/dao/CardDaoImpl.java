package com.cd.mythicdraft.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.cd.mythicdraft.model.Card;
import com.cd.mythicdraft.model.PairedSets;
import com.cd.mythicdraft.model.Set;

@Repository(value = "cardDao")
public class CardDaoImpl extends AbstractDAO implements CardDao {

	@Override
	@Transactional	
	public Map<String, Card> getCardNameToCardMap(Map<String, String> cardNameToCardSetCode) {
		Map<String, Card> cardNameToCardMap = new HashMap<String, Card>(cardNameToCardSetCode.size());
		java.util.Set<String> setCodes = new HashSet<String>();

		setCodes.addAll(PairedSets.getAllPairedSetCodes(new ArrayList<String>(cardNameToCardSetCode.values())));		
		
		Criteria crit = getCurrentSession().createCriteria(Card.class).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		crit.add(Restrictions.in("cardName", cardNameToCardSetCode.keySet()));
		
		List<Set> sets = getSetsByName(setCodes);
		
		if(null == sets.get(0).isPromo() || !sets.get(0).isPromo()) {
			crit.add(Restrictions.in("set", sets));			
		} 
		
		List<Card> results = crit.list();
		
		for(Card result : results) {
			cardNameToCardMap.put(result.getCardName(), result);
		}
		
		return cardNameToCardMap;
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
			session.merge(aCard);
		}
	}

	@Override
	@Transactional
	public List<Set> getSetsByName(Collection<String> setNames) {
		Criteria crit = getCurrentSession().createCriteria(Set.class);
		
		crit.add(Restrictions.in("name", setNames));
		
		List<Set> sets = crit.list();
		
		if(CollectionUtils.isEmpty(sets)){
			sets = addPromoSets(setNames);
		}
		
		return sets;
	}	
	
	@Transactional
	private List<Set> addPromoSets(Collection<String> setNames) {
		Session session = getCurrentSession();
		
		List<Set> sets = new ArrayList<Set>(setNames.size());
		
		for(String aSetName : setNames) {
			Set set = new Set();
			
			set.setName(aSetName);
			set.setIsPromo(true);
			
			session.persist(set);			
			
			sets.add(set);
		}
		
		return sets;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Set> getAvailableSets() {
		Session session = getCurrentSession();
		
		return session.createCriteria(Set.class).list();
	}
	
	@Override
	@Transactional(readOnly = true)
	public Map<Integer, Card> getTempCardIdToCardMap(Map<String, Integer> cardNameToTempIdMap, Collection<String> setCodes) {
		Map<Integer, Card> cardNameToCardMap = new HashMap<Integer, Card>(cardNameToTempIdMap.size());
		List<String> cardList = new ArrayList<String>();
		cardList.addAll(cardNameToTempIdMap.keySet());

		List<Set> sets = getSetsByName(PairedSets.getAllPairedSetCodes(setCodes));
		
		Criteria crit = getCurrentSession().createCriteria(Card.class).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		crit.add(Restrictions.in("cardName", cardList));
		
		if(null == sets.get(0).isPromo() || !sets.get(0).isPromo()) {
			crit.add(Restrictions.in("set", sets));
		}

		final List<Card> results = crit.list();
		
		for(Card result : results) {
			cardNameToCardMap.put(cardNameToTempIdMap.get(result.getCardName()), result);
		}
		
		return cardNameToCardMap;
	}

	@Override
	@Transactional(readOnly = true)
	public Card getCardByName(String cardName) {
		Criteria crit = getCurrentSession().createCriteria(Card.class);
		
		crit.add(Restrictions.eq("cardName", cardName));
		
		return (Card) crit.list().get(0);
	}
	
}
