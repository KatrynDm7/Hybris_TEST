<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html>
<title>Stadium Details</title>
<body>
<h1>Stadium Details</h1>

Stadium Details for ${stadium.name}<br><br>
Capacity: ${stadium.capacity}<br>
Matches:<br>
<ul>
    <c:forEach var="match" items="${stadium.matches}">
        <li>${match.matchSummaryFormatted}</li>
    </c:forEach>
</ul>
<a href="../stadiums">Back to Stadium Listing</a>
</body>
</html>
