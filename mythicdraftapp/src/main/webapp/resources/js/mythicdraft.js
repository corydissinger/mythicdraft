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
				{drafts.map(function(draft) {
					return <RecentDraft data={draft} />;
				})}
			</table>
		);
	}
});

var RecentDraft = React.createClass({
	render: function() {
		var packsString = "";
		
		this.props.data.packs.map(function(aPack) {
			packsString += aPack.setCode + " ";
		});
	
		return (
			<tr className="recentDraft">
				<td>
					<a data-draftid={this.props.data.id} 
					   data-packid={this.props.data.packs[0].id} 
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
					{this.props.data.wins}
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
					<div className="navbar-ad navbar-brand navbar-right">
						<img src="http://placehold.it/728x90" alt="Leaderboard Ad" className="img-responsive center-block leader-banner-ad" />					
					</div>
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
		
		request.get('/draft/' + this.props.draftId + '/pack/' + this.props.packId + '/pick/' + this.props.pickNumber)
			.end(function(err, resp) {
				comp.setState({data: resp.body});
			});
	},
	
	render: function() {
		if(!this.state.data.pick) {
			return <div></div>;
		}
	
		var pick = this.state.data.pick || {};	
		var available = this.state.data.available || [];
		available.push(pick);
		var cardsRowOne = available.slice(0, 5);
		var cardsRowTwo = available.slice(5, 10);
		var cardsRowThree = available.slice(10, 15);
		
		return (
			<div className="container-fluid">
				<DraftControls draft={this} />
				<CardRow cards={cardsRowOne} />
				<CardRow cards={cardsRowTwo} />
				<CardRow cards={cardsRowThree} />
				<DraftControls draft={this} />
			</div>
		);
	}
});	

var DraftControls = React.createClass({
	updatePick: function(direction) {
		var comp = this.props.draft;
		var nextPickNumber = this.props.draft.props.pickNumber + direction;
	
		request.get('/draft/' + this.props.draft.props.draftId + '/pack/' + this.props.draft.props.packId + '/pick/' + nextPickNumber)
			.end(function(err, resp) {
				comp.setProps({pickNumber: nextPickNumber});
				comp.setState({data: resp.body});
			});	
	},

	render: function() {
		return (
			<div className="row">
				<div className="col-md-3"></div>
				<div className="col-md-6">
					<div className="row">
						<div className="col-md-4">
							<button type="button" 
									className="btn btn-warning" 
									disabled={ this.props.draft.props.pickNumber == 0 ? 'disabled' : ''} 
									onClick={this.updatePick.bind(this, -1)} >
								Previous Pick
							</button>
						</div>
						<div className="col-md-4">						
							<button type="button" 
									className="btn btn-info">
								Show Current Pick
							</button>			
						</div>							
						<div className="col-md-4">							
							<button type="button" 
									className="btn btn-success" 
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
		
		return (
			<div className="row">
				<div className="col-md-1">
			    </div>
				{cards.map(function(aCard) {
					
					return <div className="col-md-2">
						       <Card data={aCard} key={aCard.multiverseId} />
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
		return (
			<img src={"http://gatherer.wizards.com/Handlers/Image.ashx?type=card&multiverseid=" + this.props.data.multiverseId} />
		);
	}
});

function showMain() {
	React.render(<NavBar/>, navElement);
	React.render(<RecentDrafts url="/draft/recent"/>, content);
}

function showDraft(event) {
	var draftId = event.currentTarget.dataset.draftid;
	var firstPackId = event.currentTarget.dataset.packid;
	var pickNumber = event.currentTarget.dataset.pickid || 0;
	
	React.render(<Draft draftId={draftId} 
						packId={firstPackId}
						pickNumber={pickNumber} />, content);
}

showMain();