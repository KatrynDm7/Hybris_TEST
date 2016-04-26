<%@ attribute name="cartData" required="true" type="de.hybris.platform.commercefacades.order.data.CartData" %>
<%@ attribute name="showShipDeliveryEntries" required="false" type="java.lang.Boolean" %>
<%@ attribute name="showPickupDeliveryEntries" required="false" type="java.lang.Boolean" %>
<%@ attribute name="showTax" required="false" type="java.lang.Boolean" %>
<%@ attribute name="showBtn" required="false" type="java.lang.Boolean" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="checkout" tagdir="/WEB-INF/tags/addons/b2ctelcocheckoutaddon/desktop/checkout" %>
<%@ taglib prefix="multi-checkout" tagdir="/WEB-INF/tags/addons/b2ctelcocheckoutaddon/desktop/checkout/multi" %>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/addons/b2ctelcostorefront/desktop/cart" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>

<c:if test="${not empty cartData}">
	<c:if test="${empty showBtn or showBtn}">
		<a href="#" id="b2ctelcocheckout-cart-details-btn" class="neutral"><spring:theme code="checkout.orderDetails.show"/></a>
	</c:if>
    <div id="checkout-cart-details" class="span-24">

        <checkout:summaryCartItems cartData="${cartData}"/>

		<div class="cartOrderTotals span-11 right">
			<cart:cartTotals cartData="${cartData}"/>
		</div>

        <cart:cartPromotions cartData="${cartData}"/>

        <div class="cartPromo span-9">
			<cart:cartPotentialPromotions cartData="${cartData}"/>
		</div>
	</div>
</c:if>
