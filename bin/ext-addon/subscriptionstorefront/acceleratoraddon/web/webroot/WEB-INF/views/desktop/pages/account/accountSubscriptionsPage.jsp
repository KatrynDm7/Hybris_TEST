<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/desktop/nav" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="breadcrumb" tagdir="/WEB-INF/tags/desktop/nav/breadcrumb" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

	<div id="globalMessages">
		<common:globalMessages/>
	</div>

    <div class="headline"><spring:theme code="text.account.subscriptions" text="Subscriptions"/></div>

	<c:if test="${not empty subscriptions}">
        <div class="description"><spring:theme code="text.account.subscriptions.manageSubscriptions" text="Manage your subscriptions"/></div>

        <table class="subscriptionsTable">
            <thead>
                <tr>
                    <th><spring:theme code="text.account.subscriptions.productName" text="Product Name"/></th>
                    <th><spring:theme code="text.account.subscriptions.startDate" text="Start Date"/></th>
                    <th><spring:theme code="text.account.subscriptions.endDate" text="End Date"/></th>
                    <th class="subscriptionsTableState"><spring:theme code="text.account.subscriptions.status" text="Status"/></th>
                    <th class="subscriptionsTablePayment"><spring:theme code="text.account.subscriptions.paymentDetails" text="Payment Details"/></th>
                    <th class="subscriptionsTableAction"><spring:theme code="text.account.orderHistory.actions" text="Actions"/></th>
                </tr>
            </thead>
            <tbody>

				<c:forEach items="${subscriptions}" var="subscription">

                    <c:url value="/my-account/subscription/${subscription.id}" var="myAccountSubscriptionDetailsUrl"/>
                    <c:url value="${subscription.productUrl}" var="productDetailsUrl"/>

                    <tr>
                        <td>
                            <ycommerce:testId code="subscriptions_productName_link">
                                <a href="${productDetailsUrl}">${subscription.name}</a>
                            </ycommerce:testId>
                        </td>
                        <td>
                            <ycommerce:testId code="subscriptions_startDate_label">
                                <fmt:formatDate value="${subscription.startDate}" dateStyle="long" timeStyle="short" type="date"/>
                            </ycommerce:testId>
                        </td>
                        <td>
                            <ycommerce:testId code="subscriptions_endDate_label">
                                <fmt:formatDate value="${subscription.endDate}" dateStyle="long" timeStyle="short" type="date"/>
                            </ycommerce:testId>
                        </td>
                        <td>
                            <ycommerce:testId code="subscriptions_status_label">
                                ${subscription.subscriptionStatus}
                            </ycommerce:testId>
                        </td>
                        <td>
                            <ycommerce:testId code="subscriptions_status_label">
                                <c:if test= "${not empty paymentInfoMap[subscription.paymentMethodId]}">
                                    <c:set value="${paymentInfoMap[subscription.paymentMethodId]}" var="paymentMethod"></c:set>
                                    <c:set value="${fn:length(paymentMethod.expiryMonth) lt 2 ? '0'.concat(paymentMethod.expiryMonth) : paymentMethod.expiryMonth}" var="paymentMethodExpiryMonth"></c:set>
                                    <spring:theme code="text.account.subscriptions.payment.details" arguments="${fn:substring(paymentMethod.cardTypeData.name,0,4)},${fn:replace(paymentMethod.cardNumber,'*','')},${paymentMethodExpiryMonth},${paymentMethod.expiryYear}" text=""/>
                                </c:if>
                            </ycommerce:testId>
                        </td>
                        <td>
                            <a href="${myAccountSubscriptionDetailsUrl}" class="function_btn">
                                <spring:theme code="text.manage" text="Manage"/>
                            </a>
                            <c:if test="${fn:toUpperCase(subscription.subscriptionStatus) ne 'PAUSED' and fn:toUpperCase(subscription.subscriptionStatus) ne 'CANCELLED'
                            and !empty subscriptionFacade.getUpsellingOptionsForSubscription(subscription.productCode)}">
                                <c:url value="/my-account/subscription/upgrades-comparison?subscriptionId=${subscription.id}" var="upgradeSubscriptionComparisionUrl"/>
                                <a type="button" href="${upgradeSubscriptionComparisionUrl}" class="button secondary">
                                    <spring:theme code="text.account.subscription.upgradeSubscription" text="Upgrade Subscription"/>
                                </a>
                            </c:if>
                            <c:if test="${fn:toUpperCase(subscription.subscriptionStatus) eq 'PAUSED' and fn:toUpperCase(subscription.subscriptionStatus) ne 'CANCELLED'}">
                                <c:url value="/my-account/subscription/change-state" var="changeSubscriptionStateUrl" />
                                <form:form class="resume_subscription_form" id="resumeSubscriptionForm" action="${changeSubscriptionStateUrl}" method="post">
                                    <button type="submit" class="positive">
                                        <spring:theme code="text.account.subscription.resumeSubscription" text="Resume Subscription"/>
                                    </button>
                                    <input type="hidden" name="newState" value="ACTIVE"/>
                                    <input type="hidden" name="subscriptionId" value="${subscription.id}"/>
                                </form:form>
                            </c:if>

                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </c:if>

    <c:if test="${empty subscriptions}">
        <p class="emptyMessage"><spring:theme code="text.account.subscriptions.noSubscriptions" text="You have no subscriptions"/></p>
    </c:if>
