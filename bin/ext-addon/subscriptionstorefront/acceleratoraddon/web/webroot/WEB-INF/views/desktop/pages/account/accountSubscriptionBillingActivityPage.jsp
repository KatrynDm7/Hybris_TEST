<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/desktop/nav" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common" %>
<%@ taglib prefix="ycommerce" uri="/WEB-INF/tld/ycommercetags.tld" %>
<%@ taglib prefix="breadcrumb" tagdir="/WEB-INF/tags/desktop/nav/breadcrumb" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/addons/subscriptionstorefront/desktop/product" %>

	<div id="globalMessages">
		<common:globalMessages/>
	</div>

    <div class="subscription_billing_activity">
        <div class="headline"><spring:theme code="text.account.subscription.billingActivity" text="Billing Activity"/></div>


        <div class="subHeadline">
            <ycommerce:testId code="subscription_productName_link">
                ${subscriptionData.name}
            </ycommerce:testId>
        </div>

        <div class="description">
            <ycommerce:testId code="subscription_description_label">
                ${subscriptionData.description}
            </ycommerce:testId>
        </div>


        <c:if test="${not empty billingActivities}">
            <table id="billingActivities" class="subscriptionsTable">
                <thead>
                    <tr>
                        <th><spring:theme code="text.account.subscription.billingActivity.billingPeriod" text="Billing Period"/></th>
                        <th><spring:theme code="text.account.subscription.billingActivity.billingDate" text="Billing Date"/></th>
                        <th><spring:theme code="text.account.subscription.billingActivity.paymentAmount" text="Payment Amount"/></th>
                        <th><spring:theme code="text.account.subscription.billingActivity.paymentStatus" text="Payment Status"/></th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${billingActivities}" var="billingActivity">

                        <c:url value="/my-account/subscription/billing-activity/download" var="billingActivityDetailUrl"/>

                        <tr>
                            <td>
                                <ycommerce:testId code="subscription_billing_activity_billingPeriod">
                                    ${billingActivity.billingPeriod}
                                </ycommerce:testId>
                            </td>
                            <td>
                                <ycommerce:testId code="subscription_billing_activity_billingDate">
                                    ${billingActivity.billingDate}
                                </ycommerce:testId>
                            </td>
                            <td>
                                <ycommerce:testId code="subscription_billing_activity_paymentAmount">
                                    ${billingActivity.paymentAmount}
                                </ycommerce:testId>
                            </td>
                            <td>
                                <ycommerce:testId code="subscription_billing_activity_paymentStatus">
                                    ${billingActivity.paymentStatus}
                                </ycommerce:testId>
                            <td>
                                <c:url value="/my-account/subscription/billing-activity/download" var="downloadBillingActivityDetailUrl" />
                                <form:form class="resume_subscription_form" id="resumeSubscriptionForm" action="${downloadBillingActivityDetailUrl}" method="get">
                                    <c:if test="${billingActivity.paymentStatus == 'Paid'}">
                                        <button type="submit" class="function_btn secondary">
                                            <spring:theme code="text.account.subscription.billingActivity.download" text="Download"/>
                                        </button>
                                        <input type="hidden" name="billingActivityId" value="${billingActivity.billingId}"/>
                                    </c:if>
                                </form:form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>

        </c:if>
        <c:if test="${empty billingActivities}">
            <p class="emptyMessage"><spring:theme code="text.account.subscriptions.noSubscriptions" text="You have no subscriptions"/></p>
        </c:if>

        <div class="accountAction">
            <c:url value="/my-account/subscription/${subscriptionData.id}" var="backToSubscriptionDetails"/>
            <a href="${backToSubscriptionDetails}" class="r_action_btn cancel"><spring:theme code="text.account.subscription.returnToSubscriptionDetails" text="Return To Subscription Details"/></a>
        </div>
	</div>
