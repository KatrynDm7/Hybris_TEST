<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<div id="payment-method-delete">
	<div class="small_popup_big_headline">
	    <spring:theme code="text.account.paymentDetails.removePaymentMethod" text="Remove Payment Details"/>
	</div>

    <div class="small_popup_content">
        <div class="payment-box">
            <div class="credit-card">${paymentInfo.cardTypeData.name}</div>
            <spring:theme code="text.account.paymentDetails.payment.details" arguments="${fn:replace(paymentInfo.cardNumber,'*','')}" text="cardNumber"/><br>
            <spring:theme code="text.expires" text="Expires"/>:&nbsp;${paymentInfo.expiryMonth}/${paymentInfo.expiryYear}
        </div>

        <div class="infoline">
            <spring:theme code="text.account.paymentDetails.removePaymentMethodQuestion"/>
        </div>

        <div class="actions">
            <a href="#" class="r_action_btn cancel">
                <spring:theme code="text.cancel" text="Cancel"/>
            </a>

            <c:url value="/my-account/my-payment-details/remove?paymentInfoId=${paymentInfo.id}" var="removePaymentMethodUrl" />
            <a href="${removePaymentMethodUrl}" class="button positive">
                <spring:theme code="text.account.paymentDetails.removePaymentDetailsConfirm" text="Remove Payment Details"/>
            </a>
        </div>
    </div>
</div>

