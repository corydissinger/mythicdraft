var request = window.superagent;
var Router = ReactRouter;
var DefaultRoute = Router.DefaultRoute;
var Link = Router.Link;
var Route = Router.Route;
var RouteHandler = Router.RouteHandler;

var navElement = document.getElementById("navContainer");
var appElement = document.getElementById("container");

var nextDummyValue = 0; //shame

ReactModal.setAppElement(navElement);
ReactModal.injectCSS();

var UploadForm = React.createClass({
	getInitialState: function() {
		return {isSubmitDisabled: false,
			    isDraftDuplicate: false,
			    isDraftInvalid: false};
	},

	handleSubmit: function(e) {
		e.preventDefault();			
		var comp = this;		

		comp.setState({isSubmitDisabled: true});
		
		var draftFile = e.target[0].files[0];
			
		request.post('/upload')
			.attach('file', draftFile, draftFile.name)		
			.field('name', this.refs.draftName.getDOMNode().value)
			.field('wins', this.refs.draftWins.getDOMNode().value)
			.field('losses', this.refs.draftLosses.getDOMNode().value)
			.end(function(err, resp) {
				if(!resp.body.draftInvalid && !resp.body.draftDuplicate) {
					comp.props.navbar.closeUploadModal();
				} else {
					comp.setState({isSubmitDisabled: false,
								   isDraftDuplicate: resp.body.draftDuplicate,
								   isDraftInvalid: resp.body.draftInvalid});
				}				
			});		
	},

	render: function(){
		var hasError = this.state.isDraftInvalid || this.state.isDraftDuplicate;
		var errorText = '';
		
		if(hasError) {
			if(this.state.isDraftDuplicate) {
				errorText = 'You have already uploaded this draft.';
			} else {
				errorText = 'The file uploaded is not a valid MTGO draft log.';
			}
		}
	
		return (
			<div className="modal-content">
				<div className="modal-header">
					<button type="button" className="close" onClick={this.props.navbar.closeUploadModal}>
						<span aria-hidden="true">&times;</span>
						<span className="sr-only">Close</span>
					</button>
					<h4 className="modal-title">Upload Draft</h4>				
				</div>
				<div className="modal-body">
					<form encType="multipart/form-data" onSubmit={this.handleSubmit} ref="uploadForm">
						<div className={hasError ? "form-group has-error" : "form-group"}>
							<label htmlFor="file">Draft to Upload</label>
							<input type="file" name="file" ref="draftFile" required></input>
							<p className="help-block">This file should be the MTGO draft log.</p>
						</div>
						
						<div className="form-group">
							<label htmlFor="name">Give Your Draft a Name</label>
							<input className="form-control" type="text" name="name" ref="draftName" maxLength="20" required></input>					
						</div>

						<div className="form-group">
							<label htmlFor="wins">Rounds Won</label>
							<input className="form-control" type="number" name="wins" ref="draftWins" required></input>					
						</div>
						
						<div className="form-group">
							<label htmlFor="losses">Rounds Lost</label>
							<input className="form-control" type="number" name="losses" ref="draftLosses" required></input>					
						</div>					
											
						<button type="submit" className="btn btn-default" disabled={this.state.isSubmitDisabled}>Submit</button>
						&nbsp;&nbsp;&nbsp;
						<span className={hasError ? 'text-danger' : ''}>{errorText}</span>
					</form>					
				</div>
			</div>
		);
	}
});

var RecentDrafts = React.createClass({
	_updateState: function(props) {
		var comp = this;
		
		request
			.get("/draft/recent")
			.end(function(err, resp) {
				comp.setState({data: resp.body});
			});	
	},

	getInitialState: function() {
		return {data: []};
	},
	
	componentDidMount: function() {
		this._updateState(this.props);
	},
	
	componentWillReceiveProps: function(nextProps) {
		this._updateState(nextProps);
	},
	
	render: function() {
		var drafts = this.state.data || [];		
		
		return (
			<table className="table table-hover">
				<thead>
					<tr>
						<td>
							Draft Name
						</td>
						<td>
							Active Drafter
						</td>
						<td>
							Format
						</td>
						<td>
							Wins
						</td>
						<td>
							Losses
						</td>						
					</tr>				
				</thead>
				<tbody>
					{drafts.map(function(draft) {
						return <RecentDraft data={draft} />;
					})}
				</tbody>
			</table>
		);
	}
});

var RecentDraft = React.createClass({
	render: function() {
		var packsString = "";
		var packsJson = JSON.stringify(this.props.data.packs);
		
		this.props.data.packs.map(function(aPack) {
			packsString += aPack.setCode + " ";
		});
	
		return (
			<tr>
				<td>
					<Link to={"/draft/" + this.props.data.id + "/pack/" + this.props.data.packs[0].id + "/pick/0"}>
						{this.props.data.name}
					</Link>					
				</td>
				<td>
					<a href={"#/player" + this.props.data.activePlayer.id}>
						{this.props.data.activePlayer.name}
					</a>
				</td>				
				<td>
					<span>
						{packsString}
					</span>
					<a href={'#/draft/' + this.props.data.eventId}>
					</a>
				</td>
				<td>
					{this.props.data.wins}
				</td>
				<td>
					{this.props.data.losses}
				</td>				
			</tr>
		);
	}
});

var NavBar = React.createClass({
	getInitialState: function() {
		return { uploadModalIsOpen: false };
	},

	openUploadModal: function() {
		this.setState({ uploadModalIsOpen: true });
	},

	closeUploadModal: function() {
		this.setState({ uploadModalIsOpen: false });
		this.props.app.forceUpdate(nextDummyValue++);
	},
	
	render: function() {
		return (
			<nav className="navbar navbar-default">
				<div className="container-fluid">
					<div className="navbar-header">
						<a className="navbar-brand" href="#">Mythic Draft</a>
					</div>
					<div className="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
						<ul className="nav navbar-nav">
							<li onClick={this.openUploadModal}>
								<a href="#">
									<span className="glyphicon glyphicon-upload"></span>&nbsp;Upload
								</a>
							</li>
							<ReactModal className="Modal__Bootstrap modal-dialog" 
										isOpen={this.state.uploadModalIsOpen}
										onRequestClose={this.closeUploadModal}>
								<UploadForm navbar={this} />
						    </ReactModal>								   
						</ul>
						<ul className="nav navbar-nav navbar-right">
							<img className="navbar-ad" src="http://placehold.it/728x90" alt="Leaderboard Ad" />											
						</ul>
					</div>									
				</div>								
			</nav>
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
		var available = this.state.data.available;
		var cardsRowOne = available.slice(0, 5);
		var cardsRowTwo = available.slice(5, 10);
		var cardsRowThree = available.slice(10, 15);
		
		return (
			<div className="container-fluid">
				<DraftControls draft={this} />
				<CardRow ref="cardRow" cards={cardsRowOne} pick={pick} isPickShown={isPickShown} />
				<CardRow ref="cardRow" cards={cardsRowTwo} pick={pick} isPickShown={isPickShown} />
				<CardRow ref="cardRow" cards={cardsRowThree} pick={pick} isPickShown={isPickShown} />
				<DraftControls draft={this} />
			</div>
		);
	}
});	

var DraftControls = React.createClass({
	showPick: function() {
		this.props.draft.setState({isPickShown: true});
	},
	
	render: function() {
		var currentPackSize = Number(this.props.draft.state.packs[this.props.draft.state.currentPack].packSize);
		var previousDisabled = Number(this.props.draft.state.pickNumber) == 0 && Number(this.props.draft.state.currentPack) == 0 ? 'disabled' : '';
		var nextDisabled = Number(this.props.draft.state.pickNumber) == currentPackSize - 1 && Number(this.props.draft.state.currentPack) == 2 ? 'disabled' : '';
	
		var draftId = this.props.draft.state.data.draftMetaData.id;
		var nextPickNumber = Number(this.props.draft.state.pickNumber) + 1;
		var previousPickNumber = Number(this.props.draft.state.pickNumber) - 1;
		var packs = this.props.draft.state.packs;
		var currentPack = this.props.draft.state.currentPack;
		var nextPackId = packs[currentPack].id;
		var previousPackId = packs[currentPack].id;
			
		if(!nextDisabled && nextPickNumber == packs[currentPack].packSize) {
			nextPackId = packs[currentPack + 1].id;
			nextPickNumber = 0;
		} 

		if(previousPickNumber == -1 && currentPack != 0) {
			previousPackId = packs[currentPack - 1].id;
			previousPickNumber = packs[currentPack - 1].packSize - 1;
		} 	
	
		return (
			<div className="row">
				<div className="col-md-3"></div>
				<div className="col-md-6">
					<div className="row">
						<div className="col-md-4">
							<Link className={previousDisabled ? "hide" : "btn btn-warning"}
								  to={"/draft/" + draftId + "/pack/" + previousPackId + "/pick/" + previousPickNumber} >
								Previous Pick
							</Link>
						</div>
						<div className="col-md-4">						
							<button type="button" 
									onClick={this.showPick}
									className="btn btn-info">
								Show Current Pick
							</button>			
						</div>							
						<div className="col-md-4">							
							<Link className={nextDisabled ? "hide" : "btn btn-success"}
								  to={"/draft/" + draftId + "/pack/" + nextPackId + "/pick/" + nextPickNumber} >
								Next Pick
							</Link>										
						</div>
					</div>
				</div>
				<div className="col-md-3"></div>
			</div>
		);
	}
});

var CardRow = React.createClass({
	render: function() {
		var cards = this.props.cards;		
		var pick = this.props.pick;
		var isPickShown = this.props.isPickShown;
		
		return (
			<div className="row">
				<div className="col-md-1">
			    </div>
				{cards.map(function(aCard) {
					
					return <div className="col-md-2">
						       <Card data={aCard} 
									 key={aCard.id} 
									 isPick={aCard.id == pick ? true : false} 
									 isPickShown={isPickShown} />
						   </div>;
				})}				
				<div className="col-md-1">
			    </div>				
			</div>			
		);
	}
});

var Card = React.createClass({
	render: function() {
		var classString;
	
		if(this.props.isPickShown) {
			if(this.props.isPick) {
				classString = '';
			} else {
				classString = 'card-not-picked-animation';
			}
		} else {
			classString = '';
		}
	
		return (
			<img className={classString}
				 src={"http://gatherer.wizards.com/Handlers/Image.ashx?type=card&multiverseid=" + this.props.data.multiverseId} />
		);
	}
});

var App = React.createClass({
	forceUpdate: function(dummyValue) {
		this.setState({update: dummyValue});
	},
	
	render: function() {
		return (
			<div>
				<NavBar app={this}/>
				<RouteHandler />
			</div>
		);
	}
});

var routes = (
  <Route name="app" path="/" handler={App}>
    <Route name="draft" path="draft/:draftId/" handler={Draft} >
		<Route name="pack" path="pack/:packId/" handler={Draft} >
			<Route name="pick" path="pick/:pickId" handler={Draft} >
			</Route>
		</Route>
	</Route>
	
    <DefaultRoute handler={RecentDrafts}/>
  </Route>
);

Router.run(routes, function (Handler) {
  React.render(<Handler/>, appElement);
});