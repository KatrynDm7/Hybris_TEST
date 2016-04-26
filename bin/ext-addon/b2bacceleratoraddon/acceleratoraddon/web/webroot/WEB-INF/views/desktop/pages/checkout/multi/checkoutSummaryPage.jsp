<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template" %>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/desktop/cart" %>
<%@ taglib prefix="checkout" tagdir="/WEB-INF/tags/desktop/checkout" %>
<%@ taglib prefix="multi-checkout" tagdir="/WEB-INF/tags/desktop/checkout/multi" %>
<%@ taglib prefix="b2b-multi-checkout" tagdir="/WEB-INF/tags/addons/b2bacceleratoraddon/desktop/checkout/multi" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>

<spring:url value="/checkout/multi/summary/placeOrder" var="placeOrderUrl"/>
<spring:url value="/checkout/multi/termsAndConditions" var="getTermsAndConditionsUrl"/>


<template:page pageTitle="${pageTitle}" hideHeaderLinks="true">

	<div id="globalMessages">
		<common:globalMessages/>
	</div>

	<multi-checkout:checkoutProgressBar steps="${checkoutSteps}" progressBarId="${progressBarId}"/>
	<div id="placeOrder" class="span-14 append-1">
		<b2b-multi-checkout:summaryFlow deliveryAddress="${cartData.deliveryAddress}" deliveryMode="${deliveryMode}" paymentInfo="${paymentInfo}" requestSecurityCode="${requestSecurityCode}" cartData="${cartData}"/>
		<form:form action="${placeOrderUrl}" id="placeOrderForm1" commandName="placeOrderForm">
			<div class="terms">
				<form:checkbox id="Terms1" path="termsCheck"/>
				<label for="Terms1"><spring:theme code="checkout.summary.placeOrder.readTermsAndConditions" arguments="${getTermsAndConditionsUrl}"/></label>
			</div>
			<c:if test="${requestSecurityCode}">
				<form:input type="hidden" class="securityCodeClass" path="securityCode"/>
				<button type="submit" class="positive right pad_right place-order placeOrderWithSecurityCode">
					<spring:theme code="checkout.summary.placeOrder"/>
				</button>
				<button type="button" class="positive right pad_right requestQuoteButton negotiateQuoteWithSecurityCode">
					<spring:theme code="checkout.summary.requestQuote"/>
				</button>
				<button type="button" class="positive right pad_right scheduleReplenishmentButton scheduleReplenishmentWithSecurityCode">
					<spring:theme code="checkout.summary.scheduleReplenishment"/>
				</button>
			</c:if>

			<c:if test="${not requestSecurityCode}">
				<button type="submit" class="positive right place-order">
					<spring:theme code="checkout.summary.placeOrder"/>
				</button>
				<button type="button" class="positive right pad_right requestQuoteButton">
					<spring:theme code="checkout.summary.requestQuote"/>
				</button>
				<button type="button" class="positive right pad_right scheduleReplenishmentButton">
					<spring:theme code="checkout.summary.scheduleReplenishment"/>
				</button>
			</c:if>
			
			<b2b-multi-checkout:replenishmentScheduleForm/>
			<b2b-multi-checkout:requestQuote/>
		</form:form>
	</div>
	<b2b-multi-checkout:checkoutOrderDetails cartData="${cartData}" showShipDeliveryEntries="true" showPickupDeliveryEntries="true" showTax="true"/>
	<cms:pageSlot position="SideContent" var="feature" element="div" class="span-24 side-content-slot cms_disp-img_slot">
		<cms:component component="${feature}"/>
	</cms:pageSlot>

</template:page>