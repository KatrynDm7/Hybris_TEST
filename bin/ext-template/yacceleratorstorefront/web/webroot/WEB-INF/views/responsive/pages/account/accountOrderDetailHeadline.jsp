<%@ page trimDirectiveWhitespaces="true" %>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:url value="/my-account/orders" var="orderHistoryUrl"/>
<button type="button" class="btn btn-default orderTopBackBtn" data-back-to-orders="${orderHistoryUrl}">
	<span class="glyphicon glyphicon-chevron-left"></span><spring:theme code="text.account.order.back.btn" text=" Back"/>
</button>
<div class="account-section-header">
	<spring:theme code="text.account.order.title.details" text="Order Details" />
</div>
