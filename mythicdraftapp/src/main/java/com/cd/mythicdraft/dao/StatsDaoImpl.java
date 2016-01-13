package com.cd.mythicdraft.dao;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.cd.mythicdraft.model.Card;
import com.cd.mythicdraft.model.Format;
import com.cd.mythicdraft.model.FormatPickStats;

@Repository(value = "statsDao")
public class StatsDaoImpl extends AbstractDao implements StatsDao {

	@Override
	public void addFormat(Format aFormat) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addFormatPickStats(FormatPickStats aFormatPickStats) {
		// TODO Auto-generated method stub
		
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
		
		List<Double> theAverage = query.list();
		
		BigDecimal theValue = new BigDecimal(theAverage.get(0));
		
		return theValue;
	}

}
