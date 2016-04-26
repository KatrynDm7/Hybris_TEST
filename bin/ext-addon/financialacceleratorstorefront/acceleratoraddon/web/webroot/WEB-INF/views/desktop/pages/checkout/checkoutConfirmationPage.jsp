<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="order" tagdir="/WEB-INF/tags/desktop/order" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common" %>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/desktop/user" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="insuranceCheckout" tagdir="/WEB-INF/tags/addons/financialacceleratorstorefront/desktop/checkout" %>
<%@ taglib prefix="insurancOrder" tagdir="/WEB-INF/tags/addons/financialacceleratorstorefront/desktop/order" %>
<%@ taglib prefix="policy" tagdir="/WEB-INF/tags/addons/financialacceleratorstorefront/desktop/checkout/policy" %>

<template:page pageTitle="${pageTitle}">
	<div id="globalMessages">
		<common:globalMessages/>
	</div>
    <cms:pageSlot position="TopContent" var="feature" element="div" class="span-24 top-content-slot cms_disp-img_slot">
        <cms:component component="${feature}"/>
    </cms:pageSlot>
	<div class="span-24">
		<div class="orderHead">
			<ycommerce:testId code="orderConfirmation_yourOrderResults_text">
				<div class="youarecovered"><spring:theme code="checkout.orderConfirmation.youAreCovered" /></div>
				<div class="copy"><spring:theme code="checkout.orderConfirmation.saveToAccount"/></div>
			</ycommerce:testId>
        <insuranceCheckout:downloadPolicy order="${orderData}"/>


        </div>
        <sec:authorize ifAnyGranted="ROLE_ANONYMOUS">
            <div class="span-24 delivery_stages-guest last">
                <user:guestRegister actionNameKey="guest.register.submit"/>
            </div>
        </sec:authorize>
	</div>
    <div class="span-24">
        <div class="span-15 append-1">
            <div class="span-16">
            	<c:set var="images" value="${orderData.entries[0].product.images}"></c:set>
                <c:forEach items="${orderData.insurancePolicyResponses}" var="policyResponse">
                    <insuranceCheckout:policySummaryLine policyResponse="${policyResponse}" images="${images}" />
                </c:forEach>
            </div>
            <div class="clear"></div>
            <div id="checkoutContentPanel" class="clearfix">
                <c:forEach items="${orderData.insurancePolicy}" var="insurancePolicy">
                    <div class="span-16">
                    	
                    	<c:if test="${orderData.insuranceQuote.quoteType ne 'EVENT'}">
			                <div class="span-15 quotePlanInfoSection">
			                    <policy:policyPlanInfoSection orderData="${orderData}"/>
			                </div>
			            </c:if>
			            	
                        <div class="span-15 quoteWhatsIncluded">
                            <policy:policyWhatsIncluded policyData="${insurancePolicy}"/>
                        </div>
                        <div class="span-15 quoteWhatsIncluded">
                            <policy:policyOptionalExtras policyData="${insurancePolicy}"/>
                        </div>
                        <div class="span-15 quoteReviewDetails">
                            <policy:policyDetails policyData="${insurancePolicy}"/>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
        <div id="rightHandDetails">
            <insurancOrder:orderDetailsItem orderData="${orderData}"/>
        </div>

        <cms:pageSlot position="SideContent" var="feature" element="div" class="span-24 side-content-slot cms_disp-img_slot">
        <cms:component component="${feature}"/>
        </cms:pageSlot>
        <div>
            <a href="${request.contextPath}" class="button positive right"><spring:theme code="checkout.orderConfirmation.continueShopping" /></a>
        </div>
    </div>


</template:page>
