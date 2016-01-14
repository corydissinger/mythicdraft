package com.cd.mythicdraft.dao;

public class StatsSql {

	public static final String GET_DRAFTS_WITHOUT_FORMAT = "SELECT D.ID"
		   									      		 + "  FROM DRAFT D"
		   									      		 + " WHERE D.FORMAT_ID IS NULL";
	
	public static final String GET_FORMATS_AND_CARDS = "SELECT F.ID AS FORMAT_ID, C.ID AS CARD_ID "
													 + "  FROM FORMAT F"
													 + "  JOIN CARD AS C"
													 + "    ON C.SET_ID = F.FIRST_PACK "
													 + "    OR C.SET_ID = F.SECOND_PACK "
													 + "    OR C.SET_ID = F.THIRD_PACK ";
	
	public static final String GET_CARDS_FOR_FORMAT = "SELECT C.ID "
													+ "  FROM CARD C "
													+ "  JOIN FORMAT AS F "
													+ "    ON F.FIRST_PACK = C.SET_ID "
													+ "  JOIN FORMAT AS F2 "
													+ "    ON F2.SECOND_PACK = C.SET_ID "
													+ "  JOIN FORMAT AS F3 "
													+ "    ON F3.THIRD_PACK = C.SET_ID "
													+ " WHERE F.ID = :formatId AND F2.ID = :formatId AND F3.ID = :formatId ";
													
	
	public static final String GET_AVG_PICK_CARD_FORMAT = "SELECT AVG(DPP.SEQUENCE_ID)"
			   											+ "  FROM DRAFT_PACK_PICK DPP"
			   											+ "  JOIN DRAFT_PACK DP"
			   											+ "    ON DPP.DRAFT_PACK_ID = DP.ID"
			   											+ "  JOIN DRAFT D"
			   											+ "    ON DP.DRAFT_ID = D.ID"
			   											+ " WHERE DPP.CARD_ID = :cardId"
			   											+ "   AND D.FORMAT_ID = :formatId";
	
}
