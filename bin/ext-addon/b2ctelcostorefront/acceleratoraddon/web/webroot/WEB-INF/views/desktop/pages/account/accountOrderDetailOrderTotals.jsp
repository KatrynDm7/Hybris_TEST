<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="order" tagdir="/WEB-INF/tags/desktop/order" %>
<%@ taglib prefix="telcoOrder" tagdir="/WEB-INF/tags/addons/b2ctelcostorefront/desktop/order" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div class="accountOrderDetailOrderTotals clearfix">
	<div class="span-8">
		<spring:theme code="text.account.order.orderNumber" arguments="${orderData.code}"/><br/>
		<spring:theme code="text.account.order.orderPlaced" arguments="${orderData.created}"/>
		<c:if test="${not empty orderData.statusDisplay}">
			<spring:theme code="text.account.order.status.display.${orderData.statusDisplay}" var="orderStatus"/><br/>
			<spring:theme code="text.account.order.orderStatus" arguments="${orderStatus}"/>
		</c:if>
	</div>
	<div class="span-8">
		<order:receivedPromotions order="${orderData}"/>
	</div>

    <telcoOrder:orderTotalsItem order="${orderData}"/>

</div>