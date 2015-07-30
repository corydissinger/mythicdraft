var request = window.superagent;
var navElement = document.getElementById("navContainer");
var content = document.getElementById("container");

ReactModal.setAppElement(navElement);
ReactModal.injectCSS();

var UploadForm = React.createClass({
	handleSubmit: function(e) {
		e.preventDefault();
		
		var comp = this;
		var draftFile = e.target[0].files[0];
		
		console.log(this.refs.uploadForm.getDOMNode());
		console.log(draftFile.value);
		console.log(e);
		
		request.post('/upload')
			.attach('file', draftFile, draftFile.name)		
			.field('name', this.refs.draftName.getDOMNode().value)
			.field('wins', this.refs.draftWins.getDOMNode().value)
			.field('losses', this.refs.draftLosses.getDOMNode().value)
			.end(function(err, resp) {
				comp.setState({data: resp.body});
			});		
	},

	render: function(){
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
						<div className="form-group">
							<label htmlFor="file">Draft to Upload</label>
							<input type="file" name="file" ref="draftFile" required></input>
							<p className="help-block">This file should be the MTGO draft log</p>
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
						
						<button type="submit" className="btn btn-default">Submit</button>
					</form>					
				</div>
			</div>
		);
	}
});

var RecentDrafts = React.createClass({
	getInitialState: function() {
		return {data: []};
	},
	
	componentDidMount: function() {
		var comp = this;
		
		request
			.get(comp.props.url)
			.end(function(err, resp) {
				comp.setState({data: resp.body});
			});
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
					<a data-draftid={this.props.data.id} 
					   data-packs={packsJson} 
					   href={"#/draft/" + this.props.data.id + "/pack/" + this.props.data.packs[0].id} 
					   onClick={showDraft}>
						{this.props.data.name}
					</a>					
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
						View Draft
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
	getInitialState: function() {
		return {data: []};
	},
	
	componentDidMount: function() {
		var comp = this;
		var pickNumber = this.props.pickNumber;
		var packId = this.props.packs[this.props.currentPack].id;
		
		request.get('/draft/' + this.props.draftId + '/pack/' + packId + '/pick/' + pickNumber)
			.end(function(err, resp) {
				if(pickNumber == 0) {
					var packs = comp.props.packs;
					packs[comp.props.currentPack].packSize = resp.body.available.length;
					
					comp.setProps({packs: packs});
				}
				
				comp.setState({data: resp.body});
			});
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
	updatePick: function(direction) {
		var comp = this.props.draft;
		var nextPickNumber = this.props.draft.props.pickNumber + direction;;
		var packs = this.props.draft.props.packs;
		var currentPack = this.props.draft.props.currentPack;
			
		if(nextPickNumber == packs[currentPack].packSize) {
			currentPack++;
			nextPickNumber = 0;
		} else if(nextPickNumber == -1) {
			currentPack--;
			nextPickNumber = packs[currentPack].packSize - 1;
		} 
	
		var packId = packs[currentPack].id;
	
		request.get('/draft/' + this.props.draft.props.draftId + '/pack/' + packId + '/pick/' + nextPickNumber)
			.end(function(err, resp) {
				if(nextPickNumber == 0) {
					packs[currentPack].packSize = resp.body.available.length;					
				}
				
				comp.setProps({pickNumber: nextPickNumber, currentPack: currentPack, packs: packs});
				comp.setState({data: resp.body, isPickShown: false});
			});	
	},

	showPick: function() {
		this.props.draft.setState({isPickShown: true});
	},
	
	render: function() {
		var currentPackSize = this.props.draft.props.packs[this.props.draft.props.currentPack].packSize;
	
		return (
			<div className="row">
				<div className="col-md-3"></div>
				<div className="col-md-6">
					<div className="row">
						<div className="col-md-4">
							<button type="button" 
									className="btn btn-warning" 
									disabled={ this.props.draft.props.pickNumber == 0 && this.props.draft.props.currentPack == 0 ? 'disabled' : ''} 
									onClick={this.updatePick.bind(this, -1)} >
								Previous Pick
							</button>
						</div>
						<div className="col-md-4">						
							<button type="button" 
									onClick={this.showPick}
									className="btn btn-info">
								Show Current Pick
							</button>			
						</div>							
						<div className="col-md-4">							
							<button type="button" 
									className="btn btn-success" 
									disabled={ this.props.draft.props.pickNumber == currentPackSize - 1 && this.props.draft.props.currentPack == 2 ? 'disabled' : ''} 
									onClick={this.updatePick.bind(this, 1)} >
								Next Pick
							</button>										
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

function showMain() {
	React.render(<NavBar/>, navElement);
	React.render(<RecentDrafts url="/draft/recent"/>, content);
}

function showDraft(event) {
	var draftId = event.currentTarget.dataset.draftid;
	var packs = JSON.parse(event.currentTarget.dataset.packs);
	var pickNumber = event.currentTarget.dataset.pickid || 0;
	
	React.render(<Draft draftId={draftId} 
						packs={packs}
						currentPack={0}
						pickNumber={pickNumber} />, content);
}

showMain();