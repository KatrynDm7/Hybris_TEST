<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/desktop/cart" %>
<%@ taglib prefix="addoncart" tagdir="/WEB-INF/tags/addons/b2bpunchoutaddon/desktop/cart" %>

<c:url value="/cxml/requisition" context="${originalContextPath}/punchout" var="checkoutUrl"/>
<c:url value="/cxml/cancel" context="${originalContextPath}/punchout" var="cancelUrl"/>

<c:if test="${not empty cartData.entries}">
	<c:choose>
		<c:when test="${isInspectOperation}">
			<addoncart:returnButton url="${cancelUrl}" />
		</c:when>
		<c:otherwise>
			<addoncart:checkoutButton url="${checkoutUrl}" />
			<addoncart:cancelButton url="${cancelUrl}" />
		</c:otherwise>
	</c:choose>
	<cart:cartItems cartData="${cartData}"/>
</c:if>
