<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="order" required="true" type="de.hybris.platform.commercefacades.order.data.OrderData" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="/WEB-INF/tld/addons/b2ctelcostorefront/ycommercetags.tld" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/addons/b2ctelcocheckoutaddon/desktop/product" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>

<%@ attribute name="containerCSS" required="false" type="java.lang.String" %>

<c:if test="${ycommerce:doesAppliedPromotionExistForOrder(order)}">

        <div class="headline"><spring:theme code="text.account.order.receivedPromotions" text="Received Promotions"/></div>

        <ul>
            <c:forEach items="${order.orderPrices}" var="orderPriceData">
                <c:forEach items="${orderPriceData.appliedOrderPromotions}" var="promotion">
                    <li class="cart-promotions-applied">${orderPriceData.billingTime.name}: ${promotion.description}</li>
                </c:forEach>
            </c:forEach>
        </ul>

</c:if>
