<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="order" tagdir="/WEB-INF/tags/desktop/order" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div class="accountOrderDetailOrderTotals clearfix">
	<div class="span-7">
		<ul>
			<li><spring:theme code="text.account.order.orderNumber" arguments="${orderData.code}"/></li>
			<li><spring:theme code="text.account.order.orderPlaced" arguments="${orderData.created}"/></li>
			<c:if test="${not empty orderData.statusDisplay}">
				<li><spring:theme code="text.account.order.status.display.${orderData.statusDisplay}" var="orderStatus"/></li>
				<li><spring:theme code="text.account.order.orderStatus" arguments="${orderStatus}"/></li>
			</c:if>
			<c:if test="${not empty orderData.placedBy}">
				<c:choose>
					<c:when test="${not empty agent}">
						<li><spring:theme code="text.account.order.placedBy" arguments="${orderData.placedBy}"/></li>
					</c:when>
					<c:otherwise>
						<li><spring:theme code="text.account.order.placedByText"/></li>
					</c:otherwise>
				</c:choose>
			</c:if>
		</ul>		
	</div>
	<div class="span-7">
		<order:receivedPromotions order="${orderData}"/><p/>
	</div>
	<div class="span-6 last order-totals">
		<order:orderTotalsItem order="${orderData}"/>
	</div>
</div>