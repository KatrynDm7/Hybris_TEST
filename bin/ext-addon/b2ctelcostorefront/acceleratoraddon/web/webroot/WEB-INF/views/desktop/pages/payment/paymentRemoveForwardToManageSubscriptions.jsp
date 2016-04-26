<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common"%>

<script type="text/javascript">

$("div").removeClass("span-24");

</script>
<div id="payment-method-delete">
    <div class="small_popup_big_headline">
	    <spring:theme code="text.account.paymentDetails.removePaymentMethod" text="Remove Payment Details"/>
	</div>

    <div class="small_popup_content">
        <div id="globalMessages">
            <common:globalMessages/>
        </div>

        <div class="payment-box">
            <div class="credit-card">${paymentInfo.cardTypeData.name}</div>
            <spring:theme code="text.account.paymentDetails.payment.details" arguments="${fn:replace(paymentInfo.cardNumber,'*','')}" text="cardNumber"/><br>
            <spring:theme code="text.expires" text="Expires"/>:&nbsp;${paymentInfo.expiryMonth}/${paymentInfo.expiryYear}
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
                <spring:theme code="text.no" text="Cancel"/>
            </a>

            <c:url value="/my-account/my-payment-details/manage?paymentInfoId=${paymentInfo.id}" var="paymentMethodSubscriptionsUrl"/>
            <a href="${paymentMethodSubscriptionsUrl}" class="button positive">
                <spring:theme code="text.account.paymentDetails.manageSubscriptions" text="Manage Subscriptions"/>
            </a>
        </div>
    </div>
</div>