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
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="breadcrumb" tagdir="/WEB-INF/tags/desktop/nav/breadcrumb" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/addons/subscriptionstorefront/desktop/product" %>

		<c:url value="${subscriptionData.productUrl}" var="productDetailsUrl"/>
		<c:url value="/my-account/order/${subscriptionData.orderNumber}" var="orderDetailsUrl"/>		
		
        <ycommerce:testId code="subscription_productName_link">
            <a href="${productDetailsUrl}">
                <div class="headline">${subscriptionData.name}</div>
            </a>
        </ycommerce:testId>

        <c:if test="${fn:toUpperCase(subscriptionData.subscriptionStatus) ne 'CANCELLED' and upgradable}">
            <c:url value="/my-account/subscription/upgrades-comparison?subscriptionId=${subscriptionData.id}" var="upgradeSubscriptionComparisionUrl"/>
            <button type="button" onclick="window.location='${upgradeSubscriptionComparisionUrl}'" class="positive right p_action_btn"><spring:theme code="text.account.subscription.upgradeSubscriptionDetail" text="Upgrade Options"/></button>
        </c:if>

        <div class="description">
            <ycommerce:testId code="subscription_description_label">
                ${subscriptionData.description}
            </ycommerce:testId>
        </div>

        <div class="manageSubscription">
            <h3><spring:theme code="text.account.subscription.detail" text="Subscription detail"/></h3>
            <table id="subscription_details" class="manageSubscriptionTable">
                <tr>
                    <td>
                        <span><spring:theme code="text.account.subscription.billingFrequency" text="Billing Frequency"/>:</span>
                    </td>
                    <td>
                        <ycommerce:testId code="subscription_billingFrequency_label">
                            ${subscriptionData.billingFrequency}
                        </ycommerce:testId>
                    </td>
                </tr>
                <tr>
                    <td>
                        <span><spring:theme code="product.list.viewplans.price" text="Price"/>:</span>
                    </td>
                    <td>
                        <span class="bold_text"><product:subscriptionPricesLister subscriptionData="${subscriptionProductData}"/></span>
                    </td>
                </tr>
                <tr>
                    <td>
                        <span><spring:theme code="text.account.subscription.contractDuration" text="Contract Duration"/>:</span>
                    </td>
                    <td>
                        <ycommerce:testId code="subscription_contractDuration_label">
                            ${subscriptionData.contractDuration}&nbsp;${subscriptionData.contractFrequency}
                        </ycommerce:testId>
                    </td>
                </tr>

                <tr>
                    <td>
                        <span><spring:theme code="text.account.subscription.Cancellable" text="Cancellable"/>:</span>
                    </td>
                    <td>
                        <ycommerce:testId code="subscription_status_label">
                            <c:choose>
                                <c:when test="${subscriptionData.cancellable}">
                                    <spring:theme code="text.account.subscription.cancellable.yes" text="yes"/>
                                </c:when>
                                <c:otherwise>
                                    <spring:theme code="text.account.subscription.cancellable.no" text="no"/>
                                </c:otherwise>
                            </c:choose>
                        </ycommerce:testId>
                    </td>

                </tr>
                <c:catch>
                    <c:if test="${subscriptionProductData.entitlements ne null}">

                <tr>
                    <td>
                        <span><spring:theme code="product.list.viewplans.entitlements" text="Included"/>:</span>
                    </td>
                    <td>
                        <span><product:entitlementLister subscriptionData="${subscriptionProductData}"/></span>
                    </td>
                </tr>
                <tr>
                    <td>
                        <span><spring:theme code="product.list.viewplans.usage.charges" text="Usage Charges"/>:</span>
                    </td>
                    <td>
                        <span><product:usageChargesLister subscriptionData="${subscriptionProductData}"/></span>
                    </td>
                </tr>
                    </c:if>
                </c:catch>
            </table>
        </div>

        <div class="manageSubscription">
            <h3><spring:theme code="text.account.subscription.manageSubscription" text="Manage this subscription"/></h3>
            <table id="subscription_manage" class="manageSubscriptionTable">
                <c:if test="${fn:toUpperCase(subscriptionData.subscriptionStatus) ne 'CANCELLED' }">
                <tr>
                    <td>
                        <span>
                            <c:url value="/my-account/subscription/change-state" var="changeSubscriptionStateUrl" />
                            <c:if test="${fn:toUpperCase(subscriptionData.subscriptionStatus) eq 'PAUSED'}">
                                <form:form class="resume_subscription_form" id="resumeSubscriptionForm" action="${changeSubscriptionStateUrl}" method="post">
                                    <button type="submit" class="r_function_btn negative play">
                                        <spring:theme code="text.account.subscription.resumeSubscription" text="Resume Subscription"/>
                                    </button>
                                    <input type="hidden" name="newState" value="ACTIVE"/>
                                    <input type="hidden" name="subscriptionId" value="${subscriptionData.id}"/>
                                </form:form>
                            </c:if>

                            <c:if test="${fn:toUpperCase(subscriptionData.subscriptionStatus) eq 'ACTIVE'}">
                            <form:form class="pause_subscription_form" id="pauseSubscriptionForm" action="${changeSubscriptionStateUrl}" method="post">
                                <button type="submit" class="r_function_btn negative pause">
                                    <spring:theme code="text.account.subscription.pauseSubscription" text="Pause Subscription"/>
                                </button>
                                <input type="hidden" name="newState" value="PAUSED"/>
                                <input type="hidden" name="subscriptionId" value="${subscriptionData.id}"/>
                            </form:form>
                            </c:if>
                        </span>
                    </td>
                    <td>
                        <c:if test="${subscriptionData.cancellable}">
                            <div>
                                <c:url value="/my-account/subscription/cancel/${subscriptionData.id}" var="cancelUrl"/>
                                <button type="button" data-url="${cancelUrl}" class="function_btn js-cancel-subscription negative cancel"><spring:theme code="text.account.subscription.cancelSubscription" text="Cancel"/></button>
                                <div class="hidden">
                                    <div id="cancel-subscription-confirm" >
                                        <div class="small_popup_headline"><spring:theme code="text.account.subscription.confirm.cancellation" text="Confirm Cancellationa"/></div>
                                        <div class="small_popup_content">
                                            <p><spring:theme code="text.account.subscription.cancellation.message" text="Are you sure you would like to cancel this subscriptiona"/></p>
                                            <div class="actions">
                                                <a href="#" class="r_action_btn cancel"><spring:theme code="text.account.subscription.cancellable.no" text="No"/></a>
                                                <a href="${cancelUrl}" class="confirm positive button"><spring:theme code="text.account.subscription.cancellable.yes" text="Yes"/></a>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:if>
                    </td>
                </tr>
                </c:if>
                <tr>
                    <td>
                        <span><spring:theme code="text.account.subscription.status" text="Status"/>:</span>
                    </td>
                    <td>
                        <ycommerce:testId code="subscription_status_label">
                            ${subscriptionData.subscriptionStatus}
                        </ycommerce:testId>
                    </td>
                </tr>
                <c:if test="${fn:toUpperCase(subscriptionData.subscriptionStatus) ne 'CANCELLED' }">
                <tr>
                    <td colspan="2">
                        <span>
                            <c:url value="/my-account/subscription/set-autorenewal-status" var="setAutorenewStatusUrl" />
                            <c:if test="${!subscriptionData.renewalType}">
                                <form:form class="activate_autorenewal_form" id="activateAutorenewalForm" action="${setAutorenewStatusUrl}" method="post">
                                    <button type="submit" class="r_function_btn negative play">
                                        <spring:theme code="text.account.subscription.activateAutorenew" text="Activate Auto-Renew"/>
                                    </button>
                                    <input type="hidden" name="autorenew" value="true"/>
                                    <input type="hidden" name="subscriptionId" value="${subscriptionData.id}"/>
                                </form:form>
                            </c:if>
                            <c:if test="${subscriptionData.renewalType}">
                                <form:form class="deactivate_autorenewal_form" id="deactivateAutorenewalForm" action="${setAutorenewStatusUrl}" method="post">
                                    <button type="submit" class="r_function_btn negative stop">
                                        <spring:theme code="text.account.subscription.deactivateAutorenew" text="Deactivate Auto-Renew"/>
                                    </button>
                                    <input type="hidden" name="autorenew" value="false"/>
                                    <input type="hidden" name="subscriptionId" value="${subscriptionData.id}"/>
                                </form:form>
                            </c:if>
                        </span>
                    </td>
                </tr>

                <tr>
                    <td>
                        <span><spring:theme code="text.account.subscription.renewalType" text="Renewal Type"/>:</span>
                    </td>
                    <td>
                        <ycommerce:testId code="subscription_renewalType_label">
                            ${subscriptionData.renewalType}
                        </ycommerce:testId>
                    </td>
                </tr>

                <tr>
                    <td>
                        <span><spring:theme code="text.account.subscription.endDate" text="End Date"/>:</span>
                    </td>
                    <td>
                        <ycommerce:testId code="subscription_endDate_label">
                            <fmt:formatDate value="${subscriptionData.endDate}" dateStyle="long" timeStyle="short" type="both"/>
                        </ycommerce:testId>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <c:url value="/my-account/subscription/extend-term-duration" var="extendSubscriptionTermDuration" />
                        <form:form class="extend_subscription_form" id="extendSubscriptionForm" action="${extendSubscriptionTermDuration}" method="post">
                            <select id="contractDurationExtensionOptions" name="contractDurationExtension" >
                                <c:forEach items="${extensionOptions}" var="extensionOption" varStatus="extensionOptionCounter">
                                    <option value="${extensionOption.code}">${extensionOption.name}</option>
                                </c:forEach>
                            </select>
                            <button type="submit" class="function_btn secondary">
                                <spring:theme code="text.account.subscription.updateTermDuration" text="Update"/>
                            </button>
                            <input type="hidden" name="subscriptionId" value="${subscriptionData.id}"/>
                        </form:form>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <c:url value="/my-account/subscription/billing-activity/payment-method/replace" var="replaceSubscriptionPaymentMethodUrl" />
                        <form:form class="update_paymentmethod_form" id="updatePaymentmethodForm" action="${replaceSubscriptionPaymentMethodUrl}" method="post">
                            <select name="paymentMethodId" id="paymentMethods" <c:if test="${fn:length(paymentInfos) lt 2}">disabled</c:if>>
                                <c:forEach items="${paymentInfos}" var="paymentInfo">
                                    <option value="${paymentInfo.subscriptionId}" <c:if test="${paymentInfo.subscriptionId eq subscriptionData.paymentMethodId}">selected</c:if>><spring:theme code="text.account.subscription.paymentMethod" text="Update" arguments="${paymentInfo.cardTypeData.name},${fn:replace(paymentInfo.cardNumber,'****','**** ')},${paymentInfo.expiryMonth},${paymentInfo.expiryYear}"/></option>
                                </c:forEach>
                            </select>
                            <button type="submit" class="function_btn secondary" <c:if test="${fn:length(paymentInfos) lt 2}">disabled</c:if>>
                                <spring:theme code="text.account.subscription.updatePaymentMethod" text="Update"/>
                            </button>
                            <input type="hidden" name="effectiveFrom" value="NOW"/>
                            <input type="hidden" name="subscriptionId" value="${subscriptionData.id}"/>
                        </form:form>
                    </td>
                </tr>
                </c:if>
            </table>

            <div class="subscriptionActivity">
                <h3><spring:theme code="text.account.subscription.activity" text="Activity"/></h3>
                <div class="order_number">
                    <span><spring:theme code="text.account.subscription.orderNumber" text="Order Number"/>:</span>
                    <span><ycommerce:testId code="subscription_orderNumber_link">
                        <a href="${orderDetailsUrl}">${subscriptionData.orderNumber}</a>
                        </ycommerce:testId>
                    </span>
                </div>
            </div>
            <table id="subscription_activity" class="manageSubscriptionTable">
                <tr>
                    <td colspan="2">
                        <c:url value="/my-account/subscription/billing-activity" var="subscriptionBillingActivityUrl" />
                        <form:form class="view_billing_activity_form" id="viewBillingActivityForm" action="${subscriptionBillingActivityUrl}" method="get">
                            <button type="submit" class="function_btn negative">
                                <spring:theme code="text.account.subscription.viewBillingActivity" text="View Billing Activity"/>
                            </button>
                            <input type="hidden" name="subscriptionId" value="${subscriptionData.id}"/>
                        </form:form>
                    </td>
                </tr>
                <c:if test="${fn:toUpperCase(subscriptionData.subscriptionStatus) eq 'CANCELLED'}">
                <tr>
                    <td>
                        <span><spring:theme code="text.account.subscription.cancelledDate" text="Cancelled Date"/>:</span>
                    </td>
                    <td>
                        <ycommerce:testId code="subscription_cancelledDate_label">
                            <fmt:formatDate value="${subscriptionData.cancelledDate}" dateStyle="long" timeStyle="short" type="both"/>
                        </ycommerce:testId>
                    </td>
                </tr>
                </c:if>
                <tr>
                    <td>
                        <span><spring:theme code="text.account.subscription.startDate" text="Start Date"/>:</span>
                    </td>
                    <td>
                        <ycommerce:testId code="subscription_startDate_label">
                            <fmt:formatDate value="${subscriptionData.startDate}" dateStyle="long" timeStyle="short" type="both"/>
                        </ycommerce:testId>
                    </td>
                </tr>
                <tr>
                    <td>
                        <span><spring:theme code="text.account.subscription.placedOn" text="Placed On"/>:</span>
                    </td>
                    <td>
                        <ycommerce:testId code="subscription_placedOn_label">
                            <fmt:formatDate value="${subscriptionData.placedOn}" dateStyle="long" timeStyle="short" type="both"/>
                        </ycommerce:testId>
                    </td>
                </tr>
            </table>
        </div>
        <div class="clear"></div>
        <div class="manageSubscriptionActions">
            <c:url value="/my-account/subscription" var="backToSubscriptions"/>
            <a href="${backToSubscriptions}" class="r_action_btn"><spring:theme code="text.account.subscription.returntosubscriptions" text="Return To Subscriptions"/></a>
        </div>