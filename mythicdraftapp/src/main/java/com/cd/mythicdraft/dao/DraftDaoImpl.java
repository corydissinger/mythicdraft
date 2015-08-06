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
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.cd.mythicdraft.exception.DuplicateDraftException;
import com.cd.mythicdraft.model.Card;
import com.cd.mythicdraft.model.Draft;
import com.cd.mythicdraft.model.DraftPack;
import com.cd.mythicdraft.model.DraftPackPick;
import com.cd.mythicdraft.model.DraftPlayer;
import com.cd.mythicdraft.model.Player;
import com.cd.mythicdraft.model.Set;

@Repository(value = "draftDao")
public class DraftDaoImpl extends AbstractDAO implements DraftDAO {

	@Override
	@Transactional
	public void addDraft(Draft draft) throws DuplicateDraftException {
		//le shrug, could be better
		if(isDraftAlreadySaved(draft)) {
			throw new DuplicateDraftException();
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
	public Draft getDraftById(final Integer draftId) {
		Criteria crit = getCurrentSession().createCriteria(Draft.class);

		crit.createAlias("draftPlayers", "draftPlayers");
		crit.createAlias("draftPacks", "draftPacks");

		crit.setFetchMode("draftPlayers", FetchMode.JOIN);
		crit.setFetchMode("draftPacks", FetchMode.JOIN);		
		
		crit.add(Restrictions.eq("id", draftId));
		
		crit.addOrder(Order.asc("draftPacks.sequenceId"));
		
		final Draft draft = (Draft)crit.uniqueResult(); 
		
		return draft;
	}

	@Override
	@Transactional(readOnly = true)	
	public Collection<Draft> getRecentDrafts(final Integer numberOfDrafts) {
		Query query = getCurrentSession().createSQLQuery("SELECT ID, NAME, CREATED, EVENT_ID, EVENT_DATE, WINS, LOSSES FROM DRAFT ORDER BY CREATED DESC").addEntity(Draft.class);
		
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
	@Transactional	
	public Map<String, Card> getCardNameToCardMap(final Map<String, String> cardNameToCardSetCode) {
		Map<String, Card> cardNameToCardMap = new HashMap<String, Card>(cardNameToCardSetCode.size());
		java.util.Set<String> setCodes = new HashSet<String>();

		setCodes.addAll(cardNameToCardSetCode.values());		
		
		Criteria crit = getCurrentSession().createCriteria(Card.class).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);;
		
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
	public List<Set> getSetsByName(Collection<String> setNames) {
		Criteria crit = getCurrentSession().createCriteria(Set.class);
		
		crit.add(Restrictions.in("name", setNames));
		
		List<Set> sets = crit.list();
		
		if(CollectionUtils.isEmpty(sets)){
			sets = addPromoSets(setNames);
		}
		
		return sets;
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
	public List<Integer> getDistinctMultiverseIdsForDraft(Integer draftId) {
		Query query = getCurrentSession().createSQLQuery("SELECT DISTINCT PICK.CARD_ID "
													   + "FROM DRAFT_PACK_PICK PICK "
													   + "INNER JOIN DRAFT_PACK PACK "
													   + "   ON PACK.ID = PICK.DRAFT_PACK_ID "
													   + "INNER JOIN DRAFT DRAFT "
													   + "   ON DRAFT.ID = PACK.DRAFT_ID "
													   + "WHERE DRAFT.ID = :draftId "
													   + "UNION "
													   + "SELECT DISTINCT AVAILABLE_PICK.CARD_ID "
													   + "FROM DRAFT_PACK_AVAILABLE_PICK AVAILABLE_PICK "													   
													   + "INNER JOIN DRAFT_PACK_PICK PICK "
													   + "   ON PICK.ID = AVAILABLE_PICK.DRAFT_PACK_PICK_ID "
													   + "INNER JOIN DRAFT_PACK PACK "
													   + "   ON PACK.ID = PICK.DRAFT_PACK_ID "
													   + "INNER JOIN DRAFT DRAFT "
													   + "   ON DRAFT.ID = PACK.DRAFT_ID "
													   + "WHERE DRAFT.ID = :draftId").setInteger("draftId", draftId);													   
		
		List<Integer> multiverseIds = query.list();
		
		return multiverseIds;
	}

	@Override
	@Transactional(readOnly = true)	
	public List<Integer> getAllPicksInOrder(Integer draftId) {
		Query query = getCurrentSession().createSQLQuery("SELECT DISTINCT PICK.CARD_ID, PICK.SEQUENCE_ID AS PICK_SEQ, PACK.SEQUENCE_ID AS PACK_SEQ "
													   + "FROM DRAFT_PACK_PICK PICK "
													   + "INNER JOIN DRAFT_PACK PACK "
													   + "   ON PACK.ID = PICK.DRAFT_PACK_ID "
													   + "INNER JOIN DRAFT DRAFT "
													   + "   ON DRAFT.ID = PACK.DRAFT_ID "
													   + "WHERE DRAFT.ID = :draftId "
													   + "ORDER BY PACK_SEQ ASC, PICK_SEQ ASC").setInteger("draftId", draftId);													   

		List<Object[]> multiverseIds = query.list();
		List<Integer> picksInOrder = new ArrayList<Integer>(multiverseIds.size());
		
		for(Object[] multiverseId : multiverseIds) {
			picksInOrder.add((Integer)multiverseId[0]);
		}
		
		return picksInOrder;
	}

	@Override
	@Transactional(readOnly = true)	
	public Collection<Draft> getDraftsByPlayerId(Integer playerId) {
		Query query = getCurrentSession().createSQLQuery("SELECT D.ID, D.NAME, D.CREATED, D.EVENT_ID, D.EVENT_DATE, D.WINS, D.LOSSES "
														 + "FROM DRAFT D "
														 + "INNER JOIN DRAFT_PLAYER P "
														 + "   ON P.DRAFT_ID = D.ID "
														 + "WHERE P.PLAYER_ID = :playerId "
														 + "  AND P.IS_ACTIVE_PLAYER = true "
														 + "ORDER BY D.CREATED DESC").addEntity(Draft.class)
														 						     .setInteger("playerId", playerId);
			
		final Collection<Draft> drafts = new ArrayList<Draft>(query.list());
		
		return drafts;
	}

	@Override
	@Transactional(readOnly = true)		
	public Collection<Player> getPlayersSearch(String searchString) {
		Criteria criteria = getCurrentSession().createCriteria(Player.class)
											   .add(Restrictions.like("name", searchString, MatchMode.START));
		
		List<Player> players = criteria.list();
		
		return players;
	}	

	@Override
	@Transactional(readOnly = true)
	public Player getPlayerById(final Integer playerId) {
		Criteria crit = getCurrentSession().createCriteria(Player.class);

		crit.add(Restrictions.eq("id", playerId));		
		
		final Player player = (Player)crit.uniqueResult(); 
		
		return player;
	}	
	
}
