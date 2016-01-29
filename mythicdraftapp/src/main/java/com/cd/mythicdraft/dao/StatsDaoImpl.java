package com.cd.mythicdraft.dao;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cd.mythicdraft.model.Card;
import com.cd.mythicdraft.model.Format;
import com.cd.mythicdraft.model.FormatPickStats;

@Repository(value = "statsDao")
public class StatsDaoImpl extends AbstractDao implements StatsDao {

	@Override
	@Transactional
	public void addFormatPickStats(FormatPickStats aFormatPickStats) {
		if(aFormatPickStats.getId() == null || aFormatPickStats.getId() == 0) {
			getCurrentSession().persist(aFormatPickStats);	
		} else {
			getCurrentSession().merge(aFormatPickStats);
		}
		
	}

	@Override
	@Transactional(readOnly = true)
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
	@Transactional(readOnly = true)
	public FormatPickStats getFormatPickStats(Card aCard, Format aFormat) {
		Criteria crit = getCurrentSession().createCriteria(FormatPickStats.class);

		crit.add(Restrictions.eq("cardId", aCard.getId()));
		crit.add(Restrictions.eq("formatId", aFormat.getId()));
				
		return (FormatPickStats) crit.uniqueResult();
	}

	@Override
	@Transactional(readOnly = true)
	public List<Format> getFormats() {
		Query query = getCurrentSession().createSQLQuery("SELECT DISTINCT FORMAT.ID, FORMAT.FIRST_PACK, FORMAT.SECOND_PACK, FORMAT.THIRD_PACK, S.NAME "
														+ " FROM FORMAT FORMAT "
														+ " JOIN SET S "
														+ "   ON S.ID = FORMAT.FIRST_PACK"
														+ " ORDER BY S.NAME ASC ").addEntity(Format.class);
		
		List<Format> formats = (List<Format>) query.list();
		
		return formats;
	}

	@Override
	public Format getFormat(int formatId) {
		Criteria crit = getCurrentSession().createCriteria(Format.class);

		crit.add(Restrictions.eq("id", formatId));
				
		return (Format) crit.uniqueResult();
	}

	@Override
	@Transactional(readOnly = true)
	public List<FormatPickStats> getFormatPickStats(int formatId) {
		Criteria crit = getCurrentSession().createCriteria(FormatPickStats.class);

		crit.add(Restrictions.eq("formatId", formatId));
				
		return (List<FormatPickStats>) crit.list();
	}

}
