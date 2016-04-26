<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div class="summaryPaymentType clearfix">
	<ycommerce:testId code="paymentType_text">
		<div class="column append-1">
			<strong><spring:theme code="checkout.multi.paymentType" htmlEscape="false"/></strong>
			<ul>
				<li>${fn:escapeXml(cartData.paymentType.displayName)}</li>
			</ul>
		</div>
	</ycommerce:testId>
	<ycommerce:testId code="checkout_changePaymentType_element">
		<c:url value="/checkout/multi/payment-type/choose" var="editPaymentTypeUrl" />
		<a href="${editPaymentTypeUrl}" class="button positive editButton"><spring:theme code="checkout.summary.edit"/></a>
	</ycommerce:testId>
</div>