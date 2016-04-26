<%@ attribute name="status" required="true" type="String" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:choose>
	<c:when test="${status eq 'Provided'}"> 
		<c:set var="isValidClass" value="valid js-toggle"/>
	</c:when>
	<c:otherwise><c:set var="isValidClass" value="invalid"/> </c:otherwise>
</c:choose>

<h2 class="${isValidClass}">
	<spring:theme code="checkout.multi.quoteReview.documentation" text="Documentation" />
</h2>
