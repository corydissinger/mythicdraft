var FormatPickStats = React.createClass({
	getInitialState: function() {
		return { pickStats: {} };
	},

	render: function() {	
		return (
			<tr>
				<td>
					{this.props.pickStats.card.name}
				</td>
				<td>
					{this.props.pickStats.avgPick + 1}
				</td>		
			</tr>
		);
	}
});

var FormatStats = React.createClass({
	contextTypes: {
		router: React.PropTypes.func
	},

	_updateState: function(props) {
		var comp = this;
		
		request
			.get("/format/" + props.params.formatId + "/stats")
			.end(function(err, resp) {
				comp.setState({data: resp.body});
			});	
	},

	getInitialState: function() {
		return {data: {
			formatPickStats: [],
			format: { sets: ["", "", ""]}			
		} };
	},
	
	componentDidMount: function() {
		this._updateState(this.props);
	},
	
	componentWillReceiveProps: function(nextProps) {
		this._updateState(nextProps);
	},
	
	render: function() {
		var formatPickStats = this.state.data.formatPickStats;		
		var format = this.state.data.format;
		var sampleSize = this.state.data.sampleSize;
		
		var theFormatName = format.sets[0].trim() + " " + format.sets[1].trim() + " " + format.sets[2].trim();		
		
		return (
			<div className="container-fluid">
				<div className="row">
					<div className="col-xs-12 col-md-3 well">
						<h2 className="text-info text-center">
							{theFormatName}
						</h2>
						<h4 className="text-right">
						    Number of Uploaded Drafts: {sampleSize}
						</h4>
					</div>
					<div className="col-xs-12 col-md-9">
						<table className="table table-hover">
							<thead>
								<tr>
									<td>
										<h2>Card</h2>
									</td>
									<td>
										<h2>Average Pick</h2>
									</td>
								</tr>				
							</thead>
							<tbody>
								{formatPickStats.map(function(aPickStats) {
									return <FormatPickStats pickStats={aPickStats} />;
								})}
							</tbody>
						</table>							
					</div>
				</div>
			</div>
		);
	}
});