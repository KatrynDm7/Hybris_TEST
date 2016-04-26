<%@ attribute name="insuranceQuoteReviews" required="true" type="java.util.List<de.hybris.platform.commercefacades.insurance.data.InsuranceQuoteReviewData>" %>
<%@ attribute name="cartData" required="false" type="de.hybris.platform.commercefacades.order.data.CartData" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="checkout" tagdir="/WEB-INF/tags/addons/financialcheckout/desktop/checkout" %>
<%@ taglib prefix="multi-checkout" tagdir="/WEB-INF/tags/addons/financialcheckout/desktop/checkout/multi" %>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/desktop/cart" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="financialCart" tagdir="/WEB-INF/tags/addons/financialacceleratorstorefront/desktop/cart" %>

<c:if test="${not empty insuranceQuoteReviews}">
    <c:forEach items="${insuranceQuoteReviews}" var="quoteReview">
        <c:forEach items="${quoteReview.mainProduct.coverageProduct.images}" var="image">
            <c:if test="${image.format == '40Wx40H_quote'}">
                <c:set var="thumbnail_img" value="${image}"/>
            </c:if>
        </c:forEach>
        <div class="span-2">
            <span class="insImg"><img src="${thumbnail_img.url}"/></span>
        </div>
        <div class="span-4 text-18">
            <span>${quoteReview.mainProduct.coverageProduct.defaultCategory.name}</span>&nbsp;<spring:theme code="checkout.multi.quoteReview.quote" text="Quote"/>
        </div>
        <div class="span-7 right">
            <c:choose>
                <c:when test="${cartData.insuranceQuote.state eq 'BIND'}">
                    <div class="quoteId"><spring:theme code="checkout.multi.quoteReview.quote.id" text="ID: {0}" arguments="${cartData.insuranceQuote.quoteId}" /></div>
                    <div class="expiryDate"><spring:theme code="checkout.multi.quoteReview.expiry.date" text="Expiry Date: {0}" arguments="${cartData.insuranceQuote.formattedExpiryDate}" /></div>
                </c:when>
            </c:choose>
        </div>
    </c:forEach>
</c:if>
