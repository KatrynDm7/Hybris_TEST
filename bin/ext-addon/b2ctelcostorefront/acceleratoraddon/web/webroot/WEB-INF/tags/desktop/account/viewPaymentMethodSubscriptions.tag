<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="subscriptions" required="true" type="java.util.List" %>
<%@ attribute name="paymentInfo" required="true" type="de.hybris.platform.commercefacades.order.data.CCPaymentInfoData" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div id="payment-method-subscriptions">
    <div class="small_popup_big_headline">
	    <spring:theme code="text.account.paymentDetails.editPaymentDetails" text="Edit Payment Details"/>
	</div>

    <div class="small_popup_content">

        <div class="payment-box">
            <div class="credit-card">${paymentInfo.cardTypeData.name}</div>
            <spring:theme code="text.account.paymentDetails.payment.details" arguments="${fn:replace(paymentInfo.cardNumber,'*','')}" text="cardNumber"/><br>
            <spring:theme code="text.expires" text="Expires"/>:&nbsp;${paymentInfo.expiryMonth}/${paymentInfo.expiryYear}
        </div>

        <div class="infoline">
            <spring:theme code="text.account.paymentDetails.associatedSubscriptions" text="Edit Payment Details"/>
        </div>


        <table class="paymentListTable">
            <thead>
                <tr>
                    <th><spring:theme code="text.account.subscription.productName" text="Product Name"/></th>
                    <th><spring:theme code="text.account.subscription.startDate" text="Start Date"/></th>
                    <th><spring:theme code="text.account.subscription.endDate" text="End Date"/></th>
                    <th><spring:theme code="text.account.subscription.status" text="Status"/></th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${subscriptions}" var="subscription" varStatus="loop">
                    <tr>
                        <td>${subscription.name}</td>
                        <td><fmt:formatDate type="date" value="${subscription.startDate}"/></td>
                        <td><fmt:formatDate type="date" value="${subscription.endDate}"/></td>
                        <td>${subscription.subscriptionStatus}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <div class="actions">
            <a href="#" class="r_action_btn cancel">
                <spring:theme code="text.cancel" text="Cancel"/>
            </a>

            <c:url value="/my-account/my-payment-details/edit?paymentInfoId=${paymentInfo.id}&targetArea=accountArea" var="continueToEditPaymentDetailsUrl" />
            <a href="${continueToEditPaymentDetailsUrl}" class="button positive">
                <spring:theme code="text.editpaymentdetails" text="Edit Payment Details"/>
            </a>
        </div>
    </div>
</div>
