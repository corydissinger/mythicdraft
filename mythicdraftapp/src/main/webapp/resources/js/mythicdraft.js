var Upload = React.createClass({
	render: function(){
		return (
			<form action="upload" method="post" enctype="multipart/form-data">
				<input type="file" name="file" required></input>
				<input type="text" name="name" maxlength="20" required></input>
				<input type="submit"></input>
			</form>	
		);
	}
});

React.render(<Upload/>, document.getElementById("container"));