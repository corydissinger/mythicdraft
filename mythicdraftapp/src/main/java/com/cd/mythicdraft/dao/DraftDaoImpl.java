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
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.cd.mythicdraft.exception.DuplicateDraftException;
import com.cd.mythicdraft.model.Card;
import com.cd.mythicdraft.model.Deck;
import com.cd.mythicdraft.model.DeckCard;
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
		Query query = getCurrentSession().createSQLQuery("SELECT * FROM Player WHERE UPPER(NAME) LIKE UPPER(:searchString) ORDER BY NAME ASC")
				 							 .addEntity(Player.class)
				 						     .setString("searchString", searchString + "%")
				 						     .setMaxResults(20);		
				
		List<Player> players = query.list();
		
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

	@Override
	@Transactional
	public Integer addDeck(Deck deck) throws DuplicateDraftException {
		Session session = getCurrentSession();
		
		session.persist(deck);
		
		for(DeckCard deckCard : deck.getDeckCards()) {
			session.persist(deckCard);
		}
		
		return deck.getId();
	}

	@Override
	@Transactional(readOnly = true)
	public Deck getDeckById(Integer deckId) {
		Criteria crit = getCurrentSession().createCriteria(Deck.class);
		
		crit.add(Restrictions.eq("id", deckId));
		
		final Deck deck = (Deck) crit.uniqueResult();
		
		return deck;
	}	
	
}
