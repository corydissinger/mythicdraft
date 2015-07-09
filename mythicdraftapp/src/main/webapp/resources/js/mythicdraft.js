var Upload = React.createClass({
	render: function(){
		return (
			<form action="upload" method="post" encType="multipart/form-data">
				<input type="file" name="file" required></input>
				<input type="text" name="name" maxlength="20" required></input>
				<input type="submit"></input>
			</form>	
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

React.render(<RecentDrafts url="/draft/recent"/>, document.getElementById("container"));
React.render(<Upload />, document.getElementById("uploadContainer"));