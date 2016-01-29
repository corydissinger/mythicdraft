package com.cd.mythicdraft.batch.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class DraftRowMapper implements RowMapper<Integer> {

	@Override
	public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
		Integer theDraftId = rs.getInt("D.ID");
		
		return theDraftId;
	}

}
