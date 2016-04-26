<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="productCodes" required="false" type="java.lang.String[]"%>
<%@ attribute name="bundleTemplateIds" required="false" type="java.lang.String[]"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<spring:url var="cartUrl" value="/cart/addBundle">
	<spring:param name="CSRFToken" value="${CSRFToken}" />
</spring:url>

<form action="${cartUrl}" method="post">
	<c:forEach var="productCode" items="${productCodes}">
		<input type="hidden" name="productCodes" value="${productCode}">
	</c:forEach>
	<c:forEach var="bundleTemplateId" items="${bundleTemplateIds}">
		<input type="hidden" name="bundleTemplateIds"
			value="${bundleTemplateId}">
	</c:forEach>
	<input type="submit"
		value='<spring:message code="basket.add.to.basket"/>'>
</form>