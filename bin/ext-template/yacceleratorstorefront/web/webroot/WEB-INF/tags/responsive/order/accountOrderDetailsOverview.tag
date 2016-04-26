<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="order" required="true" type="de.hybris.platform.commercefacades.order.data.OrderData" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>

<div class="account-order-header_data">
    <div class="col-md-5">
        <ycommerce:testId code="orderDetail_overviewOrderID_label">
            <spring:theme code="text.account.order.orderDetails.orderNumber" arguments="${orderData.code}"/>
        </ycommerce:testId>
    </div>
    <div class="col-md-7 text-right-md">
        <ycommerce:testId code="orderDetail_overviewOrderTotal_label">
            <span class="uppercase-sm">
                <spring:theme code="text.account.order.total"/>&nbsp;&ndash;&nbsp;
            </span>
            <format:price priceData="${order.totalPrice}"/>
        </ycommerce:testId>

        <span class="hidden-sm hidden-xs">&nbsp;&#x7C;</span>
        <br class="hidden-md hidden-lg"/>

        <c:if test="${not empty orderData.statusDisplay}">
            <ycommerce:testId code="orderDetail_overviewOrderStatus_label">
                <span class="uppercase-sm">
                    <spring:theme code="text.account.order.status.display.${orderData.statusDisplay}"/>
                </span>
            </ycommerce:testId>
        </c:if>

        <span>&nbsp;&ndash;&nbsp;</span>

        <ycommerce:testId code="orderDetail_overviewStatusDate_label">
            <fmt:formatDate value="${order.created}" dateStyle="medium" timeStyle="short" type="both"/>
        </ycommerce:testId>
    </div>
</div>
