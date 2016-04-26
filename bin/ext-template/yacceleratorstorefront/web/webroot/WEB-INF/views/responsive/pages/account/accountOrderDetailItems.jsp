<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="order" tagdir="/WEB-INF/tags/responsive/order" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>

<spring:url value="/my-account/orders" var="ordersUrl"/>
<div class="account-orderdetail">
	<div class="account-orderdetail-overview">
		<ycommerce:testId code="orderDetail_overview_section">
			<order:accountOrderDetailsOverview order="${orderData}"/>
		</ycommerce:testId>
	</div>
	<div class="account-orderdetail-item-section">
		<ycommerce:testId code="orderDetail_itemList_section">
			<c:if test="${not empty orderData.unconsignedEntries}">
				<order:orderUnconsignedEntries order="${orderData}"/>
			</c:if>
			<c:forEach items="${orderData.consignments}" var="consignment">
				<c:if test="${consignment.status.code eq 'WAITING' or consignment.status.code eq 'PICKPACK' or consignment.status.code eq 'READY'}">
					<div class="productItemListHolder fulfilment-states-${consignment.status.code}">
						<order:accountOrderDetailsItem order="${orderData}" consignment="${consignment}" inProgress="true"/>
					</div>
				</c:if>
			</c:forEach>		
			<c:forEach items="${orderData.consignments}" var="consignment">
				<c:if test="${consignment.status.code ne 'WAITING' and consignment.status.code ne 'PICKPACK' and consignment.status.code ne 'READY'}">
					<div class="productItemListHolder fulfilment-states-${consignment.status.code}">
						<order:accountOrderDetailsItem order="${orderData}" consignment="${consignment}"/>
					</div>
				</c:if>
			</c:forEach>
		</ycommerce:testId>
	</div>
</div>