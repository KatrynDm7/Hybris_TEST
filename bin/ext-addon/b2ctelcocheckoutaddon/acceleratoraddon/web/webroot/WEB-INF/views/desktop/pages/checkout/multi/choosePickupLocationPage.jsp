<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template" %>
<%@ taglib prefix="multi-checkout" tagdir="/WEB-INF/tags/addons/b2ctelcocheckoutaddon/desktop/checkout/multi" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/desktop/cart" %>

<c:url value="${nextStepUrl}" var="nextStep"/>

<template:page pageTitle="${pageTitle}">
	<div id="globalMessages">
		<common:globalMessages/>
		<cart:cartValidation/>
	</div>

	<multi-checkout:checkoutProgressBar steps="${checkoutSteps}" progressBarId="${progressBarId}"/>

	<div class="span-14">
		<multi-checkout:pickupConsolidationOptions cartData="${cartData}" pickupConsolidationOptions="${pickupConsolidationOptions}"/>
		<multi-checkout:pickupGroups cartData="${cartData}"/>

		<form:form id="selectDeliverylocationForm" action="${nextStep}" method="get">
			<button id="chooseDeliveryLocation_continue_button" class="positive continue right pad_right">
                <spring:theme code="checkout.multi.deliveryMethod.continue" text="Next"/>
			</button>
		</form:form>
	</div>
	<a class="button" href="${nextStep}"><spring:theme code="checkout.multi.deliveryMethod.continue" text="Next"/></a>
	<multi-checkout:checkoutOrderDetails cartData="${cartData}" showShipDeliveryEntries="false" showPickupDeliveryEntries="true" showTax="false"/>


	<cms:pageSlot position="SideContent" var="feature" element="div" class="span-24 side-content-slot cms_disp-img_slot">
		<cms:component component="${feature}"/>
	</cms:pageSlot>

</template:page>
