<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="addon" tagdir="/WEB-INF/tags/addons/ysapordermgmtb2baddon/desktop/order"%>
<%@ taglib prefix="order" tagdir="/WEB-INF/tags/desktop/order"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<div class="accountBodyContentSlot">
	<div class="headline">Order Details</div>
	<div class="span-19">
		<div class="span-7">
			<spring:theme code="text.account.order.orderNumber"
				arguments="${orderData.code}" />
			<br />
			<spring:theme code="text.account.order.orderPlaced"
				arguments="${orderData.createdDateSAPFormat}" />
			<br />
			<c:if test="${not empty orderData.statusDisplay}">
				<spring:theme
					code="text.account.order.status.display.${orderData.statusDisplay}"
					var="orderStatus" />
				<spring:theme code="text.account.order.orderStatus"
					arguments="${orderStatus}" />
				<br />
			</c:if>
			<c:if test="${not empty orderData.deliveryStatusDisplay}">
				<spring:theme
					code="sap.custom.order.status.${orderData.deliveryStatusDisplay}"
					var="deliveryStatus" />
				<p>
					<spring:theme code="text.account.order.orderStatus"
						text="The order is {0}" arguments="${deliveryStatus}" />
				</p>
			</c:if>
		</div>

		<div class="span-5">
			&nbsp;
			<order:receivedPromotions order="${orderData}" />
		</div>
		<div class="span-6 last order-totals">
			<addon:orderTotalsItem order="${orderData}" />
		</div>
	</div>

	<div class="orderBoxes clearfix">
		<addon:paymentMethodItem order="${orderData}" />
		<order:deliveryAddressItem order="${orderData}" />
		<order:deliveryMethodItem order="${orderData}" />

		<c:if test="${not empty orderData.paymentInfo}">
			<div class="orderBox payment">
				<order:paymentDetailsItem order="${orderData}" />
			</div>
		</c:if>
	</div>

	<addon:orderDetailsItem order="${orderData}" />

</div>


