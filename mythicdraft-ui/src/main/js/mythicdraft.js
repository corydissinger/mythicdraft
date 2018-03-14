var request = window.superagent;
var Router = ReactRouter;
var DefaultRoute = Router.DefaultRoute;
var Link = Router.Link;
var Route = Router.Route;
var RouteHandler = Router.RouteHandler;

var navElement = document.getElementById("nav-container");
var appElement = document.getElementById("app-container");

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
		var deckFile = e.target[1].files[0];
			
		var req = request.post('/upload')
						 .attach('file', draftFile, draftFile.name)					
						 .field('name', this.refs.draftName.getDOMNode().value)
						 .field('wins', this.refs.draftWins.getDOMNode().value)
						 .field('losses', this.refs.draftLosses.getDOMNode().value)
			
		if(deckFile) {
			req.attach('deck', deckFile, deckFile.name);
		}
			
		req.end(function(err, resp) {
			if(!resp.body.draftInvalid && !resp.body.draftDuplicate) {
				comp.props.navbar.closeUploadModal();
			} else {
				comp.setState({isSubmitDisabled: false,
							   isDraftDuplicate: resp.body.draftDuplicate,
							   isDraftInvalid: resp.body.draftInvalid,
							   isDeckInvalid: resp.body.deckInvalid});
			}				
		});		
	},

	render: function(){
		var hasError = this.state.isDraftInvalid || this.state.isDraftDuplicate || this.state.isDeckInvalid;
		var errorText = '';
		
		if(hasError) {
			if(this.state.isDraftDuplicate) {
				errorText = 'You have already uploaded this draft.';
			} else if(this.state.isDraftInvalid) {
				errorText = 'The file uploaded is not a valid MTGO draft log.';
			} else {
				errorText = 'The deck file uploaded contains cards not drafted, or is not a valid MTGO deck file in text form.';
			}
		}
	
		return (
			<div className="modal-content">
				<div className="modal-header">
					<button type="button" className="close" onClick={this.props.navbar.closeUploadModal}>
						<span aria-hidden="true" className="glyphicon glyphicon-remove"></span>
						<span className="sr-only margin-left">Close</span>
					</button>
					<h4 className="modal-title">Upload Draft</h4>				
				</div>
				<div className="modal-body">
					<form encType="multipart/form-data" onSubmit={this.handleSubmit} ref="uploadForm">
						<div className="form-group">
							<label htmlFor="file">Draft to Upload</label>
							<input type="file" name="file" ref="draftFile" required></input>
							<p className="help-block">This file should be the MTGO draft log.</p>
						</div>

						<div className="form-group">
							<label htmlFor="deck">Deck to Upload (Optional)</label>
							<input type="file" name="deck" ref="deckFile"></input>
							<p className="help-block">This file should be the deck exported in <span className="text-warning">.TXT</span> from the MTGO freeform log.</p>
						</div>
						
						<div className="form-group">
							<label htmlFor="name">Give Your Draft a Name</label>
							<input className="form-control" type="text" name="name" ref="draftName" maxLength="20" required></input>					
						</div>

						<div className="form-group">
							<label htmlFor="wins">Rounds Won</label>
							<input className="form-control" type="number" min="0" max="3" name="wins" ref="draftWins" required></input>
						</div>
						
						<div className="form-group">
							<label htmlFor="losses">Rounds Lost</label>
							<input className="form-control" type="number" min="0" max="3" name="losses" ref="draftLosses" required></input>
						</div>					
											
						<button type="submit" className="btn btn-default" disabled={this.state.isSubmitDisabled}>Submit</button>					
						<span className={hasError ? 'text-danger margin-left' : ''}>{errorText}</span>
					</form>					
				</div>
			</div>
		);
	}
});

var UploadDeckForm = React.createClass({
	getInitialState: function() {
		return {isSubmitDisabled: false,
			    isDraftDuplicate: false,
			    isDraftInvalid: false};
	},

	handleSubmit: function(e) {
		e.preventDefault();			
		var comp = this;		

		comp.setState({isSubmitDisabled: true});
		
		var deckFile = e.target[0].files[0];
			
		request.post('/upload/deck')
			.attach('deck', deckFile, deckFile.name)
			.field('draftId', this.props.draftId)
			.end(function(err, resp) {
				if(!resp.body.draftInvalid && !resp.body.draftDuplicate) {
					comp.props.draftTable.closeUploadModal();
					window.location.href = "#/deck/" + resp.body.deckId;
				} else {
					comp.setState({isSubmitDisabled: false,
								   isDraftDuplicate: resp.body.draftDuplicate,
								   isDraftInvalid: resp.body.draftInvalid,
								   isDeckInvalid: resp.body.deckInvalid});
				}				
			});		
	},

	render: function(){
		var hasError = this.state.isDraftInvalid || this.state.isDraftDuplicate || this.state.isDeckInvalid;
		var draftName = this.props.draftName;
		var errorText = '';
		
		if(hasError) {
			if(this.state.isDraftDuplicate) {
				errorText = 'You have already uploaded this draft.';
			} else  if(this.state.isDraftInvalid) {
				errorText = 'The file uploaded is not a valid MTGO draft log.';
			} else {
				errorText = 'The deck file uploaded contains cards not drafted, or is not a valid MTGO deck file in text form.';
			}
		}
	
		return (
			<div className="modal-content">
				<div className="modal-header">
					<button type="button" className="close" onClick={this.props.draftTable.closeUploadModal}>
						<span aria-hidden="true" className="glyphicon glyphicon-remove"></span>
						<span className="sr-only margin-left">Close</span>
					</button>
					<h4 className="modal-title">Upload Deck for Draft {draftName}</h4>				
				</div>
				<div className="modal-body">
					<form encType="multipart/form-data" onSubmit={this.handleSubmit} ref="uploadDeckForm">

						<div className={hasError ? "form-group has-error" : "form-group"}>
							<label htmlFor="deck">Deck to Upload</label>
							<input type="file" name="deck" ref="deckFile" required></input>
							<p className="help-block">This file should be the deck exported in <span className="text-warning">.TXT</span> from the MTGO freeform log.</p>
						</div>
											
						<button type="submit" className="btn btn-default" disabled={this.state.isSubmitDisabled}>Submit</button>					
						<span className={hasError ? 'text-danger margin-left' : ''}>{errorText}</span>
					</form>					
				</div>
			</div>
		);
	}
});

var RecentDrafts = React.createClass({
	contextTypes: {
		router: React.PropTypes.func
	},

	getDefaultProps: function() {
		return { params: { pageNumber: 0} };
	},	
	
	_updateState: function(props) {
		var comp = this;					
		var pageNumber = props.params.pageNumber ? props.params.pageNumber : 0;
		
		request
			.get("/draft/recent/page/" + pageNumber)
			.end(function(err, resp) {
				comp.setState(resp.body);
			});	
	},

	getInitialState: function() {
		return { recentDrafts: [], totalPages: 0, pageNumber: 0};
	},
	
	componentDidMount: function() {
		this._updateState(this.props);
	},
	
	componentWillReceiveProps: function(nextProps) {
		this._updateState(nextProps);
	},
	
	render: function() {
		var drafts = this.state.recentDrafts;		
		var totalPages = this.state.totalPages;
		
		return (
			<div className="container-fluid">
				<div className="row">
					<div className="col-xs-12">
						<table className="table table-hover">
							<thead>
								<tr>
									<td>
										Draft Name
									</td>
									<td>
										Active Player
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
									<td>
										Deck
									</td>
								</tr>				
							</thead>
							<tbody>
								{drafts.map(function(draft) {
									return <RecentDraft data={draft} />;
								})}
							</tbody>
						</table>
					</div>						
				</div>
				<div className="row">
					<div className="col-xs-12">				
						<RecentDraftsFooter recentDrafts={this} totalPages={totalPages} pageNumber={this.props.params.pageNumber} />
					</div>
				</div>
			</div>			
		);		
	}
});

var RecentDraftsFooter = React.createClass({
	handlePagination: function(pageNumber) {
		this.props.recentDrafts.setState({pageNumber: pageNumber});		
	},
	
	getDefaultProps: function() {
		return { pageNumber: 0, totalPages: 0 };
	},
	
	_updateState: function(props) {
		var comp = this;				
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
		var pages = [];
		var pageNumber = this.props.pageNumber;
		var offsetPage;
	
		if(pageNumber < 5) {
			offsetPage = 0;
		} else if (pageNumber < this.props.totalPages - 5) {
			offsetPage = pageNumber - 5;
		} else {
			offsetPage = this.props.totalPages - 10;
		}
	
		for(var i = offsetPage; i < offsetPage + 10; i++) {
			pages.push(i);
		}
	
		var previousPageButtonClass = pageNumber == 0 ? "disabled" : "";		
		var nextPageButtonClass = pageNumber == this.props.totalPages - 1 ? "disabled" : "";
		var firstPageButtonClass = previousPageButtonClass;
		var lastPageButtonClass = nextPageButtonClass;
		
		var previousLinkRoute = pageNumber != 0 ? "/draft/recent/" + Number(Number(pageNumber) - 1) : "/draft/recent/" + pageNumber;
		var nextLinkRoute = pageNumber != this.props.totalPages - 1 ? "/draft/recent/" + (Number(pageNumber) + 1) : "/draft/recent/" + pageNumber;
	
		return (
			<nav className="text-center">
				<ul className="pagination">
				    <li className={firstPageButtonClass}>
						<Link to="/draft/recent/0" aria-label="First">
							<span aria-hidden="true">{String.fromCharCode(8656)}</span>
						</Link>
					</li>				
					
				    <li className={previousPageButtonClass}>
						<Link to={previousLinkRoute} aria-label="Previous">
							<span aria-hidden="true">{String.fromCharCode(8592)}</span>
						</Link>
					</li>
					
					{pages.map(function(aPageNumber, index) {						
						return <li className={aPageNumber == pageNumber ? "active" : ""}><Link to={"/draft/recent/" + aPageNumber}> {aPageNumber + 1} </Link></li>;
					})}
					
					<li className={nextPageButtonClass} onClick={this.handleNext}>
						<Link to={nextLinkRoute} aria-label="Next">
							<span aria-hidden="true">{String.fromCharCode(8594)}</span>
						</Link>
					</li>					
					
				    <li className={lastPageButtonClass}>
						<Link to={"/draft/recent/" + Number(this.props.totalPages - 1)} aria-label="Last">
							<span aria-hidden="true">{String.fromCharCode(8658)}</span>
						</Link>
					</li>									
				</ul>
			</nav>
		);
	}
});

var RecentDraft = React.createClass({
	getInitialState: function() {
		return { uploadModalIsOpen: false };
	},

	openUploadModal: function() {
		this.setState({ uploadModalIsOpen: true });
	},

	closeUploadModal: function() {
		this.setState({ uploadModalIsOpen: false });	
	},

	render: function() {
		var packsString = "";
		var packsJson = JSON.stringify(this.props.data.packs);
		
		this.props.data.packs.map(function(aPack) {
			packsString += aPack.setCode + " ";
		});
	
		var uploadCell;
		
		if(this.props.data.deckId) {
			uploadCell = <Link className="btn btn-xs btn-primary" to={"/deck/" + this.props.data.deckId}><span className="glyphicon glyphicon-eye-open"></span><span className="margin-left">View Deck</span></Link>;
		} else {
			uploadCell = <UploadDeckButton data={this.props.data} draftTable={this} />;
		}
	
		return (
			<tr>
				<td>
					<Link to={"/draft/" + this.props.data.id + "/pack/" + this.props.data.packs[0].id + "/pick/0"}>
						{this.props.data.name}
					</Link>					
				</td>
				<td>
					<Link to={"/draft/player/" + this.props.data.activePlayer.id}>
						{this.props.data.activePlayer.name}
					</Link>					
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
				<td>
					{uploadCell}
				</td>
			</tr>
		);
	}
});

var UploadDeckButton = React.createClass({

	render: function() {
		return (
			<span>
				<a className="btn btn-xs btn-warning" href="#" role="button" onClick={this.props.draftTable.openUploadModal} >
					<span className="glyphicon glyphicon-upload"></span><span className="margin-left">Upload Deck</span>
				</a>
				<ReactModal className="Modal__Bootstrap modal-dialog" 
							isOpen={this.props.draftTable.state.uploadModalIsOpen}
							onRequestClose={this.props.draftTable.closeUploadModal}>
					<UploadDeckForm draftTable={this.props.draftTable} draftId={this.props.data.id} draftName={this.props.data.name}/>
				</ReactModal>		
			</span>
		);
	}

})

var Deck = React.createClass({
	contextTypes: {
		router: React.PropTypes.func
	},

	_updateState: function(props) {
		var comp = this;
		
		request
			.get("/draft/" + props.params.deckId + "/deck")
			.end(function(err, resp) {
				var state = {data: CardDeckOrganizer.sortCards(resp.body) };
				
				state.data.draftMetaData = resp.body.draftMetaData;
				
				comp.setState(state);
			});	
	},

	getInitialState: function() {
		return { data: {
				mainDeckCards: { land: [], cmcArrays: [ [] ] },
				sideBoardCards: { land: [], cmcArrays: [ [] ] },
				draftMetaData: { packs: [{ id: 0 }], id: 0 }
			}
		};
	},
	
	componentDidMount: function() {
		this._updateState(this.props);
	},
	
	componentWillReceiveProps: function(nextProps) {
		this._updateState(nextProps);
	},

	render: function() {
		var draftId = this.state.data.draftMetaData.id;
		var firstPackId = this.state.data.draftMetaData.packs[0].id;
	
		return (
			<div className="container-fluid">		
				<div className="row">
					<div className="col-xs-12">
						<Link className="btn btn-sm btn-primary" to={"/draft/" + draftId + "/pack/" + firstPackId + "/pick/0"}><span className="glyphicon glyphicon-eye-open"></span><span className="margin-left">View Draft</span></Link>
					</div>
				</div>						
			
				<div className="row">
					<div className="col-xs-12">
						<h1>Main Deck</h1>						
					</div>
				</div>
				<DeckPanel deck={this.state.data.mainDeckCards} />
				
				<div className="row">
					<div className="col-xs-12">
						<h1>Sideboard</h1>
					</div>
				</div>				
				<DeckPanel deck={this.state.data.sideBoardCards} />
			</div>
		);
	}

});

var PlayerSearch = React.createClass({
	getInitialState: function() {
		return { players: [], searchDisabled: false };
	},
	
	handleChange: function(event) {
		var searchString = event.target.value;
	
		//If search is disabled it's because we're waiting on an ajax response
		if(this.state.searchDisabled) {
			return;
		}else if(!searchString || searchString.length < 3) {
			this.setState({ players: [] });
			return;
		} else {
			this.setState({ searchDisabled: true });
		}
	
		var comp = this;
	
		request.get('/player/search')
			.query({ name: searchString })
			.end(function(err, resp){
				comp.setState({ players: resp.body, searchDisabled: false });
			});
	},
	
	handleKeyPress: function(event) {				
		if(event.keyCode == 13) {
			var playerId = 0;
			var selectedPlayerName = React.findDOMNode(this.refs.playerInput).value;
			var playersList = React.findDOMNode(this.refs.playersList).children;
			
			for(var i = 0; i < playersList.length; i++){
				var playerNode = playersList[i];
				
				if(playerNode.value == selectedPlayerName){
					playerId = playerNode.dataset.playerId;
					break;
				}
			}
			
			window.location.hash = "#/draft/player/" + playerId;
			event.preventDefault();			
		}		
	},
	
	render: function() {
		var players = this.state.players;
	
		return (
			<form className="navbar-form navbar-left" role="playersSearch" name="players">
				<div className="form-group">
					<input type="text" 
						   className="form-control" 
						   placeholder="Player Search" 
						   onInput={this.handleChange} 
						   onKeyDown={this.handleKeyPress}
						   ref="playerInput"
						   list="players" />
					<datalist onChange={this.handlePlayerSelect} id="players" ref="playersList">
						{players.map(function(aPlayer, idx) {
							return <option key={aPlayer.id} 
										   data-player-id={aPlayer.id}
										   value={aPlayer.name}>
								   </option>;
						}, this)}
					</datalist>
				</div>
			</form>
		);
	}
});

var FormatSearch = React.createClass({
	getInitialState: function() {
		return { data: {formats: []}, searchDisabled: false };
	},
	
	contextTypes: {
		router: React.PropTypes.func
	},

	_updateState: function(props) {
		var comp = this;
		
		request
			.get("/formats/")
			.end(function(err, resp) {
				var state = {data: {formats: resp.body} };				
				
				comp.setState(state);
			});	
	},
	
	componentDidMount: function() {
		this._updateState(this.props);
	},
	
	componentWillReceiveProps: function(nextProps) {
		this._updateState(nextProps);
	},			
	
	handleKeyPress: function(event) {				
		if(event.keyCode == 13) {
			var formatId = 0;
			var selectedFormatName = React.findDOMNode(this.refs.formatInput).value;
			var formatsList = React.findDOMNode(this.refs.formatsList).children;
			
			for(var i = 0; i < formatsList.length; i++){
				var formatNode = formatsList[i];
				
				if(formatNode.value == selectedFormatName){
					formatId = formatNode.dataset.formatId;
					break;
				}
			}
			
			window.location.hash = "#/format/" + formatId + "/stats";
			event.preventDefault();			
		}		
	},	
	
	render: function() {
		var formats = this.state.data.formats;
	
		return (
			<form className="navbar-form navbar-left" role="formatsSearch" name="formats">
				<div className="form-group">
					<input type="text" 
						   className="form-control" 
						   placeholder="Format Stats Search" 
						   onKeyDown={this.handleKeyPress}
						   ref="formatInput"
						   list="formats" />
					<datalist onChange={this.handleFormatSelect} id="formats" ref="formatsList">
						{formats.map(function(aFormat, idx) {
							var fullFormatName = new String(aFormat.sets[0].trim() + " " + aFormat.sets[1].trim() + " " + aFormat.sets[2].trim()).trim();
						
							return <option key={aFormat.id} 
										   data-format-id={aFormat.id}
										   value={fullFormatName}>
								   </option>;
						}, this)}
					</datalist>
				</div>				
			</form>
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
	
	componentDidMount: function() {
		bindNavbarToggle();
	},
	
	render: function() {
		var adProps = {display: "inline-block", width:"728px", height:"90px"};
		
		return (
			<nav className="navbar navbar-default">
				<div className="container-fluid">
					<div className="navbar-header">
						<a className="navbar-brand" href="#">Mythic Draft</a>
						<button type="button" 
								className="navbar-toggle collapsed" 
								data-toggle="collapse" 
								data-target="#collapsable-navbar" 
								aria-expanded="false">
							<span className="sr-only">Toggle navigation</span>
							<span className="icon-bar"></span>
							<span className="icon-bar"></span>
							<span className="icon-bar"></span>								
						</button>
					</div>
					<div className="collapse navbar-collapse" id="collapsable-navbar">
						<ul className="nav navbar-nav">
							<li onClick={this.openUploadModal} className="visible-md-block visible-lg-block">
								<a href="#">
									<span className="glyphicon glyphicon-upload"></span><span className="margin-left">Upload</span>
								</a>
							</li>
							<ReactModal className="Modal__Bootstrap modal-dialog" 
										isOpen={this.state.uploadModalIsOpen}
										onRequestClose={this.closeUploadModal}>
								<UploadForm navbar={this} />
						    </ReactModal>
						</ul>
						
						<PlayerSearch />
						
						<FormatSearch />						
						
						<ul className="nav navbar-nav navbar-right visible-md-block visible-lg-block">
							<script async src="//pagead2.googlesyndication.com/pagead/js/adsbygoogle.js"></script>
							<ins className="adsbygoogle"
								 style={adProps}
								 data-ad-client="ca-pub-2254619448324918"
								 data-ad-slot="1012777982"></ins>
							<script>
							(adsbygoogle = window.adsbygoogle || []).push({});
							</script>			
						</ul>
					</div>									
				</div>								
			</nav>
		);
	}
});

var Card = React.createClass({
	render: function() {
		var classString = 'img-responsive card-img';				
	
		if(this.props.isPickShown) {
			if(!this.props.isPick) {
				classString = 'img-responsive card-img card-not-picked-animation';
			}
		} 
	
		if(this.props.extraClass) {
			classString += ' ' + this.props.extraClass;
		}
	
		return (
			<img className={classString}
				 src={"http://gatherer.wizards.com/Handlers/Image.ashx?type=card&multiverseid=" + this.props.multiverseId} />
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
				<Footer />
			</div>
		);
	}
});

var Footer = React.createClass({	
	render: function() {
		return (
			<div className="container-fluid">
				<div className="row">
					<div className="col-xs-12">
						<footer>
							<p>Magic the Gathering, FNM is TM and copyright Wizards of the Coast, Inc, a subsidiary of Hasbro, Inc. All rights reserved. This site is unaffiliated. </p>

							<p>This site {String.fromCharCode(169)} 2016 MythicDraft.com </p>
						</footer>
					</div>					
				</div>
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
	
	<Route name="player" path="draft/player/:playerId" handler={PlayerDrafts} />
	
	<Route name="stats" path="format/:formatId/stats" handler={FormatStats} />	

	<Route name="deck" path="deck/:deckId" handler={Deck} />
	
	<Route name="recent" path="draft/recent/:pageNumber" handler={RecentDrafts} />	
	
    <DefaultRoute handler={RecentDrafts}/>
  </Route>
);

Router.run(routes, function (Handler) {
  React.render(<Handler/>, appElement);
});