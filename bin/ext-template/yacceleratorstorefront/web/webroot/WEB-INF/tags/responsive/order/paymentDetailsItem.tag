<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="order" required="true" type="de.hybris.platform.commercefacades.order.data.OrderData" %>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

	<div class="label-order">
		<spring:theme code="text.account.paymentType" text="Payment Type"/>
	</div>
	${fn:escapeXml(order.paymentInfo.cardTypeData.name)}
	${fn:escapeXml(order.paymentInfo.cardNumber)}
	

