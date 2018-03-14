var CardDeckOrganizer = CardDeckOrganizer || {

	sortCards: function(unsortedCards) {
		var sortedDeck = { mainDeckCards: {}, sideBoardCards: {} };	
	
		var cardSortingFunction = function(unsortedCardsArray) {
			var sortedCardsObject = { land: [], cmcArrays: [] };
		
			for(var i = 0; i < unsortedCardsArray.length; i++) {
				var cardObject = unsortedCardsArray[i];
				
				if(!cardObject.isCreature && !cardObject.isNonCreature) {
					for(var j = 0; j < cardObject.count; j++) {
						sortedCardsObject.land.push(cardObject);
					}
					
					continue;
				}
				
				var cmcArray = sortedCardsObject.cmcArrays[cardObject.cmc];
				
				if(!cmcArray) {
					cmcArray = [];
				}
				
				for(var j = 0; j < cardObject.count; j++) {
					cmcArray.push(cardObject);
				}				
				
				sortedCardsObject.cmcArrays[cardObject.cmc] = cmcArray;
			}		
			
			return sortedCardsObject;
		};		
	
		sortedDeck.mainDeckCards = cardSortingFunction(unsortedCards.mainDeckCards);
		sortedDeck.sideBoardCards = cardSortingFunction(unsortedCards.sideBoardCards);
	
		return sortedDeck;
	}

};