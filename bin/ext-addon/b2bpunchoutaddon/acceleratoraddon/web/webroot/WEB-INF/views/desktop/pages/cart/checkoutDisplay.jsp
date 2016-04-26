<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="addoncart" tagdir="/WEB-INF/tags/addons/b2bpunchoutaddon/desktop/cart" %>

<c:url value="/cxml/requisition" context="${originalContextPath}/punchout" var="checkoutUrl"/>
<c:url value="/cxml/cancel" context="${originalContextPath}/punchout" var="cancelUrl"/>

<c:choose>
	<c:when test="${isInspectOperation}">
		<addoncart:returnButton url="${cancelUrl}" />
	</c:when>
	<c:otherwise>
		<a class="button" href="${continueShoppingUrl}">
			<spring:theme text="Continue Shopping" code="cart.page.continue"/>
		</a>
		<addoncart:checkoutButton url="${checkoutUrl}" />
		<addoncart:cancelButton url="${cancelUrl}" />
	</c:otherwise>
</c:choose>
