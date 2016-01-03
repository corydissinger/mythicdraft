var PlayerDraft = React.createClass({
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
					<span>
						{packsString}
					</span>
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

var PlayerDrafts = React.createClass({
	contextTypes: {
		router: React.PropTypes.func
	},

	_updateState: function(props) {
		var comp = this;
		
		request
			.get("/draft/player/" + props.params.playerId)
			.end(function(err, resp) {
				comp.setState({data: resp.body});
			});	
	},

	getInitialState: function() {
		return {data: {
			drafts: [],
			player: {},
			winPercentage: ""
		}};
	},
	
	componentDidMount: function() {
		this._updateState(this.props);
	},
	
	componentWillReceiveProps: function(nextProps) {
		this._updateState(nextProps);
	},
	
	render: function() {
		var drafts = this.state.data.drafts;		
		var player = this.state.data.player;		
		var winPercentage = this.state.data.winPercentage;		
		
		return (
			<div className="container-fluid">
				<div className="row">
					<div className="col-xs-12 col-md-3">
						<h2 className="text-info text-center">
							{player.name}
						</h2>
						<div className="row well">
							<div className="col-xs-8">
								Total Drafts Uploaded
							</div>
							<div className="col-xs-4">
								{drafts.length}
							</div>
						</div>
						<div className="row well">
							<div className="col-xs-8">
								Win Percentage
							</div>
							<div className="col-xs-4">
								{winPercentage}
							</div>						
						</div>						
					</div>
					<div className="col-xs-12 col-md-9">
						<table className="table table-hover">
							<thead>
								<tr>
									<td>
										Draft Name
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
									return <PlayerDraft data={draft} />;
								})}
							</tbody>
						</table>							
					</div>
				</div>
			</div>
		);
	}
});