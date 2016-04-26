<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="insuranceQuoteReviews" required="true" type="java.util.List<de.hybris.platform.commercefacades.insurance.data.InsuranceQuoteReviewData>" %>
<%@ attribute name="cartData" required="true" type="de.hybris.platform.commercefacades.order.data.CartData" %>
<%@ taglib prefix="financialCart" tagdir="/WEB-INF/tags/addons/financialacceleratorstorefront/desktop/cart" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:if test="${not empty insuranceQuoteReviews}">
    <c:set var="insuranceQuoteReview" value="${insuranceQuoteReviews[0]}"/>
    <div class="quoteWhatsIncluded">
        <div class="header">
            <c:if test="${cartData.insuranceQuote.state eq 'UNBIND'}">
                <spring:theme code="checkout.multi.quoteReview.add.options" text="Add Options"/>
            </c:if>
            <c:if test="${cartData.insuranceQuote.state eq 'BIND'}">
                <spring:theme code="checkout.multi.quoteReview.added" text="Added by You"/>
            </c:if>
        </div>
        <div class="content">
            <ul>
                <c:forEach items="${insuranceQuoteReview.optionalProducts}" var="coverageData">
                    <li>
                            ${coverageData.coverageProduct.name}
                    </li>
                </c:forEach>
            </ul>
            <c:if test="${cartData.insuranceQuote.state eq 'UNBIND'}">
                <div>
                    <financialCart:cartChangeOption/>
                </div>
            </c:if>
        </div>
    </div>
</c:if>
