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
			<div className="row well">
				<CardColumn labelName={"Lands"} labelClass={"default"} cards={land} />
				
				{cmcArrays.map(function(aCmcArray, aLabelName) {
					return <CardColumn labelName={aLabelName + " drops"} labelClass={"info"} cards={aCmcArray} />;
				})}
			</div>
		);
	}

});

var CardColumn = React.createClass({

	render: function() {
		var cards = [];
		var labelName = "";
		var labelClass = "";
	
		if(this.props.cards) {
			cards = this.props.cards;
		}
		
		if(this.props.labelName) {
			labelName = this.props.labelName;
		}		
		
		if(this.props.labelClass) {
			labelClass = this.props.labelClass;
		}				
	
		return (
			<div className="col-xs-3 col-md-1">
				<h2> 
					<span className={"label label-" + labelClass}>{labelName}</span>
				</h2>
				{cards.map(function(aCard) {
					return <p>{aCard.name}</p>;
				})}
			</div>
		);
	}

});