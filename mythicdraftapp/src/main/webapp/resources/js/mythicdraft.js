var request = window.superagent;
var navElement = document.getElementById("navContainer");

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
					<button type="button" className="close" onClick={this.handleModalCloseRequest}>
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
			<div className="recentDrafts">
				{drafts.map(function(draft) {
					return <RecentDraft data={draft} />;
				})}
			</div>
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
			<div className="recentDraft">
				<h2 className="recentDraftName">
					{this.props.data.name}
				</h2>
				<p>
					<span>
						{packsString}
					</span>
					<a href={'#/draft/' + this.props.data.eventId}>
						View Draft
					</a>
				</p>
			</div>
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
					<div className="navbar-brand navbar-right">
						<img src="http://placehold.it/728x90" alt="Leaderboard Ad" class="img-responsive center-block" />					
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
								<UploadForm/>
						    </ReactModal>								   
						</ul>
					</div>									
				</div>
			</nav>
		);
	}
});

React.render(<NavBar/>, navElement);
React.render(<RecentDrafts url="/draft/recent"/>, document.getElementById("container"));