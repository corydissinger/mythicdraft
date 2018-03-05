var AllPlayerPicks = React.createClass({
	getInitialState: function() {
		return { allCards: [] , 
				 picksInOrder: [],
				 draftMetaData: {isPick: false, isPickShown:true} };
	},

	componentDidMount: function() {
		var comp = this;
		var draftId = this.props.draftId;
		
		request.get('/draft/' + draftId + '/all')
			.end(function(err, resp) {							
				comp.setState(resp.body);
			});		
	},

	render: function() {
		var pickIdsInOrder = this.state.picksInOrder;
		var cardIds = this.state.allCards;
		var packNumber = this.props.packNumber;	
		var pickNumber = this.props.pickNumber;		
		var currentPick = 0;
		
		for(var i = 0; i < packNumber; i++) {
			currentPick += this.state.draftMetaData.packs[i].packSize;
		}
		
		currentPick += pickNumber;
		
		return (
			<div className="row top-buffer no-pad well">
				{pickIdsInOrder.map(function(aCardId, index) {
					var isPick = index <= currentPick;
					
					return  <div className="col-md-1 col-xs-3">
								<Card multiverseId={aCardId} 
									  isPick={isPick}
									  isPickShown={true}
									  extraClass="card-img-small"
									  key={aCardId + index} />
							</div>;
				})}
				
				<div className="hidden">		
					{cardIds.map(function(aCardId) {
						return <img src={"http://gatherer.wizards.com/Handlers/Image.ashx?type=card&multiverseid=" + aCardId} />;
					})}
				</div>				
			</div>
		);
	}
});

var CardRow = React.createClass({
	render: function() {
		var cards = this.props.cards;		
		var pick = this.props.pick;
		var isPickShown = this.props.isPickShown;
		var counter = 0;
		
		return (
			<div className="row top-buffer no-pad">				
				{cards.map(function(aCard) {
					
					return <div className={counter++ % 5 == 0 ? "col-md-offset-1 col-md-2 col-sm-3 col-xs-4" : "col-md-2 col-sm-3 col-xs-4"}>
						       <Card multiverseId={aCard.multiverseId} 
									 key={aCard.id} 
									 isPick={aCard.id == pick ? true : false} 
									 isPickShown={isPickShown} />
							</div>;
				})}				
			</div>			
		);
	}
});

var Draft = React.createClass({
	_updateState: function(props) {
		var comp = this;
		var draftId = props.params.draftId;
		var packId = props.params.packId;
		var pickNumber = props.params.pickId;
		
		request.get('/draft/' + draftId + '/pack/' + packId + '/pick/' + pickNumber)
			.end(function(err, resp) {		
				var currentPack;
				
				for(var i = 0; i < resp.body.draftMetaData.packs.length; i++){
					if(packId == resp.body.draftMetaData.packs[i].id){
						currentPack = i;
						break;
					}
				}
			
				comp.setState({data: resp.body, 
							   packs: resp.body.draftMetaData.packs, 
							   currentPack: currentPack, 
							   pickNumber: pickNumber, 
							   isPickShown: props.isPickShown});
			});	
	},

	componentWillReceiveProps: function(nextProps) {
		nextProps.isPickShown = false;
		this._updateState(nextProps);
	},
	
	contextTypes: {
		router: React.PropTypes.func
	},
	
	getInitialState: function() {
		return {data: []};
	},
	
	componentDidMount: function() {
		this._updateState(this.props);
	},
	
	render: function() {
		if(!this.state.data.pick) {
			return <div></div>;
		} 
	
		var pick = this.state.data.pick;	
		var isPickShown = this.state.isPickShown;		
		var pickNumber = this.state.pickNumber;
		var packNumber = this.state.currentPack;
		var packs = this.state.packs;
		
		var available = this.state.data.available.slice(0, 5);
		var availableSecond = this.state.data.available.slice(5, 10);		
		var availableThird = this.state.data.available.slice(10, 15);				
		
		return (
			<div className="container-fluid">
				<DraftControls draft={this} />
				<h1>Pack {Number(packNumber) + 1} Pick {Number(pickNumber) + 1}</h1>
				
				<div className="well">
					<CardRow ref="cardRow" cards={available} pick={pick} isPickShown={isPickShown} />
					<CardRow ref="cardTwo" cards={availableSecond} pick={pick} isPickShown={isPickShown} />				
					<CardRow ref="cardThree" cards={availableThird} pick={pick} isPickShown={isPickShown} />								
				</div>
				
				<h1>All Player Picks</h1>
				<AllPlayerPicks pickNumber={pickNumber} packNumber={packNumber} draftId={this.props.params.draftId} packs={packs} />
			</div>
		);
	}
});	

var DraftControls = React.createClass({
	showPick: function() {
		this.props.draft.setState({isPickShown: true});
	},
	
	linkHandler: function(event) {
		var link = React.findDOMNode(this.refs.shareLink);
		link.focus();
		link.select();
	},
	
	render: function() {
		var currentPackSize = Number(this.props.draft.state.packs[this.props.draft.state.currentPack].packSize);
		var previousDisabled = Number(this.props.draft.state.pickNumber) == 0 && Number(this.props.draft.state.currentPack) == 0 ? 'disabled' : '';
		var nextDisabled = Number(this.props.draft.state.pickNumber) == currentPackSize - 1 && Number(this.props.draft.state.currentPack) == 2 ? 'disabled' : '';
	
		var draftId = this.props.draft.state.data.draftMetaData.id;
		var deckId = this.props.draft.state.data.draftMetaData.deckId;
		var nextPickNumber = Number(this.props.draft.state.pickNumber) + 1;
		var previousPickNumber = Number(this.props.draft.state.pickNumber) - 1;
		var packs = this.props.draft.state.packs;
		var currentPack = this.props.draft.state.currentPack;
		var nextPackId = packs[currentPack].id;
		var previousPackId = packs[currentPack].id;
			
		var linkToThis = "http://" + window.location.host + "/#/draft/" + draftId + "/pack/" + nextPackId + "/pick/" + this.props.draft.state.pickNumber;
			
		if(!nextDisabled && nextPickNumber == packs[currentPack].packSize) {
			nextPackId = packs[currentPack + 1].id;
			nextPickNumber = 0;
		} 

		if(previousPickNumber == -1 && currentPack != 0) {
			previousPackId = packs[currentPack - 1].id;
			previousPickNumber = packs[currentPack - 1].packSize - 1;
		} 	

		var viewDeckLink;
		
		if(deckId) {
			viewDeckLink = <Link className="btn btn-sm btn-primary" to={"/deck/" + deckId}><span className="glyphicon glyphicon-eye-open"></span><span className="margin-left">View Deck</span></Link>;
		}
		
		return (
			<div className="row">
				<div className="col-md-3 col-md-offset-1 col-xs-12">
					<Link className={previousDisabled ? "visibility-hidden btn btn-sm btn-warning" : "btn btn-sm btn-warning"}
						  to={"/draft/" + draftId + "/pack/" + previousPackId + "/pick/" + previousPickNumber} >
						Previous Pick
					</Link>

					<button type="button" 
							onClick={this.showPick}
							className="margin-left btn btn-sm btn-info">
						Show Pick
					</button>			

					<Link className={nextDisabled ? "margin-left visibility-hidden btn btn-sm btn-success" : "margin-left btn btn-sm btn-success"}
						  to={"/draft/" + draftId + "/pack/" + nextPackId + "/pick/" + nextPickNumber} >
						Next Pick
					</Link>										
				</div>
				
				<div className="col-md-3 col-xs-9">
					<div onClick={this.linkHandler} className="input-group input-group-sm">
						<span className="input-group-addon glyphicon glyphicon-share"></span>
						<input ref="shareLink" type="text" className="form-control" aria-describedby="basic-addon1" value={linkToThis}/>						
					</div>											
				</div>																	
				
				<div className="col-md-3 col-xs-3">
					{viewDeckLink}
				</div>
			</div>
		);
	}
});