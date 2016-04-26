<%@ attribute name="cartData" required="true" type="de.hybris.platform.commercefacades.order.data.CartData" %>
<%@ attribute name="showShipDeliveryEntries" required="false" type="java.lang.Boolean" %>
<%@ attribute name="showPickupDeliveryEntries" required="false" type="java.lang.Boolean" %>
<%@ attribute name="showTax" required="false" type="java.lang.Boolean" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="checkout" tagdir="/WEB-INF/tags/addons/financialcheckout/desktop/checkout" %>
<%@ taglib prefix="multi-checkout" tagdir="/WEB-INF/tags/addons/financialcheckout/desktop/checkout/multi" %>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/desktop/cart" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="financialCart" tagdir="/WEB-INF/tags/addons/financialacceleratorstorefront/desktop/cart" %>
<%@ taglib prefix="quote" tagdir="/WEB-INF/tags/addons/financialcheckout/desktop/checkout/multi/quote" %>

<c:if test="${not empty cartData}">
    <div id="rightHandDetails">
        <div id="checkoutOrderDetails" class="span-8 last cartData">
            <financialCart:cartItems cartData="${cartData}" displayChangeOptionLink="true"/>
            <financialCart:cartTotals cartData="${cartData}" showTaxEstimate="${taxEstimationEnabled}"/>
        </div>
		<financialCart:cartModifyPlan cartData="${cartData}" flowStartUrl="${flowStartUrl}"/>
    </div>
</c:if>
