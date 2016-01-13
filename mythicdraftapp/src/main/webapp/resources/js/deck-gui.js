var DeckPanel = React.createClass({

	render: function() {
		var cmcArrays = [];
		var land = [];
	
		if(this.props.deck.cmcArrays) {
			cmcArrays = this.props.deck.cmcArrays;
		} 
	
		if(this.props.deck.land) {
			land = this.props.deck.land;
		}
	
		return (
			<div className="row">
				<CardColumn cards={land} />
				
				{cmcArrays.map(function(aCmcArray, cmc) {
					return <CardColumn cards={aCmcArray} />;
				})}
			</div>
		);
	}

});

var CardColumn = React.createClass({

	render: function() {
		var cards = [];
	
		if(this.props.cards) {
			cards = this.props.cards;
		}
	
		return (
			<div className="col-xs-3 col-md-1 deck-card-column">
				{cards.map(function(aCard) {
					return <Card multiverseId={aCard.multiverseId} extraClass={"deck-card"} />;
				})}
			</div>
		);
	}

});