package com.cd.mythicdraft.dao;

public class StatsSql {

	public static final String GET_DRAFTS_WITHOUT_FORMAT = "SELECT D.ID"
		   									      		 + "  FROM DRAFT D"
		   									      		 + " WHERE D.FORMAT_ID IS NULL";
	
}
