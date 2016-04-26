<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="order" required="true" type="de.hybris.platform.commercefacades.order.data.AbstractOrderData"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div>
	<ul>
		<li><spring:theme code="checkout.multi.summary.orderPlacedBy"/>&nbsp;<spring:theme code="text.company.user.${order.b2bCustomerData.titleCode}.name" text="N/A"/>&nbsp;${fn:escapeXml(order.b2bCustomerData.firstName)}&nbsp;${fn:escapeXml(order.b2bCustomerData.lastName)}</li>
		<li><spring:theme code="checkout.multi.purchaseOrderNumber.label"/>&nbsp;${order.purchaseOrderNumber}</li>
		<spring:theme code="sap.paymenttype.${order.paymentType.code}" var="displayName"/>
		<li><spring:theme code="sap.custom.order.paymentType" arguments="${displayName}"/></li>
	</ul>
</div>
