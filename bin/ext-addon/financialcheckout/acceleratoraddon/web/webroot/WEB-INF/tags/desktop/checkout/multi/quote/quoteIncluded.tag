<%@ attribute name="insuranceQuoteReviews" required="true" type="java.util.List<de.hybris.platform.commercefacades.insurance.data.InsuranceQuoteReviewData>" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="financialCart" tagdir="/WEB-INF/tags/addons/financialacceleratorstorefront/desktop/cart" %>
<%@ attribute name="isValidStep" required="false" type="java.lang.Boolean" %>

<c:if test="${not empty insuranceQuoteReviews}">
    <c:forEach items="${insuranceQuoteReviews}" var="quoteReview">
        <c:choose>
            <c:when test="${isValidStep == 'true'}"> <c:set var="isValidClass" value="valid js-toggle"/></c:when>
            <c:otherwise><c:set var="isValidClass" value="invalid"/></c:otherwise>
        </c:choose>
        <h2 class="${isValidClass}">
            <c:if test="${cartData.insuranceQuote.state eq 'UNBIND'}">
                <spring:theme code="checkout.multi.quoteReview.included" text="Whats Included" />
            </c:if>
            <c:if test="${cartData.insuranceQuote.state eq 'BIND'}">
                <spring:theme code="checkout.multi.quoteReview.quoteDetails" text="Quote Details" /> 
            </c:if>
        </h2>
        <c:if test="${isValidStep == 'true'}">
            <span class="open js top right" data-open="whatsIncludedPanel"></span>
        </c:if>

        <c:if test="${isValidStep == 'true'}">
            <div class="hidden" id="whatsIncludedPanel">
                <c:if test="${empty quoteReview.mainProduct.benefits}">
                    <div>
                        <p>
                            <spring:theme code="checkout.multi.quoteReview.included.empty" text="-" />
                        </p>
                    </div>
                </c:if>
                <ul>
                    <c:forEach var="includedItem" items="${quoteReview.mainProduct.benefits}">
                        <li>${includedItem.name}</li>
                    </c:forEach>
                </ul>
                <c:if test="${cartData.insuranceQuote.state eq 'UNBIND'}">
                    <spring:url var="editInformationUrl" value="/c/${quoteReview.mainProduct.coverageProduct.defaultCategory.code}">
                        <spring:param value="view" name="viewStatus"/>
                    </spring:url>
                    <div class="edit">
                        <a href="${editInformationUrl}" class="button"><spring:theme code="text.cmsformsubmitcomponent.edit" text="Edit"/></a>
                    </div>
                </c:if>
            </div>
        </c:if>
    </c:forEach>
</c:if>

