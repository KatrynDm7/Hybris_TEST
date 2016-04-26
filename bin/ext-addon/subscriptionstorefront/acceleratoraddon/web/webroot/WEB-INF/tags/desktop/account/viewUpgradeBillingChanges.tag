<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="upgradePreviewData" required="true" type="java.util.List" %>
<%@ attribute name="tabId" required="true" type="java.lang.String" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div id="upgrade-billing-changes">
	<div class="small_popup_big_headline">
		<spring:theme code="text.account.upgrade.proposedBillingChanges" text="Proposed Billing Changes"/>
	</div>

    <div class="small_popup_content">
        <div class="change-compare clearfix">
            <div class="payment-box">
                <div class="title"><spring:theme code="text.account.upgrade.currentPlan" text="Proposed Billing Changes"/></div>
                <div class="plan">${subProdData.name}</div>
                <div class="term">${subProdData.subscriptionTerm.name}</div>
            </div>

            <div class="payment-box right">
                <div class="title"><spring:theme code="text.account.upgrade.upgradePlan" text="Proposed Billing Changes"/></div>
                <div class="plan">${upgradeData.name}</div>
                <div class="term">${upgradeData.subscriptionTerm.name}</div>
            </div>

        </div>



        <c:forEach items="${upgradePreviewData}" var="upgradePreview" varStatus="loop">
            <table class="upgrade-preview-table">
                <thead>
                    <tr>
                        <th><spring:theme code="text.account.upgrade.billingPeriod" text="Billing Period"/></th>
                        <th>${upgradePreview.billingPeriod}</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td><spring:theme code="text.account.upgrade.paymentAmount" text="Payment Amount"/></td>
                        <td>${upgradePreview.paymentAmount}</td>
                    </tr>
                    <tr>
                        <td><spring:theme code="text.account.upgrade.billingDate" text="Billing Date"/></td>
                        <td>${upgradePreview.billingDate}</td>
                    </tr>
                </tbody>
            </table>
        </c:forEach>




        <div class="actions">
            <a class="r_action_btn cancel" href="#">
                <spring:theme code="text.cancel" text="Cancel"/>
            </a>

            <c:url value="/my-account/subscription/upgrade" var="upgradeSubscription" />
            <form:form class="upgrade_subscription_form" action="${upgradeSubscription}" method="post">
                <button  type="submit"  class="confirm positive">
                    <spring:theme code="text.account.subscription.upgradeSubscriptionNow" text="Upgrade Now"/>
                </button>

                <input type="hidden" name="productCode" value="${upgradeData.code}"/>
                <input type="hidden" name="subscriptionId" value="${subscriptionData.id}"/>
                <input type="hidden" name="originalOrderCode" value="${subscriptionData.orderNumber}"/>
                <input type="hidden" name="originalEntryNumber" value="${subscriptionData.orderEntryNumber}"/>
            </form:form>
        </div>
    </div>
</div>
