<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" isErrorPage="true"%>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html xmlns="http://www.w3.org/1999/xhtml">
<body>
	<%-- Display generic error to client --%>
	<h1>
		<spring:message code="text.saml.error"
			text="You do not have access rights to perform this operation. Please contact your System Administrator." />
	</h1>
</body>
</html>

