<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
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
			
			<link rel="stylesheet" media="all" href="resources/css/mythicdraft.css"/>
		</c:when>
		<c:otherwise>
			<script src="https://fb.me/react-with-addons-0.13.3.min.js"></script>
			<script src="https://cdnjs.cloudflare.com/ajax/libs/react-modal/0.3.0/react-modal.min.js"></script>
			<script src="https://cdnjs.cloudflare.com/ajax/libs/react-router/0.13.3/ReactRouter.min.js"></script>
			
			<script src="https://cdnjs.cloudflare.com/ajax/libs/superagent/1.2.0/superagent.min.js"></script>					
					
			<link rel="stylesheet" media="all" href="resources/css/app.css"/>

			<%-- Analytics --%>
			<script>
			  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
			  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
			  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
			  })(window,document,'script','//www.google-analytics.com/analytics.js','ga');

			  ga('create', 'UA-65898926-1', 'auto');
			  ga('send', 'pageview');

			</script>			
		</c:otherwise>
	</c:choose>
	
</head>
<body>

	<div id="navContainer"></div>	
	<div id="container"></div>
	
	<c:choose>
		<c:when test="${development}">
			<script type="text/jsx" src="resources/js/mythicdraft.js"></script>
			<script src="resources/js/bootstrap-toggle-shim.js"></script>
			<script src="resources/js/card-deck-organizer.js"></script>
		</c:when>
		<c:otherwise>
			<script src="resources/js/app.js"></script>		
		</c:otherwise>
	</c:choose>	
	
</body>
</html>