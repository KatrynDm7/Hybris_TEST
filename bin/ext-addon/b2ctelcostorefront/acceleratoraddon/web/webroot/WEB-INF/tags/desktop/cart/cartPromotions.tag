<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="cartData" required="true" type="de.hybris.platform.commercefacades.order.data.CartData" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="ycommerce" uri="/WEB-INF/tld/ycommercetags.tld" %>

<c:if test="${ycommerce:doesAppliedPromotionExistForOrder(cartData)}">
    <div class="cartPromo span-9">
        <div class="headline"><spring:theme code="basket.received.promotions" /></div>

        <ycommerce:testId code="cart_recievedPromotions_labels">
            <ul>
                <c:forEach items="${cartData.orderPrices}" var="orderPriceData">
                    <c:forEach items="${orderPriceData.appliedOrderPromotions}" var="promotion">
                        <li class="cart-promotions-applied">${orderPriceData.billingTime.name}: ${promotion.description}</li>
                    </c:forEach>
                </c:forEach>
            </ul>
        </ycommerce:testId>
    </div>
</c:if>
