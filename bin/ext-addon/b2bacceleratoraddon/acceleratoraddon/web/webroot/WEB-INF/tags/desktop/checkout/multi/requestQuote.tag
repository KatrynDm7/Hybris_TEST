<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template" %>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/desktop/formElement"%>

<script type="text/javascript"> // set vars
	var cartDataQuoteAllowed = ${cartData.quoteAllowed};
	var negotiateQuote = ${placeOrderForm.negotiateQuote};
</script>

<div id="requestQuote" class="clearfix" style="display:none">
	<div class="headline"><spring:theme code="checkout.summary.requestQuote.quoteReason"/></div>

	<form:textarea cssClass="text" id="quoteRequestDescription" path="quoteRequestDescription" maxlength="200" />

	<div class="form-actions clearfix" style="clear:both;">
		<form:input type="hidden" class="requestQuoteClass" path="negotiateQuote"/>
		<button type="submit" class="positive right" id="placeRequestQuote"><spring:theme code="checkout.summary.requestQuote.proceed"/></button>
		<button type="button" class="negative right" id="cancelRequestQuote"><spring:theme code="checkout.summary.requestQuote.cancel"/></button>
	</div>
</div>