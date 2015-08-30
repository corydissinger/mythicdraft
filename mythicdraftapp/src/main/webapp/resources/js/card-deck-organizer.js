var CardDeckOrganizer = CardDeckOrganizer || {

	sortCards: function(unsortedCards) {
		var sortedDeck = { mainDeckCards: {}, sideBoardCards: {} };
	
		var mainDeckCards = { land: [], cmcArrays: [] };
		var sideBoardCards = { land: [], cmcArrays: [] };
	
		var cardSortingFunction = function(unsortedCardsArray, sortedCardsObject) {
			for(var i = 0; i < unsortedCardsArray.length; i++) {
				var cardObject = unsortedCardsArray[i];
				
				if(!cardObject.isCreature && !cardObject.isNonCreature) {
					sortedCardsObject.land.push(cardObject);
					continue;
				}
				
				var cmcArray = sortedCardsObject.cmcArrays[cardObject.cmc];
				
				if(!cmcArray) {
					cmcArray = [];
				}
				
				cmcArray.push(cardObject);
				
				sortedCardsObject.cmcArrays[cardObject.cmc] = cmcArray;
			}		
		};		
	
		cardSortingFunction(unsortedCards.mainDeckCards, mainDeckCards);
		cardSortingFunction(unsortedCards.sideBoardCards, sideBoardCards);
	
		return sortedDeck;
	}

};