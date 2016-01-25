package com.cd.mythicdraft.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.cd.mythicdraft.model.Card;
import com.cd.mythicdraft.model.Format;
import com.cd.mythicdraft.model.PairedSets;
import com.cd.mythicdraft.model.Set;

@Repository(value = "cardDao")
public class CardDaoImpl extends AbstractDao implements CardDao {

	@Autowired
	private DraftDao draftDao;
	
	@Override
	public Map<String, Card> getCardNameToCardMap(Map<String, String> cardNameToCardSetCode, List<Set> sets) {
		Map<String, Card> cardNameToCardMap = new HashMap<String, Card>(cardNameToCardSetCode.size());
		List<String> setCodes = new ArrayList<String>(3);

		for(Set aSet : sets) {
			setCodes.add(aSet.getName());
		}
		
		List<Set> pairedSets = getSetsByName(PairedSets.getAllPairedSetCodes(setCodes));
		
		if(!pairedSets.equals(sets)) {
			sets.addAll(pairedSets);	
		}
		
		setCodes.addAll(PairedSets.getAllPairedSetCodes(new ArrayList<String>(cardNameToCardSetCode.values())));		
		
		Criteria crit = getCurrentSession().createCriteria(Card.class).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		crit.add(Restrictions.in("cardName", cardNameToCardSetCode.keySet()));
		
		if(!CollectionUtils.isEmpty(sets) && (null == sets.get(0).isPromo() || !sets.get(0).isPromo())) {
			crit.add(Restrictions.in("set", sets));			
		} 
		
		List<Card> results = crit.list();
		
		for(Card result : results) {
			cardNameToCardMap.put(result.getCardName(), result);
		}
		
		return cardNameToCardMap;
	}

	@Override
	public void persistSets(List<Set> sets) {
		Session session = getCurrentSession();
		
		for(Set newSet : sets) {
			session.save(newSet);			
		}
	}

	@Override
	public void persistCards(List<Card> cardsInSet) {
		Session session = getCurrentSession();
		
		for(Card aCard : cardsInSet) {
			session.merge(aCard);
		}
	}

	@Override
	public List<Set> getSetsByName(Collection<String> setNames) {
		Criteria crit = getCurrentSession().createCriteria(Set.class);
		
		crit.add(Restrictions.in("name", setNames));
		
		List<Set> sets = crit.list();
		
		return sets;
	}	

	@Override
	public List<Set> addPromoSets(Collection<String> setNames) {
		Session session = getCurrentSession();
		
		List<Set> sets = new ArrayList<Set>(setNames.size());
		List<String> usedNames = new ArrayList<String>();
		
		for(String aSetName : setNames) {
			if(usedNames.contains(aSetName)) {
				continue;
			}
			
			Set set = new Set();
			
			set.setName(aSetName);
			set.setIsPromo(true);
			
			session.persist(set);			
			
			sets.add(set);
			usedNames.add(aSetName);
		}
		
		final Set packOne;
		final Set packTwo;
		final Set packThree;
		
		if(sets.size() > 1) {
			packOne = sets.get(0);
			packTwo = sets.get(1);
			packThree = sets.get(2);
		} else {
			packOne = sets.get(0);
			packTwo = sets.get(0);
			packThree = sets.get(0);
		}

		Format newPromoFormat = draftDao.getFormatByPacks(packOne, packTwo, packThree);		

		if(newPromoFormat == null) {
			newPromoFormat = new Format();
		}
		
		newPromoFormat.setFirstPackSet(packOne);
		newPromoFormat.setSecondPackSet(packTwo);
		newPromoFormat.setThirdPackSet(packThree);
		
		newPromoFormat.setFirstPack(packOne.getId());
		newPromoFormat.setSecondPack(packTwo.getId());
		newPromoFormat.setThirdPack(packThree.getId());		
		
		if(newPromoFormat.getId() != null && newPromoFormat.getId() > 0) {
			session.merge(newPromoFormat);			
		} else {
			session.persist(newPromoFormat);
		}		
		
		return sets;
	}

	@Override
	public List<Set> getAvailableSets() {
		Session session = getCurrentSession();
		
		return session.createCriteria(Set.class).list();
	}
	
	@Override
	public Map<Integer, Card> getTempCardIdToCardMap(Map<String, Integer> cardNameToTempIdMap, List<Set> sets) {
		Map<Integer, Card> cardNameToCardMap = new HashMap<Integer, Card>(cardNameToTempIdMap.size());
		List<String> cardList = new ArrayList<String>();
		List<String> setCodes = new ArrayList<String>(3);
		cardList.addAll(cardNameToTempIdMap.keySet());

		for(Set aSet : sets) {
			setCodes.add(aSet.getName());
		}
		
		sets.addAll(getSetsByName(PairedSets.getAllPairedSetCodes(setCodes)));
		
		Criteria crit = getCurrentSession().createCriteria(Card.class).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		crit.add(Restrictions.in("cardName", cardList));
		
		if(!CollectionUtils.isEmpty(sets) && (null == sets.get(0).isPromo() || !sets.get(0).isPromo())) {
			crit.add(Restrictions.in("set", sets));
		}

		final List<Card> results = crit.list();
		
		for(Card result : results) {
			cardNameToCardMap.put(cardNameToTempIdMap.get(result.getCardName()), result);
		}
		
		return cardNameToCardMap;
	}

	@Override
	public Card getCardByName(String cardName) {
		Criteria crit = getCurrentSession().createCriteria(Card.class);
		
		crit.add(Restrictions.eq("cardName", cardName));
		
		if(crit.list().isEmpty()) {
			return null;
		} else {
			return (Card) crit.list().get(0);	
		}			
	}
	
}
