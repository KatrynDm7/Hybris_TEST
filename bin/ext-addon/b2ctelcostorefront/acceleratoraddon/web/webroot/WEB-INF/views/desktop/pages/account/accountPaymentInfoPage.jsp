	<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

	<div class="headline">
		<spring:theme code="text.account.paymentDetails" text="Payment Details"/>
	</div>
	<div class="description">
		<spring:theme code="text.account.paymentDetails.managePaymentDetails" text="Manage your saved payment details."/>
	</div>
	<c:choose>
		<c:when test="${not empty paymentInfoData}">
            <table class="paymentListTable">
                <thead>
                    <tr>
                        <th><spring:theme code="text.account.paymentDetails.paymentCard" text="Payment card"/></th>
                        <th><spring:theme code="text.account.paymentDetails.billingAddress" text="Billing address"/></th>
                        <th><spring:theme code="text.default" text="Default"/></th>
                        <th><spring:theme code="text.account.paymentDetails.actions" text="Actions"/></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${paymentInfoData}" var="paymentInfo">
                        <tr class="paymentItem">
                            <td>
                                <ul>
                                    <li>${fn:escapeXml(paymentInfo.cardNumber)}</li>
                                    <li>${fn:escapeXml(paymentInfo.cardType)}</li>
                                    <li>
                                        <fmt:formatNumber minIntegerDigits="2" value="${paymentInfo.expiryMonth}" var="expiryMonth"/>
                                        <spring:theme code="text.expires" text="Expires"/>&nbsp;
                                        ${fn:escapeXml(expiryMonth)}/${fn:escapeXml(paymentInfo.expiryYear)}
                                    </li>
                                </ul>
                            </td>
                            <td>
                                <ul>
                                    <li>
                                        <c:out value="${fn:escapeXml(paymentInfo.billingAddress.title)} ${fn:escapeXml(paymentInfo.billingAddress.firstName)} ${fn:escapeXml(paymentInfo.billingAddress.lastName)}"/>
                                    </li>
                                    <li>${fn:escapeXml(paymentInfo.billingAddress.line1)}</li>
                                    <li>${fn:escapeXml(paymentInfo.billingAddress.line2)}</li>
                                    <li>${fn:escapeXml(paymentInfo.billingAddress.town)}</li>
                                    <li>${fn:escapeXml(paymentInfo.billingAddress.postalCode)}</li>
                                    <li>${fn:escapeXml(paymentInfo.billingAddress.country.name)}</li>
                                </ul>
                            </td>
                            <td>
                                <c:if test="${not paymentInfo.defaultPaymentInfo}">
                                    <c:url value="/my-account/my-payment-details/set-default" var="setDefaultPaymentActionUrl"/>
                                    <form:form id="setDefaultPaymentDetails${paymentInfo.id}" action="${setDefaultPaymentActionUrl}" method="post">
                                        <input type="hidden" name="paymentInfoId" value="${paymentInfo.id}"/>
                                        <button type="submit" class="submitSetDefault secondary" id="${paymentInfo.id}" href="#">
                                            <spring:theme code="text.setDefault" text="Set as default"/>
                                        </button>
                                    </form:form>
                                </c:if>

                                <c:if test="${paymentInfo.defaultPaymentInfo}">
                                    <spring:theme code="text.account.paymentDetails.default" text="Default"/>
                                </c:if>
                            </td>
                            <td>
                                <c:if
                                    test="${subscriptionsPerPaymentMethod[paymentInfo.id] gt '0'}">
                                    <c:url value="/my-account/my-payment-details/manage?paymentInfoId=${paymentInfo.id}" var="manageSubscriptionsUrl" />
                                    <a href="${manageSubscriptionsUrl}" class="manageSubscriptions">
                                        <spring:theme code="text.account.paymentDetails.manageSubscriptions" text="Manage Subscriptions" />
                                    </a>
                                </c:if>
                                <div class="clear"></div>
                                <c:if test="${subscriptionsPerPaymentMethod[paymentInfo.id] gt '0'}">
                                <c:url value="/my-account/my-payment-details/payment-method-subscriptions?paymentInfoId=${paymentInfo.id}" var="manageSubscriptionsUrl"/>
                                    <a href="#" id="editWithSubscriptions-account" class="function_btn" data-url="${manageSubscriptionsUrl}">
                                        <spring:theme code="text.edit" text="Edit"/>
                                    </a>
                                </c:if>
                                <c:if test="${subscriptionsPerPaymentMethod[paymentInfo.id] eq '0'}">
                                    <c:url value="/my-account/my-payment-details/edit?paymentInfoId=${paymentInfo.id}&targetArea=accountArea" var="continueToEditPaymentDetailsUrl" />
                                    <a href="${continueToEditPaymentDetailsUrl}" class="function_btn">
                                        <spring:theme code="text.edit" text="Edit"/>
                                    </a>
                                </c:if>

                                <c:url value="/my-account/my-payment-details/check-remove?paymentInfoId=${paymentInfo.id}" var="removePaymentActionUrl"/>
                                <form:form id="removePaymentDetails${paymentInfo.id}">
                                    <input type="hidden" name="paymentInfoId" value="${paymentInfo.id}"/>
                                    <a href="#" class="checkSubmitRemove-account remove" id="${paymentInfo.id}" data-url="${removePaymentActionUrl}">
                                        <spring:theme code="text.remove" text="Remove"/>
                                    </a>
                                </form:form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
		</c:when>
		<c:otherwise>
			<p class="emptyMessage"><spring:theme code="text.account.paymentDetails.noPaymentInformation" text="No Saved Payment Details"/></p>
		</c:otherwise>
	</c:choose>
	<div class="accountAction">
		<c:url value="/my-account/my-payment-details/add" var="addPaymentMethodUrl" />
		<button type="button" onclick="window.location='${addPaymentMethodUrl}'" class="positive">
	                 <spring:theme code="text.account.paymentDetails.addNewPaymentDetails" text="Add New Payment Details"/>
		</button>
	</div>
	
