<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="productCode" required="true" type="java.lang.String"%>
<%@ attribute name="bundleTemplateId" required="true" type="java.lang.String"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:url var="cartUrl" value="/cart/addSingleProduct">
	<spring:param name="CSRFToken" value="${CSRFToken}" />
</spring:url>

<form action="${cartUrl}" method="post">
	<input type="hidden" name="productCodePost" value="${productCode}">
	<input type="hidden" name="bundleTemplateId" value="${bundleTemplateId}">
	<input type="submit" value='<spring:message code="basket.add.to.basket"/>'>
</form>