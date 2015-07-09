package com.cd.mythicdraft.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cd.mythicdraft.model.Card;
import com.cd.mythicdraft.model.Draft;
import com.cd.mythicdraft.model.DraftPack;
import com.cd.mythicdraft.model.DraftPackPick;
import com.cd.mythicdraft.model.DraftPlayer;
import com.cd.mythicdraft.model.Player;
import com.cd.mythicdraft.model.Set;

@Repository(value = "draftDao")
public class DraftDAOImpl extends AbstractDAO implements DraftDAO {

	@Override
	@Transactional
	public void addDraft(Draft draft) {
		//le shrug, could be better
		if(isDraftAlreadySaved(draft)) {
			return;
		}
		
		Session session = getCurrentSession();

		draft.setCreated(new Date());
		session.persist(draft);		
		
		for(DraftPlayer draftPlayer : draft.getDraftPlayers()) {
			Player player = draftPlayer.getPlayer();
			
			if(player.getId() == null) {
				session.persist(player);
			} else {
				session.merge(player);
			}
			
			draftPlayer.setDraftId(draft.getId());
			draftPlayer.setPlayerId(player.getId());
			
			session.merge(draftPlayer);
		}
		
		for(DraftPack draftPack : draft.getDraftPacks()) {
			session.persist(draftPack);
		}
	}
	
	@Override
	@Transactional(readOnly = true)
	public Draft getDraftByActivePlayer(final Integer draftId, final Integer activePlayerId) {
		Criteria crit = getCurrentSession().createCriteria(Draft.class);

		crit.createAlias("draftPlayers", "draftPlayers");
		crit.createAlias("draftPacks", "draftPacks");

		crit.setFetchMode("draftPlayers", FetchMode.JOIN);
		crit.setFetchMode("draftPacks", FetchMode.JOIN);		
		
		crit.add(Restrictions.eq("id", draftId));
		crit.add(Restrictions.eq("draftPlayers.playerId", activePlayerId));
		
		crit.addOrder(Order.asc("draftPacks.sequenceId"));
		
		final Draft draft = (Draft)crit.uniqueResult(); 
		
		return draft;
	}

	@Override
	@Transactional(readOnly = true)	
	public Collection<Draft> getRecentDrafts(final Integer numberOfDrafts) {
		Query query = getCurrentSession().createSQLQuery("SELECT ID, NAME, CREATED, EVENT_ID, EVENT_DATE FROM DRAFT ORDER BY CREATED DESC").addEntity(Draft.class);
		
		query.setMaxResults(numberOfDrafts);
		
		final Collection<Draft> drafts = new ArrayList<Draft>(query.list());
		
		return drafts;
	}	
	
	@Override
	@Transactional(readOnly = true)
	public DraftPackPick getPackByIdAndPick(final Integer draftPackId, final Integer pickId) {
		Criteria crit = getCurrentSession().createCriteria(DraftPackPick.class);

		crit.add(Restrictions.eq("draftPackId", draftPackId));
		crit.add(Restrictions.eq("sequenceId", pickId));
		
		final DraftPackPick draftPackPick = (DraftPackPick)crit.uniqueResult(); 
		
		return draftPackPick;
	}	

	@Override
	@Transactional(readOnly = true)
	public boolean isDraftAlreadySaved(final Draft draft) {
		Player activePlayer = null;
		
		for(DraftPlayer draftPlayer : draft.getDraftPlayers()) {
			if(draftPlayer.getIsActivePlayer()) {
				activePlayer = draftPlayer.getPlayer();
			}
		}
		
		Criteria criteria = getCurrentSession().createCriteria(Draft.class);
		
		criteria.add(Restrictions.eq("eventId", draft.getEventId()));
		
		List<Draft> existingDrafts = criteria.list();
		
		for(Draft existingDraft : existingDrafts) {
			for(DraftPlayer existingPlayer : existingDraft.getDraftPlayers()) {
				if(existingPlayer.getIsActivePlayer()) {
					if(existingPlayer.getPlayer().equals(activePlayer)) {
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	@Override
	@Transactional(readOnly = true)	
	public Map<String, Card> getCardNameToCardMap(final Map<String, String> cardNameToCardSetCode) {
		Map<String, Card> cardNameToCardMap = new HashMap<String, Card>(cardNameToCardSetCode.size());
		java.util.Set<String> setCodes = new HashSet<String>();

		setCodes.addAll(cardNameToCardSetCode.values());		
		
		Criteria crit = getCurrentSession().createCriteria(Card.class);
		
		crit.add(Restrictions.in("cardName", cardNameToCardSetCode.keySet()));
		crit.add(Restrictions.in("set", getSetsByName(setCodes)));
		
		List<Card> results = crit.list();
		
		for(Card result : results) {
			cardNameToCardMap.put(result.getCardName(), result);
		}
		
		return cardNameToCardMap;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Set> getSetsByName(Collection<String> setNames) {
		Criteria crit = getCurrentSession().createCriteria(Set.class);
		
		crit.add(Restrictions.in("name", setNames));
		
		return crit.list();
	}
	
	@Override
	@Transactional(readOnly = true)
	public Map<String, Player> getPlayersByName(Collection<String> playerNames) {
		Map<String, Player> nameToPlayerMap = new HashMap<String, Player>();
		
		Criteria crit = getCurrentSession().createCriteria(Player.class);
		
		crit.add(Restrictions.in("name", playerNames));
		
		List<Player> existingPlayers = crit.list();
		
		for(Player existingPlayer : existingPlayers){
			nameToPlayerMap.put(existingPlayer.getName(), existingPlayer);
		}
		
		return nameToPlayerMap;
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
