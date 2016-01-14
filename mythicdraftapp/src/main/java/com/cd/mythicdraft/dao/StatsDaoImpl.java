package com.cd.mythicdraft.dao;

import java.math.BigDecimal;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.cd.mythicdraft.model.Card;
import com.cd.mythicdraft.model.Format;
import com.cd.mythicdraft.model.FormatPickStats;

@Repository(value = "statsDao")
public class StatsDaoImpl extends AbstractDao implements StatsDao {

	@Override
	public void addFormatPickStats(FormatPickStats aFormatPickStats) {
		if(aFormatPickStats.getId() == null || aFormatPickStats.getId() == 0) {
			getCurrentSession().persist(aFormatPickStats);	
		} else {
			getCurrentSession().merge(aFormatPickStats);
		}
		
	}

	@Override
	public BigDecimal getCardFormatAverage(Card aCard, Format aFormat) {
		Query query = getCurrentSession().createSQLQuery("SELECT AVG(DPP.SEQUENCE_ID)"
													   + "  FROM DRAFT_PACK_PICK DPP"
													   + "  JOIN DRAFT_PACK DP"
													   + "    ON DPP.DRAFT_PACK_ID = DP.ID"
													   + "  JOIN DRAFT D"
													   + "    ON DP.DRAFT_ID = D.ID"
													   + " WHERE DPP.CARD_ID = :cardId"
													   + "   AND D.FORMAT_ID = :formatId").setInteger("cardId", aCard.getId())
													   									  .setInteger("formatId", aFormat.getId());
		
		BigDecimal theAverage = (BigDecimal) query.uniqueResult();
		
		return theAverage;
	}

	@Override
	public FormatPickStats getFormatPickStats(Card aCard, Format aFormat) {
		Criteria crit = getCurrentSession().createCriteria(FormatPickStats.class);

		crit.add(Restrictions.eq("cardId", aCard.getId()));
		crit.add(Restrictions.eq("formatId", aFormat.getId()));
				
		return (FormatPickStats) crit.uniqueResult();
	}

}
