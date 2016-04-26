<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="order" tagdir="/WEB-INF/tags/desktop/order" %>
<%@ taglib prefix="b2b-order" tagdir="/WEB-INF/tags/addons/b2bacceleratoraddon/desktop/order" %>

<div class="orderBoxes clearfix">
	<order:deliveryAddressItem order="${orderData}"/>
	<order:deliveryMethodItem order="${orderData}"/>
	<c:if test="${orderData.paymentType.code eq 'CARD'}">
		<div class="orderBox billing">
			<order:billingAddressItem order="${orderData}"/>
		</div>
		<div class="orderBox payment">
			<order:paymentDetailsItem order="${orderData}"/>
		</div>
	</c:if>
	<c:if test="${orderData.paymentType.code eq 'ACCOUNT'}">
		<div class="orderBox payment">
			<b2b-order:paymentDetailsAccountItem order="${orderData}"/>
		</div>
	</c:if>
	
</div>