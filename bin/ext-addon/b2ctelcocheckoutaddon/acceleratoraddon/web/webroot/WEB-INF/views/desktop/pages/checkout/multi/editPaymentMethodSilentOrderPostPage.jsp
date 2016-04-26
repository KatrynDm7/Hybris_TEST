<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/desktop/nav"%>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/addons/b2ctelcocheckoutaddon/desktop/formElement"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="address" tagdir="/WEB-INF/tags/desktop/address" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common"%>
<%@ taglib prefix="breadcrumb" tagdir="/WEB-INF/tags/desktop/nav/breadcrumb"%>
<%@ taglib prefix="ycommerce" uri="/WEB-INF/tld/ycommercetags.tld"%>
<%@ taglib prefix="multi-checkout" tagdir="/WEB-INF/tags/desktop/checkout/multi"%>



<c:url value="/checkout/multi/choose-payment-method" var="choosePaymentMethodUrl" />
<c:url value="/sbg-sop-mock/handle-form-post" var="sopMockUrl" />

<template:page pageTitle="${pageTitle}">

	<c:choose>
		<c:when test="${targetArea eq 'accountArea'}">
			<nav:accountNav selected="payment-details" />
			<c:set var="divClass" value="span-20 last cust_acc-page"/>
		</c:when>
		<c:otherwise>
			<c:set var="divClass" value="telco-checkout checkout-add-payment"/>
		</c:otherwise>
	</c:choose>

    <div class="span-24">
        <cms:pageSlot position="SideContent" var="feature" element="div" class="span-4 accountLeftNavigation">
            <cms:component component="${feature}"/>
        </cms:pageSlot>


	    <div class="span-20 last accountContentPane ${divClass}">

            <div id="globalMessages">
                <common:globalMessages />
            </div>

            <div class="headline">
                <spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.header" text="Payment Details" />
            </div>

            <form:form method="post" commandName="sopPaymentDetailsForm"
                class="create_update_payment_form"
                action="${paymentFormUrl}">

                <%-- payment card --%>
                <div class="cardForm">
                    <h1><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.paymentCard" /></h1>

                    <div class="required right"><spring:theme code="form.required" /></div>

                    <div class="description">
                        <spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.enterYourCardDetails" />
                    </div>

                    <c:if test="${isEditMode}">
                        <c:set var="selectCSSClass" value="uneditable"/>
                    </c:if>

                    <formElement:formSelectBox idKey="payment.cardType"
                        labelKey="payment.cardType" path="card_cardType" mandatory="true"
                        skipBlank="false"
                        skipBlankMessageKey="payment.cardType.pleaseSelect"
                        selectCSSClass="${selectCSSClass}"
                        items="${cardTypes}" tabindex="1" disabled="${isEditMode}"/>
                    <c:if test="${isEditMode}">
                        <form:hidden path="card_cardType" id="payment.cardType"/>
                    </c:if>
                    <formElement:formInputBox idKey="payment.nameOnCard"
                        labelKey="payment.nameOnCard" path="card_nameOnCard" inputCSS="text"
                        tabindex="2" readonly="${isEditMode}"/>

                    <formElement:formInputBox idKey="payment.cardNumber"
                        labelKey="payment.cardNumber" path="card_accountNumber" inputCSS="text"
                        mandatory="true" tabindex="3" readonly="${isEditMode}"/>
                    <formElement:formInputBox idKey="payment.issueNumber"
                        labelKey="payment.issueNumber" path="card_issueNumber"
                        inputCSS="text" mandatory="false" tabindex="8" readonly="${isEditMode}"/>


                    <fieldset class="cardDate">
                        <legend><spring:theme code="payment.startDate" /></legend>

                        <c:set var="cardDate" value="card_date"/>
                        <c:if test="${isEditMode}">
                            <c:set var="cardDate" value="${cardDate} uneditable"/>
                        </c:if>

                        <template:errorSpanField path="card_startMonth">
                            <form:select id="StartMonth" path="card_startMonth" cssClass="${cardDate}" tabindex="4"  disabled="${isEditMode}">
                                <option value="" label="<spring:theme code='payment.month'/>" />
                                <form:options items="${months}" itemValue="code" itemLabel="name" />
                            </form:select>

                            <c:if test="${isEditMode}">
                                <form:hidden path="card_startMonth" id="StartMonth"/>
                            </c:if>
                        </template:errorSpanField>

                        <template:errorSpanField path="card_startYear">
                            <form:select id="StartYear" path="card_startYear" cssClass="${cardDate}" tabindex="5"  disabled="${isEditMode}">
                                <option value="" label="<spring:theme code='payment.year'/>" />
                                <form:options items="${startYears}" itemValue="code" itemLabel="name" />
                            </form:select>
                            <c:if test="${isEditMode}">
                                <form:hidden path="card_startYear" id="StartYear"/>
                            </c:if>
                        </template:errorSpanField>
                    </fieldset>



                    <fieldset class="cardDate">
                        <legend><spring:theme code="payment.expiryDate" /></legend>

                        <template:errorSpanField path="card_expirationMonth">
                            <form:select id="ExpiryMonth" path="card_expirationMonth" cssClass="${cardDate}" tabindex="6" disabled="${isEditMode}">
                                <option value="" label="<spring:theme code='payment.month'/>" />
                                <form:options items="${months}" itemValue="code" itemLabel="name" />
                            </form:select>
                            <c:if test="${isEditMode}">
                                <form:hidden path="card_expirationMonth" id="ExpiryMonth"/>
                            </c:if>
                        </template:errorSpanField>

                        <template:errorSpanField path="card_expirationYear">
                            <form:select id="ExpiryYear" path="card_expirationYear" cssClass="${cardDate}" tabindex="7" disabled="${isEditMode}">
                                <option value="" label="<spring:theme code='payment.year'/>" />
                                <form:options items="${expiryYears}" itemValue="code" itemLabel="name" />
                            </form:select>
                            <c:if test="${isEditMode}">
                                <form:hidden path="card_expirationYear" id="ExpiryYear"/>
                            </c:if>
                        </template:errorSpanField>
                    </fieldset>
                </div>


                <%-- billing address --%>
                <div class="billingForm">

                    <h1>
                        <spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.billingAddress" />
                    </h1>

                    <div class="required right"><spring:theme code="form.required" /></div>

                    <div id="newBillingAddressFields">

                    <c:if test="${isEditMode}">
                        <button class="form secondary" type="button" onclick="ACC.payment.clearBillingFields()">
                            <spring:theme code="text.account.paymentDetails.edit.clearAddressFields" text="Clear Address Fields"/>
                        </button>
                        <div class="clear"></div>
                    </c:if>
								
                        <div class="clear"></div>

                        <address:billAddressFormSelector supportedCountries="${countries}" regions="${regions}" tabindex="12"/>

                        <div class="clear"></div>
                </div>

                <div class="form-actions">
                    <c:url value="/my-account/my-payment-details" var="paymentDetails" />
                    <a class="r_action_btn" href="${paymentDetails}">
                        <spring:theme code="checkout.multi.cancel" text="Cancel" />
                    </a>

                    <ycommerce:testId code="editPaymentMethod_savePaymentMethod_button">
                        <button class="positive" tabindex="19" id="lastInTheForm">
                            <spring:theme code="text.account.paymentDetails.savePaymentDetails" text="Save Payment Details"/>
                        </button>
                    </ycommerce:testId>
                </div>
            </form:form>
		</div>
    </div>
</template:page>
