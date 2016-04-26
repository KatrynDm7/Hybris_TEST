<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div class="summaryPayment clearfix">
    <ycommerce:testId code="checkout_paymentDetails_text">
		<div class="column append-1">
			<strong><spring:theme code="checkout.summary.paymentMethod.header" htmlEscape="false"/></strong>
			<ul>
				<li><spring:theme code="checkout.multi.summary.orderPlacedBy"/>:&nbsp;<spring:theme code="text.company.user.${cartData.b2bCustomerData.titleCode}.name" text="N/A"/>&nbsp;${fn:escapeXml(cartData.b2bCustomerData.firstName)}&nbsp;${fn:escapeXml(cartData.b2bCustomerData.lastName)}</li>
				<c:if test="${(not empty cartData.costCenter) and (not empty cartData.costCenter.code)}">
					<li><spring:theme code="checkout.multi.costCenter.label" htmlEscape="false"/>:&nbsp;${fn:escapeXml(cartData.costCenter.name)}</li>
				</c:if>
				<c:choose>
					<c:when test="${not empty cartData.purchaseOrderNumber}">
						<li><spring:theme code="checkout.multi.purchaseOrderNumber.label" htmlEscape="false"/>:&nbsp;${fn:escapeXml(cartData.purchaseOrderNumber)}</li>
					</c:when>
					<c:otherwise>
						<li><spring:theme code="checkout.multi.purchaseOrderNumber.label" htmlEscape="false"/>:&nbsp;-</li>
					</c:otherwise>
				</c:choose>
			</ul>
		</div>
    </ycommerce:testId>
    <ycommerce:testId code="checkout_changePayment_element">
	    <c:url value="/checkout/multi/payment-type/choose" var="choosePaymentTypeUrl"/>
        <a href="${choosePaymentTypeUrl}" class="button positive editButton"><spring:theme code="checkout.summary.edit"/></a>
    </ycommerce:testId>
</div>