<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/desktop/nav" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="multi-checkout" tagdir="/WEB-INF/tags/addons/financialcheckout/desktop/checkout/multi" %>
<%@ taglib prefix="address" tagdir="/WEB-INF/tags/desktop/address" %>

<template:page pageTitle="${pageTitle}">	
    <c:url value="${nextStepUrl}" var="nextStep"/>
    <c:url value="${previousStepUrl}" var="previousStep"/>

	<multi-checkout:checkoutProgressBar steps="${checkoutSteps}" progressBarId="${progressBarId}"/>
	<div class="span-15 append-1 clearfix cartLeft">
		<div id="checkoutContentPanel" class="xformContainer clearfix">
			<div class="required"><spring:theme code="form.required" text="Fields marked * are required"/></div>
			<div class="description"><spring:theme code="checkout.multi.description.${progressBarId}" text="Please fill in the necessary information for the quote."/></div>
            <div class="form">
                <c:forEach var="formHtml" items="${embeddedFormHtmls}">
                    <div class="formHtml">${formHtml}</div> <br/>
                </c:forEach>
            </div>
            <div id="form_button_panel">
            <ycommerce:testId code="multicheckout_cancel_button">
                <a id="backBtn" class="button negative" href="${previousStep}"><spring:theme code="checkout.multi.quoteForm.back" text="Back"/></a>
            </ycommerce:testId>
            <ycommerce:testId code="multicheckout_saveForm_button">
                <a id="continueBtn" class="button positive right show_processing_message" href="${nextStep}"><spring:theme code="checkout.multi.quoteForm.continue" text="Continue"/></a>
            </ycommerce:testId>
            </div>
        </div>
    </div>
    <multi-checkout:checkoutOrderDetails cartData="${cartData}" showShipDeliveryEntries="true" showPickupDeliveryEntries="true" showTax="false"/>

    <cms:pageSlot position="SideContent" var="feature" element="div" class="span-24 side-content-slot cms_disp-img_slot">
		<cms:component component="${feature}"/>
	</cms:pageSlot>

</template:page>
