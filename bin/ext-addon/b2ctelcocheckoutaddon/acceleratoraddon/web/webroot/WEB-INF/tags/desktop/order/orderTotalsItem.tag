<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="order" required="true" type="de.hybris.platform.commercefacades.order.data.OrderData" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="/WEB-INF/tld/addons/b2ctelcostorefront/ycommercetags.tld" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/addons/b2ctelcocheckoutaddon/desktop/product" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>

<%@ attribute name="containerCSS" required="false" type="java.lang.String" %>

<div class="span-11 last right cartOrderTotals">

    <table id="orderTotals">
        <thead>
            <tr>
                <th colspan="${fn:length(order.orderPrices) + 1}"><spring:theme code="text.account.order.orderTotals" text="Order Totals"/></th>
            </tr>
        </thead>
        <tfoot>
            <tr>
                <th>&nbsp;</th>
                <c:forEach items="${order.orderPrices}" var="tpentry" varStatus="rowCounter">
                    <th class="cart-bundle-itemPrice" scope="col">
                        <c:choose>
                            <c:when test="${not empty tpentry.billingTime.nameInOrder}">
                                ${tpentry.billingTime.nameInOrder}
                            </c:when>
                            <c:otherwise>
                                ${tpentry.billingTime.name}
                            </c:otherwise>
                        </c:choose>
                    </th>
                </c:forEach>
            </tr>
        </tfoot>
        <tbody>

            <tr>
                <td class="cartBundlePackage">
                    <spring:theme code="text.account.order.subtotal" text="Subtotal:"/>
                </td>
                <ycommerce:testId code="Order_Totals_Subtotal">
                    <c:forEach items="${order.orderPrices}" var="entry">
                     <td class="cart-bundle-itemPrice">
                        <format:price priceData="${entry.subTotal}"/>
                    </td>
                    </c:forEach>
                </ycommerce:testId>
            </tr>

            <tr>
                <td class="cartBundlePackage"> <spring:theme code="text.account.order.savings" text="Savings:"/>
                </td>
                <ycommerce:testId code="Order_Totals_Savings">
                    <c:forEach var="entry" items="${order.orderPrices}">
                        <td>
                            <format:price priceData="${entry.totalDiscounts}"/>
                        </td>
                    </c:forEach>
                </ycommerce:testId>
            </tr>

            <tr>
                <td class="cartBundlePackage">
                    <spring:theme code="text.account.order.delivery" text="Delivery:"/>
                </td>
                <c:forEach items="${order.orderPrices}" var="entry">
                     <td>
                        <format:price priceData="${entry.deliveryCost}" displayFreeForZero="TRUE"/>
                    </td>
                </c:forEach>
            </tr>

            <c:if test="${not orderData.net}">
                <tr>
                    <td class="cartBundlePackage" scope="row">
                        <spring:theme code="text.account.order.includesTax"/>
                    </td>
                    <c:forEach items="${order.orderPrices}" var="entry">
                         <td>
                            <format:price priceData="${entry.totalTax}" displayFreeForZero="TRUE"/>
                        </td>
                    </c:forEach>

                </tr>
            </c:if>

            <tr class="cartTotal">
             <td class="cartBundlePackage"> </td>
             <ycommerce:testId code="cart_totalPrice_label">
                <c:forEach items="${order.orderPrices}" var="entry">
                    <td>
                        <format:price priceData="${entry.totalPrice}"/>
                    </td>
                </c:forEach>

                </ycommerce:testId>
            </tr>

            <c:if test="${orderData.net}">
                <tr>
                    <td>
                        <spring:theme code="basket.page.totals.netTax"/>
                    </td>
                    <c:forEach items="${orderData.orderPrices}" var="entry">
                        <td class="rightAlign">
                            <format:price priceData="${entry.totalTax}" displayFreeForZero="TRUE"/>
                        </td>
                    </c:forEach>
                </tr>
            </c:if>
        </tbody>
    </table>
</div>