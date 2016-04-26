<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
   <head>
   <title>Heartbeat</title>
   </head>
   <body>
   
   		<h1>E2E Heartbeat Statistics</h1>
   		<h2>[<%= new java.util.Date()%>]</h2>
   		
   		<p>
   			<c:forEach items="${stats}" var="stats">
	   		[${stats.key}:${stats.value}]
			</c:forEach>
   		</p>
   </body>
</html>