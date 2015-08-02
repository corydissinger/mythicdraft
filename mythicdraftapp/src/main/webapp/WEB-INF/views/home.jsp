<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<title>Mythic Draft</title>
	
	<%-- Metas - down with raredraft --%>
	<meta name="viewport" content="width=device-width, initial-scale=1">
	
	<%-- Static assets --%>
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootswatch/3.3.5/darkly/bootstrap.min.css"></link>	

	<c:choose>
		<c:when test="${development}">
			<script src="https://fb.me/react-with-addons-0.13.3.js"></script>
			<script src="https://cdnjs.cloudflare.com/ajax/libs/react-modal/0.3.0/react-modal.js"></script>
			<script src="https://cdnjs.cloudflare.com/ajax/libs/react-router/0.13.3/ReactRouter.js"></script>			
			
			<script src="https://cdnjs.cloudflare.com/ajax/libs/superagent/1.2.0/superagent.js"></script>		
			<script src="https://fb.me/JSXTransformer-0.13.3.js"></script>
			
			<link rel="stylesheet" href="resources/css/mythicdraft.css"/>
		</c:when>
		<c:otherwise>
			<script src="https://fb.me/react-with-addons-0.13.3.min.js"></script>
			<script src="https://cdnjs.cloudflare.com/ajax/libs/react-modal/0.3.0/react-modal.min.js"></script>
			<script src="https://cdnjs.cloudflare.com/ajax/libs/react-router/0.13.3/ReactRouter.min.js"></script>
			
			<script src="https://cdnjs.cloudflare.com/ajax/libs/superagent/1.2.0/superagent.min.js"></script>					
					
			<link rel="stylesheet" href="resources/css/app.css"/>			
		</c:otherwise>
	</c:choose>
	
</head>
<body>

	<div id="navContainer"></div>	
	<div id="container"></div>
	
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