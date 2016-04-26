<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="order" tagdir="/WEB-INF/tags/addons/b2ctelcocheckoutaddon/desktop/order" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common" %>
<%@ taglib prefix="breadcrumb" tagdir="/WEB-INF/tags/desktop/nav/breadcrumb" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>

<template:page pageTitle="${pageTitle}">
	<div id="breadcrumb" class="breadcrumb">
		<breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}"/>
	</div>

	<div id="globalMessages">
		<common:globalMessages/>
	</div>

<div class="telco-checkout checkout-confirmation"> <!-- telco change -->

	<div class="span-24 multicheckout">
	
		<ycommerce:testId code="orderConfirmation_yourOrderResults_text">
			<div class="span-24 your_order">
				<h1>
					<spring:theme code="checkout.orderConfirmation.thankYou"/>
				</h1>
				<p>
					<spring:theme code="checkout.orderConfirmation.copySentTo" arguments="${email}"/>
				</p>
                <p>
                    <spring:theme code="text.account.order.orderNumber" text="Order number is {0}" arguments="${orderData.code}"/>
                </p>
                <p>
                    <spring:theme code="text.account.order.orderPlaced" text="Placed on {0}" arguments="${orderData.created}"/>
                </p>
                <c:if test="${not empty orderData.status}">
                    <p><spring:theme code="text.account.order.orderStatus" text="The order is {0}" arguments="${orderData.statusDisplay}"/></p>
                </c:if>
			</div>
		</ycommerce:testId>

        <div class="clear"></div>
		<div class="span-24 top-content-slot">
			<cms:pageSlot position="TopContent" var="feature" contentSlot="${slots.TopContent}">
				<cms:component component="${feature}"/>
			</cms:pageSlot>
		</div>
        <div class="clear"></div>

		<div class="checkout_summary_flow">
			<div class="span-8">
				<order:deliveryAddressItem order="${orderData}"/>
			</div>
			<div class="span-8">
				<order:deliveryMethodItem order="${orderData}"/>
			</div>
			<div class="span-8 last">
				<order:paymentMethodItem order="${orderData}"/>
			</div>
		</div>


            <order:orderDetailsItem order="${orderData}"/>

            <order:orderTotalsItem order="${orderData}"/>

			<div class="cartPromo span-9">
				<order:receivedPromotions order="${orderData}"/>
			</div>

		
	</div>
	
	<div class="span-24 side-content-slot disp-img">
		<cms:pageSlot position="SideContent" var="feature" contentSlot="${slots.SideContent}">
			<cms:component component="${feature}"/>
		</cms:pageSlot>
	</div>
	
</div>
</template:page>
