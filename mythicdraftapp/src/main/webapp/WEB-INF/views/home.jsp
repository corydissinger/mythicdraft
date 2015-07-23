<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<title>Mythic Draft</title>
	
	<%-- Metas - down with raredraft --%>
	<meta name="viewport" content="width=device-width, initial-scale=1">
	
	<%-- Static assets --%>
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootswatch/3.3.5/darkly/bootstrap.min.css"></link>
	
	<script src="https://fb.me/react-with-addons-0.13.3.min.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/react-modal/0.3.0/react-modal.min.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/superagent/0.15.7/superagent.min.js"></script>

	<script type="text/javascript">
		var request = window.superagent;
	</script>

	<c:choose>
		<c:when test="${development}">
			<script src="https://fb.me/react-with-addons-0.13.3.js"></script>
			<script src="https://cdnjs.cloudflare.com/ajax/libs/react-modal/0.3.0/react-modal.js"></script>
			<script src="https://cdnjs.cloudflare.com/ajax/libs/superagent/0.15.7/superagent.js"></script>		
			<script src="https://fb.me/JSXTransformer-0.13.3.js"></script>
			<script type="text/jsx" src="resources/js/mythicdraft.js"></script>
		</c:when>
		<c:otherwise>
			<script src="https://fb.me/react-with-addons-0.13.3.min.js"></script>
			<script src="https://cdnjs.cloudflare.com/ajax/libs/react-modal/0.3.0/react-modal.min.js"></script>
			<script src="https://cdnjs.cloudflare.com/ajax/libs/superagent/0.15.7/superagent.min.js"></script>		
			<script src="resources/js/app.js"></script>		
		</c:otherwise>
	</c:choose>
</head>
<body>
	<div id="navContainer"></div>
	<img src="http://placehold.it/728x90" alt="Leaderboard Ad" class="img-responsive center-block" />
	<div id="container"></div>
	<div id="uploadContainer"></div>
	
	<c:choose>
		<c:when test="${development}">
			<script type="text/jsx" src="resources/js/mythicdraft.js"></script>
		</c:when>
		<c:otherwise>
			<script src="resources/js/app.js"></script>		
		</c:otherwise>
	</c:choose>	
	
</body>
</html>