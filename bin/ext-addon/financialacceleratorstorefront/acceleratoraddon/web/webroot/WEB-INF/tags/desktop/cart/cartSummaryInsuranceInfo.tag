<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="insuranceQuote" required="true"
              type="de.hybris.platform.commercefacades.quotation.InsuranceQuoteData" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>

<div class="cartSummaryInfo">
    <c:if test="${not empty insuranceQuote.tripDestination}">
        <p><spring:theme code="text.travel.insurance.destination" text="Destination&nbsp;"/>${insuranceQuote.tripDestination}</p>
    </c:if>

    <c:if test="${not empty insuranceQuote.tripStartDate}">
        <p><spring:theme code="text.travel.insurance.depart" text="Depart&nbsp;"/>${insuranceQuote.tripStartDate}</p>
    </c:if>
    
    <c:if test="${not empty insuranceQuote.tripEndDate}">
        <p><spring:theme code="text.travel.insurance.return" text="Return&nbsp;"/>${insuranceQuote.tripEndDate}</p>
    </c:if>
    
    <c:if test="${not empty insuranceQuote.tripNoOfTravellers}">
        <p>${insuranceQuote.tripNoOfTravellers}&nbsp;
            <c:choose>
                <c:when test="${insuranceQuote.tripNoOfTravellers == 1}">
                    <spring:theme code="text.travel.insurance.traveller" text="Traveller&nbsp;"/>
                </c:when>
                <c:otherwise>
                    <spring:theme code="text.travel.insurance.travellers" text="Travellers&nbsp;"/>
                </c:otherwise>
            </c:choose>
        </p>
    </c:if>
</div>