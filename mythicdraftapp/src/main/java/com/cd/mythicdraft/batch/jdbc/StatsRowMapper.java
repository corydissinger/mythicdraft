package com.cd.mythicdraft.batch.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.jdbc.core.RowMapper;

import com.cd.mythicdraft.model.Card;
import com.cd.mythicdraft.model.Format;

public class StatsRowMapper implements RowMapper<ImmutablePair<Card, Format>> {

	@Override
	public ImmutablePair<Card, Format> mapRow(ResultSet rs, int rowNum) throws SQLException {
		Card aCard = new Card();
		Format aFormat = new Format();
		
		aCard.setId(rs.getInt("CARD_ID"));
		aFormat.setId(rs.getInt("FORMAT_ID"));

		ImmutablePair<Card, Format> cardFormat = new ImmutablePair<Card, Format>(aCard, aFormat);
		
		return cardFormat;
	}

}
