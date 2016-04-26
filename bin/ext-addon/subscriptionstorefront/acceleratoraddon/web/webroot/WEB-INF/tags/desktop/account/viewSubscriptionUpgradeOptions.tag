<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="upgradeOptions" required="true" type="java.util.List" %>
<%@ attribute name="selectProduct" required="true" type="java.lang.Boolean" %>
<%@ attribute name="showButtons" required="true" type="java.lang.Boolean" %>
<%@ attribute name="horizontalLayout" required="true" type="java.lang.Boolean" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/addons/subscriptionstorefront/desktop/product"%>


<div class="headline"><spring:theme code="text.account.upgrade.options" text="Upgrade Options"/></div>

<div class="description"><spring:theme code="text.account.upgrade.options.description" text="View options"/></div>


<div class="item_container account-upgrade-subscription">
    <c:url value="/my-account/subscription/upgrade" var="upgradeSubscription" />
    <c:url value="" var="buttonTooltip" />
    <c:set var="isSubscriptionUpgradable" value="true"/>
    <c:if test="${subscriptionData.subscriptionStatus == 'cancelled'}">
        <c:set var="isSubscriptionUpgradable" value="false"/>
        <c:url value="text.account.subscription.cancelledSubscriptionNotUpgradable" var="buttonTooltip" />
    </c:if>
    <c:if test="${isSubscriptionUpgradable eq true}">
        <c:forEach items="${cartData.entries}" var="entry">
            <c:if test="${not empty entry.originalSubscriptionId and subscriptionData.id eq entry.originalSubscriptionId}">
                <c:set var="isSubscriptionUpgradable" value="false"/>
                <c:url value="text.account.subscription.alreadyUpgraded" var="buttonTooltip" />
            </c:if>
        </c:forEach>
    </c:if>

    <div class="tabs">
        <c:forEach items="${upgradeOptions}" var="upgradeOptionTab" varStatus="upgradeOptionTabCounter">
            <c:if test="${upgradeOptionTab.preselected}"></c:if>
            <h2 <c:if test="${upgradeOptionTab.preselected}"> id="preselected"</c:if>>${upgradeOptionTab.name}</h2>

            <div class="tabbody">

                <table class="account-upgrade-subscription-table">
                    <thead>
                        <tr>
                            <th>
                                <div class="top_plan">Current Subscription</div>
                                <div class="info-line">${subscriptionProductData.name}</div>
                            </th>
                            <th>
                               <div class="info-line center"> <spring:theme code="text.account.subscriptions.upgrade.change" text="Change"/></div>
                            </th>
                            <th>
                                <div class="top_plan">Upgrade option(s)</div>
                                <div class="info-line">${upgradeOptionTab.name}</div>
                            </th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>${subscriptionProductData.subscriptionTerm.billingPlan.billingTime.name}</td>
                            <td class="change_col">
                                <spring:theme code="product.list.viewplans.billingFrequency" text="Billing Frequency"/>
                            </td>
                            <td>${upgradeOptionTab.subscriptionTerm.billingPlan.billingTime.name}</td>
                        </tr>
                        <tr>
                            <td><product:subscriptionPricesLister subscriptionData="${subscriptionProductData}"/></td>
                            <td class="change_col">
                                <spring:theme code="product.list.viewplans.price" text="Price"/>
                            </td>
                            <td><product:subscriptionPricesLister subscriptionData="${upgradeOptionTab}"/></td>
                        </tr>
                        <tr>
                            <td>${subscriptionProductData.subscriptionTerm.termOfServiceNumber}&nbsp;${subscriptionProductData.subscriptionTerm.termOfServiceFrequency.name}</td>
                            <td class="change_col">
                                <spring:theme code="product.list.viewplans.termOfServiceFrequency" text="Term of service frequency"/>
                            </td>
                            <td>${upgradeOptionTab.subscriptionTerm.termOfServiceNumber}&nbsp;${upgradeOptionTab.subscriptionTerm.termOfServiceFrequency.name}</td>
                        </tr>
                        <c:catch>
                            <c:if test="${upgradeOptionTab.entitlements ne null}">
                        <tr>
                            <td><product:entitlementLister subscriptionData="${subscriptionProductData}"/></td>
                            <td class="change_col">
                                <spring:theme code="product.list.viewplans.entitlements" text="Included"/>
                            </td>
                            <td><product:entitlementLister subscriptionData="${upgradeOptionTab}"/></td>
                        </tr>
                            </c:if>
                        </c:catch>
                        <tr>
                            <td><product:usageChargesLister subscriptionData="${subscriptionProductData}"/></td>
                            <td class="change_col">
                                <spring:theme code="product.list.viewplans.usage.charges" text="Usage Charges"/>
                            </td>
                            <td><product:usageChargesLister subscriptionData="${upgradeOptionTab}"/></td>
                        </tr>
                        <tr>
                            <td>${subscriptionProductData.subscriptionTerm.termOfServiceRenewal.name}</td>
                            <td class="change_col">
                                <spring:theme code="text.account.subscription.renewalType" text="Renewal Type"/>
                            </td>
                            <td>${upgradeOptionTab.subscriptionTerm.termOfServiceRenewal.name}</td>
                        </tr>
                    </tbody>
                </table>

                <c:url value="/my-account/subscription/${subscriptionData.id}" var="myAccountSubscriptionDetailsUrl"/>
                <div class="actions">
                    <a class="r_action_btn cancel" href="${myAccountSubscriptionDetailsUrl}">
                        <spring:theme code="text.account.subscription.returnToCurrentPlan" text="Return to Current Plan"/>
                    </a>

                    <form:form class="upgrade_subscription_form right" id="upgradeSubscriptionForm_${subscriptionData.id}_${upgradeOptionTab.code}" action="${upgradeSubscription}" method="post">
                        <c:set var="buttonTypeUpgrade">button</c:set>
                        <c:if test="${ isSubscriptionUpgradable eq true }">
                            <c:set var="buttonTypeUpgrade">submit</c:set>
                        </c:if>

                        <button id="addUpgradeButton" type="${buttonTypeUpgrade}" title="<spring:theme code="${buttonTooltip}"/>" class="p_action_btn positive right" <c:if test="${isSubscriptionUpgradable eq false}">disabled="disabled"</c:if>>
                            <spring:theme code="text.account.subscription.upgradeSubscriptionNow" text="Upgrade Now"/>
                        </button>

                        <input type="hidden" name="productCode" value="${upgradeOptionTab.code}"/>
                        <input type="hidden" name="subscriptionId" value="${subscriptionData.id}"/>
                        <input type="hidden" name="originalOrderCode" value="${subscriptionData.orderNumber}"/>
                        <input type="hidden" name="originalEntryNumber" value="${subscriptionData.orderEntryNumber}"/>
                    </form:form>


                    <button class="function_btn secondary right viewPotentialUpgradeBillingDetails" data-url="billing-upgrade-details?subscriptionId=${subscriptionData.id}&upgradeId=${upgradeOptionTab.code}">
                        <spring:theme code="text.account.subscription.previewBillingChanges" text="View Billing Changes"/>
                    </button>
                </div>
            </div>
        </c:forEach>
    </div>
</div>