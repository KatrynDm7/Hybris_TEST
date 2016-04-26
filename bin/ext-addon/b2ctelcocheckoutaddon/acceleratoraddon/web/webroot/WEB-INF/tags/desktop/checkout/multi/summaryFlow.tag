<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="deliveryAddress" required="true" type="de.hybris.platform.commercefacades.user.data.AddressData" %>
<%@ attribute name="deliveryMode" required="true" type="de.hybris.platform.commercefacades.order.data.DeliveryModeData" %>
<%@ attribute name="paymentInfo" required="true" type="de.hybris.platform.commercefacades.order.data.CCPaymentInfoData" %>
<%@ attribute name="requestSecurityCode" required="true" type="java.lang.Boolean" %>
<%@ attribute name="cartData" required="false" type="de.hybris.platform.commercefacades.order.data.CartData" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="multi-checkout" tagdir="/WEB-INF/tags/addons/b2ctelcocheckoutaddon/desktop/checkout/multi" %>


<div id="checkoutContentPanel" class="clearfix summaryFlow">
	<div class="headline"><spring:theme code="checkout.summary.reviewYourOrder" /></div>
    <div class="summaryFlowWrapper">
        <multi-checkout:summaryFlowDeliveryAddress deliveryAddress="${deliveryAddress}" />
        <multi-checkout:summaryFlowDeliveryMode deliveryMode="${deliveryMode}" cartData="${cartData}" />
        <multi-checkout:summaryFlowPayment paymentInfo="${paymentInfo}" requestSecurityCode="${requestSecurityCode}"/>
    </div>
</div>
