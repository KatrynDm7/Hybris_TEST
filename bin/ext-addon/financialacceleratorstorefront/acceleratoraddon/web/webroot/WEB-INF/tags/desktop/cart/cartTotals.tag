<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="cartData" required="true" type="de.hybris.platform.commercefacades.order.data.CartData" %>
<%@ attribute name="showTaxEstimate" required="false" type="java.lang.Boolean" %>
<%@ attribute name="showTax" required="false" type="java.lang.Boolean" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div id="orderTotals">
    <div>
        <spring:theme code="basket.page.totals.total"/>&nbsp;
        <span class="price">
        <ycommerce:testId code="cart_totalPrice_label">
            <c:choose>
                <c:when test="${showTax}">
                    <format:price priceData="${cartData.totalPriceWithTax}"/>
                </c:when>
                <c:otherwise>
                    <format:price priceData="${cartData.totalPrice}"/>
                </c:otherwise>
            </c:choose>
        </ycommerce:testId>
        </span>
    </div>
</div>
