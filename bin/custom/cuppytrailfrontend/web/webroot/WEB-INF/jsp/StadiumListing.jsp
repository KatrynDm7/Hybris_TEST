<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html>
<title>Stadium Listing</title>
<body>
<h1>Stadium Listing</h1>
<ul>
    <c:forEach var="stadium" items="${stadiums}">
        <li><a href="./stadiums/${stadium.name}">${stadium.name}</a></li>
    </c:forEach>
</ul>
</body>
</html>
