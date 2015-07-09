<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<title>Mythic Draft</title>
	
	<%-- Metas - down with raredraft --%>
	<meta name="viewport" content="width=device-width, initial-scale=1">
	
	<%-- Static assets --%>
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootswatch/3.3.5/darkly/bootstrap.min.css"></link>
	
	<script src="https://fb.me/react-with-addons-0.13.3.js"></script>
	<script src="https://fb.me/JSXTransformer-0.13.3.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/superagent/0.15.7/superagent.min.js"></script>
</head>
<body>
	<div id="container"></div>
	<div id="uploadContainer"></div>
	
	<script type="text/javascript">
		var request = window.superagent;
	</script>
	<script type="text/jsx" src="resources/js/mythicdraft.js"></script>
</body>
</html>