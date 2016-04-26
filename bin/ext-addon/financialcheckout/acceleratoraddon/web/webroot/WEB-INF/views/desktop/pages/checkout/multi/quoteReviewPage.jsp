<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/desktop/nav" %>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/desktop/formElement" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="multi-checkout" tagdir="/WEB-INF/tags/addons/financialcheckout/desktop/checkout/multi" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="quote" tagdir="/WEB-INF/tags/addons/financialcheckout/desktop/checkout/multi/quote" %>
<%@ taglib prefix="insuranceCheckout" tagdir="/WEB-INF/tags/addons/financialacceleratorstorefront/desktop/checkout" %>

<template:page pageTitle="${pageTitle}">

	<multi-checkout:checkoutProgressBar steps="${checkoutSteps}" progressBarId="${progressBarId}"/>
	<div class="span-15 append-1">
		<div id="checkoutContentPanel" class="clearfix">
			<c:url value="${nextStepUrl}" var="nextStep"/>
			<c:url value="/checkout/multi/quote/back" var="backStep"/>
			<div class="span-15" id="globalMessages">
				<common:globalMessages/>
			</div>
			<div class="span-15 quoteHeader greyBox">
				<quote:quoteHeader insuranceQuoteReviews="${insuranceQuoteReviews}"/>
			</div>
            <c:if test="${cartData.insuranceQuote.quoteType ne 'EVENT'}">
                <div class="span-15 quotePlanInfoSection">
                    <quote:quotePlanInfoSection insuranceQuoteReviews="${insuranceQuoteReviews}" isValidStep="${insuranceQuoteReviews[0].validation['0']}"/>
                </div>
            </c:if>
			<div class="span-15 quoteWhatsIncluded">
				<quote:quoteIncluded insuranceQuoteReviews="${insuranceQuoteReviews}" isValidStep="${insuranceQuoteReviews[0].validation['0']}"/>
			</div>
			<div class="span-15 optionalExtras">
				<quote:quoteExtras insuranceQuoteReviews="${insuranceQuoteReviews}" isValidStep="${insuranceQuoteReviews[0].validation['1']}"/>
			</div>
			
			<div class="span-15 quoteReviewDetails">
				<quote:quoteReviewDetails insuranceQuoteReviews="${insuranceQuoteReviews}" isValidStep="${insuranceQuoteReviews[0].validation['3']}"/>
			</div>

            <c:if test="${insuranceQuoteReviews[0].workFlowType eq 'LIFE'}">
                <div class="span-15 documentsFlow">
                    <quote:documentationFlow status="${insuranceQuoteReviews[0].documentationStatus }"/>
                </div>
            </c:if>
            
            <form:form method="post" commandName="quoteReviewForm" class="review_quote_form review_quote_form_back_button">
                <ycommerce:testId code="multicheckout_back_button">
                    <a class="button negative checkQuoteStatus" href="${backStep}">
                        <spring:theme code="checkout.multi.quoteReview.back" text="Back"/>
                    </a>
                </ycommerce:testId>
            </form:form>
            <quote:quoteCertifySection checkoutUrl="${nextStep}" cartData="${cartData}"/>
			
		</div>
	</div>

    <multi-checkout:checkoutOrderDetails cartData="${cartData}" showShipDeliveryEntries="true" showPickupDeliveryEntries="true" showTax="true"/>

    <cms:pageSlot position="SideContent" var="feature" element="div" class="span-24 side-content-slot cms_disp-img_slot">
		<cms:component component="${feature}"/>
	</cms:pageSlot>

	<insuranceCheckout:confirmQuoteUnbindActionPopup/>
	<insuranceCheckout:confirmQuoteBindActionPopup/>
</template:page>
	