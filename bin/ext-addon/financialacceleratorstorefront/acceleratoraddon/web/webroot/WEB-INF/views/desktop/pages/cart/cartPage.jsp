<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template" %>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/desktop/cart" %>
<%@ taglib prefix="financialCart" tagdir="/WEB-INF/tags/addons/financialacceleratorstorefront/desktop/cart" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product" %>

<spring:theme text="Your Shopping Cart" var="title" code="cart.page.title"/>
<c:url value="/cart/checkout" var="checkoutUrl"/>

<template:page pageTitle="${pageTitle}">
	
	<cart:cartRestoration/>
	<cart:cartValidation/>
	<cart:cartPickupValidation/>

    <div class="span-24 clearfix">
        <cms:pageSlot position="TopContent" var="feature" element="div" class="span-24">
            <cms:component component="${feature}"/>
        </cms:pageSlot>
    </div>

    <div class="span-24">
        <div class="span-16 first cartItemsList">
            <div class="headline">
                <h3><spring:theme code="cart.product.options"/></h3>
            </div>
            <div class="span-16 ItemsList">
            	<%--  Temporary Fix to INSA-238 --%>
            	<c:set var="bundleNoTemp" value="${cartData.entries[0].bundleNo}"/>
                <c:forEach items="${cartData.entries}" var="entry" varStatus="status">
                    <c:if test="${not status.first}">
                        <hr>
                    	<c:set target="${entry}" property="bundleNo" value="${bundleNoTemp}" />
                        <financialCart:cartPotentialItem orderEntryData="${entry}" planProductData="${planProductData}"/>
                    </c:if>
                </c:forEach>
            </div>
            <div class="span-16 buttonColumn">
                <c:if test="${not empty cartData.entries}">
                    <button id="checkoutButtonBottom" class="doCheckoutBut positive" type="button" data-checkout-url="${checkoutUrl}">
                        <spring:theme code="checkout.next"/>
                    </button>
                </c:if>
            </div>
        </div>
        <div class="span-8 last cartData">
            <financialCart:cartItems cartData="${cartData}" displayChangeOptionLink="false"/>
            <financialCart:cartTotals cartData="${cartData}" showTaxEstimate="${taxEstimationEnabled}"/>
        </div>
		<financialCart:cartModifyPlan cartData="${cartData}" flowStartUrl="${flowStartUrl}"/>
    </div>

    <div class="span-24">
        <div class="span-24 wide-content-slot cms_disp-img_slot">
            <c:if test="${empty cartData.entries}">
                <cms:pageSlot position="MiddleContent" var="feature" element="div">
                    <cms:component component="${feature}"/>
                </cms:pageSlot>
            </c:if>
            <c:if test="${not empty cartData.entries}">
                <cms:pageSlot position="Suggestions" var="feature" element="div" class="span-24">
                    <cms:component component="${feature}"/>
                </cms:pageSlot>
            </c:if>
            <cms:pageSlot position="BottomContent" var="feature" element="div">
                <cms:component component="${feature}"/>
            </cms:pageSlot>
        </div>
    </div>

</template:page>
