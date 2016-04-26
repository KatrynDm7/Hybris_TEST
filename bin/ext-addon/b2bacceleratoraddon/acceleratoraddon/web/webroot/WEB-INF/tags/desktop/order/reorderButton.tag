<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="order" required="true" type="de.hybris.platform.commercefacades.order.data.AbstractOrderData" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<spring:url value="/checkout/summary/reorder" var="reorderUrl" />

<form:form action="${reorderUrl}" id="reorderForm" commandName="reorderForm">
	<button type="submit" class="positive right pad_right re-order" id="reorderButton">
		<spring:theme code="text.order.reorderbutton" text="Reorder"/>
	</button>
	<div>	
		<input type="hidden" name="orderCode" path="orderCode" value="${order.code}" />
	</div>
</form:form>
