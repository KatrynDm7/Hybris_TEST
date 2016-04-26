<%--
  ~ [y] hybris Platform
  ~
  ~ Copyright (c) 2000-2015 hybris AG
  ~ All rights reserved.
  ~
  ~ This software is the confidential and proprietary information of hybris
  ~ ("Confidential Information"). You shall not disclose such Confidential
  ~ Information and shall use it only in accordance with the terms of the
  ~ license agreement you entered into with hybris.
  --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8"/>
	<title>hybris testweb console | <sitemesh:write property='title'/></title>
	<link rel="stylesheet" href="<c:url value="/static/css/blueprint/screen.css"/>" type="text/css"
			media="screen, projection"/>
	<link rel="stylesheet" href="<c:url value="/static/css/blueprint/print.css"/>" type="text/css"
			media="screen, projection"/>
	<link rel="stylesheet" href="<c:url value="/static/css/blueprint/ie.css"/>" type="text/css"
			media="screen, projection"/>
	<link rel="stylesheet" href="<c:url value="/static/css/jquery-ui/jquery-ui.css" />" type="text/css"
			media="screen, projection"/>
	<link rel="stylesheet" href="<c:url value="/static/css/table.css"/>" type="text/css" media="screen, projection" />
	<link rel="stylesheet" href="<c:url value="/static/css/main.css"/>" type="text/css" media="screen, projection"/>
	<link rel="stylesheet" href="<c:url value="/static/css/style.css"/>" type="text/css" media="screen, projection"/>

	<script type="text/javascript" src="<c:url value="/static/js/jquery-2.0.2.min.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/static/js/jquery-ui.js" />"></script>
	<script type="text/javascript" src="<c:url value="/static/js/ba-debug.min.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/static/js/main.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/static/js/jquery.dataTables.min.js"/>"></script>
	<sitemesh:write property='head'/>
</head>
<body>
<div id="mainContainer" class="container" data-expandIconUrl="<c:url value="/static/img/expand-small-silver.png"  />"
	  data-collapseIconUrl="<c:url value="/static/img/collapse-small-silver.png"  />">
	<sitemesh:write property='body'/>
</div>
<footer>
	&copy; hybris AG, 2015
</footer>
</body>
</html>