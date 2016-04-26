<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="cartData" required="true" type="de.hybris.platform.commercefacades.order.data.CartData"%>
<%@ attribute name="showTaxEstimate" required="false" type="java.lang.Boolean"%>
<%@ attribute name="showTax" required="false" type="java.lang.Boolean"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/desktop/cart"%>
<table id="orderTotals">
	<thead>
		<tr>
			<th colspan="${fn:length(cartData.orderPrices) + 1}"><spring:theme code="order.order.totals" /></th>
		</tr>
	</thead>
	<tfoot>
        <tr>
            <th>&nbsp;</th>
            <c:forEach items="${cartData.orderPrices}" var="tpentry" varStatus="rowCounter">
                <th scope="col">${tpentry.billingTime.name}</th>
            </c:forEach>
        </tr>
	</tfoot>
	<tbody>
		<tr>
			<td class="cartBundlePackage"><spring:theme code="basket.page.totals.subtotal" /></td>
			<ycommerce:testId code="Order_Totals_Subtotal">
				<c:forEach items="${cartData.orderPrices}" var="entry">
					<td><format:price priceData="${entry.subTotal}" /></td>
				</c:forEach>
			</ycommerce:testId>
		</tr>

		<%--
	<c:if test="${cartData.totalDiscounts.value > 0}">
	 --%>
		<tr>
			<td class="cartBundlePackage"><spring:theme code="basket.page.totals.savings" /></td>
			<ycommerce:testId code="Order_Totals_Savings">
				<c:forEach var="entry" items="${cartData.orderPrices}">
					<td><format:price priceData="${entry.totalDiscounts}" /></td>
				</c:forEach>
			</ycommerce:testId>
		</tr>
		<%--
	</c:if>
	 --%>

		<%--<c:if test="${not empty cartData.deliveryCost}"> --%>
		<tr>
			<td class="cartBundlePackage"><spring:theme code="basket.page.totals.delivery" /></td>
			<c:forEach items="${cartData.orderPrices}" var="entry">
				<td><format:price priceData="${entry.deliveryCost}" displayFreeForZero="TRUE" /></td>
			</c:forEach>
		</tr>
		<%--
	</c:if>
	 --%>

		<%--
	<c:if test="${cartData.net && cartData.totalTax.value > 0 && showTax}">
 --%>
		<tr>
			<td class="cartBundlePackage"><spring:theme code="basket.page.totals.grossTax" /></td>
			<c:forEach items="${cartData.orderPrices}" var="entry">
				<td><format:price priceData="${entry.totalTax}" displayFreeForZero="TRUE" /></td>
			</c:forEach>
		</tr>
		<%--
	</c:if>
 --%>

        <tr class="cartTotal">
            <td class="cartBundlePackage"></td>
            <ycommerce:testId code="cart_totalPrice_label">
                <c:forEach items="${cartData.orderPrices}" var="entry">
                    <td><format:price priceData="${entry.totalPrice}" /></td>
                </c:forEach>
            </ycommerce:testId>
        </tr>

		<cart:taxExtimate cartData="${cartData}" showTaxEstimate="${showTaxEstimate}" />
	</tbody>
</table>
