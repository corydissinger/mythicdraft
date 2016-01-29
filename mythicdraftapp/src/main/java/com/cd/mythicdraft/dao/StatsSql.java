package com.cd.mythicdraft.dao;

public class StatsSql {

	public static final String GET_DRAFTS_WITHOUT_FORMAT = "SELECT new Draft(D.id) "
														 + "FROM Draft D"
		   									      		 + " WHERE D.formatId IS NULL";
	
	public static final String GET_FORMATS = "FROM Format format ";
	
	public static final String GET_CARDS_FOR_FORMAT = "SELECT C.id "
													+ "  FROM Card C "
													+ "  JOIN Format AS F "
													+ "    ON F.firstPack = C.setId "
													+ "  JOIN Format AS F2 "
													+ "    ON F2.secondPack = C.setId "
													+ "  JOIN Format AS F3 "
													+ "    ON F3.thirdPack = C.setId "
													+ " WHERE F.id = :formatId AND F2.id = :formatId AND F3.id = :formatId ";
													
	
	public static final String GET_AVG_PICK_CARD_FORMAT = "SELECT AVG(DPP.SEQUENCE_ID)"
			   											+ "  FROM DraftPackPick DPP"
			   											+ "  JOIN DraftPack DP"
			   											+ "    ON DPP.draftPackId = DP.id"
			   											+ "  JOIN Draft D"
			   											+ "    ON DP.draftId = D.id"
			   											+ " WHERE DPP.cardId = :cardId"
			   											+ "   AND D.formatId = :formatId";

	public static final String GET_NEW_FORMATS = "SELECT DISTINCT NEW Format(dp1.setId, dp2.setId, dp3.setId) "
												+ " FROM Draft draft "
												+ " JOIN draft.draftPacks as dp1 "
												+ " JOIN draft.draftPacks as dp2 "
												+ " JOIN draft.draftPacks as dp3 "
												+ " WHERE draft.formatId is null AND dp1.sequenceId = 0 AND dp2.sequenceId = 1 AND dp3.sequenceId = 2 ";												
	
}
