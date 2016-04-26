<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/desktop/cart" %>

<a class="button" href="${continueShoppingUrl}">
	<spring:theme text="Continue Shopping" code="cart.page.continue"/>
</a>
<button id="checkoutButtonBottom" class="doCheckoutBut positive right continueCheckout" type="button" data-checkout-url="${checkoutUrl}">
	<spring:theme code="checkout.checkout" />
</button>