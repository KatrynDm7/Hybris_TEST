<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="orderData" required="false" type="de.hybris.platform.commercefacades.order.data.OrderData" %>
<%@ attribute name="isValidStep" required="false" type="java.lang.Boolean" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="quote" tagdir="/WEB-INF/tags/addons/financialcheckout/desktop/checkout/multi/quote" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

    <c:set value="${orderData}" var="masterEntry"/>

    <h2 class="valid js-toggle"><spring:theme code="checkout.orderConfirmation.details.information" text="Information"/></h2>
	<span class="open js top right" data-open="policyCoverageLevel"></span>

    <div class="hidden" id="policyCoverageLevel">
        <div class="span-12">
            <ul class="chooseCover">

                <c:if test="${not empty masterEntry.insuranceQuote.tripDestination}">

                    <li>
                        <div class="left">
                            <spring:theme code="checkout.multi.quoteReview.travel.destination" text="Destination"/>
                        </div>
                        <div class="right">${masterEntry.insuranceQuote.tripDestination }</div>
                    </li>

                    <li>
                        <div class="left">
                            <spring:theme code="checkout.multi.quoteReview.travel.depart" text="Depart"/>
                        </div>
                        <div class="right">${masterEntry.insuranceQuote.tripStartDate }</div>
                    </li>

					<c:if test="${not empty masterEntry.insuranceQuote.tripEndDate}">
                    <li>
                        <div class="left">
                            <spring:theme code="checkout.multi.quoteReview.travel.return" text="Return"/>
                        </div>
                        <div class="right">${masterEntry.insuranceQuote.tripEndDate }</div>
                    </li>
                    </c:if>
                    
                    <li>
                        <div class="left">
                            <spring:theme code="checkout.multi.quoteReview.travel.nooftravellers" text="No. of Travellers"/>
                        </div>
                        <div class="right">${masterEntry.insuranceQuote.tripNoOfTravellers }</div>
                    </li>

                    <li>
                        <div class="left">
                            <spring:theme code="checkout.multi.quoteReview.travel.age" text="Age of Travellers"/>
                        </div>
                        <c:forEach items="${masterEntry.insuranceQuote.tripTravellersAge}" var="travellers"
                                   varStatus="status">
                            ${travellers}${not status.last ? ',' : ''}
                        </c:forEach>
                    </li>

                </c:if>

                <c:if test="${not empty masterEntry.insuranceQuote.propertyAddressLine1}">
                    <li>
                        <div class="left">
                            <spring:theme code="checkout.multi.quoteReview.property.property.address" text="Property:"/>
                        </div>
                        <div class="right">${masterEntry.insuranceQuote.propertyAddressLine1}</div>
                    </li>
                </c:if>
                <c:if test="${not empty masterEntry.insuranceQuote.propertyCoverRequired}">
                    <li>
                        <div class="left">
                            <spring:theme code="checkout.multi.quoteReview.property.property.cover.required" text="Cover Required:"/>
                        </div>
                        <div class="right">${masterEntry.insuranceQuote.propertyCoverRequired}</div>
                    </li>
                </c:if>
                <c:if test="${not empty masterEntry.insuranceQuote.propertyStartDate}">
                    <li>
                        <div class="left">
                            <spring:theme code="checkout.multi.quoteReview.property.property.start.date" text="Start Date:"/>
                        </div>
                        <div class="right">${masterEntry.insuranceQuote.propertyStartDate}</div>
                    </li>
                </c:if>
                <c:if test="${not empty masterEntry.insuranceQuote.propertyType}">
                    <li>
                        <div class="left">
                            <spring:theme code="checkout.multi.quoteReview.property.property.type" text="Property Type:"/>
                        </div>
                        <div class="right">${masterEntry.insuranceQuote.propertyType}</div>
                    </li>
                </c:if>
                <c:if test="${not empty masterEntry.insuranceQuote.propertyValue}">
                    <li>
                        <div class="left">
                            <spring:theme code="checkout.multi.quoteReview.property.property.value" text="Property Value:"/>
                        </div>
                        <div class="right">${masterEntry.insuranceQuote.propertyValue}</div>
                    </li>
                </c:if>
                <c:if test="${not empty masterEntry.insuranceQuote.autoDetail.autoMake}">
                    <li>
                        <div class="left">
                            <spring:theme code="checkout.multi.quoteReview.auto.vehicle.make" text="Vehicle Make: "/>
                        </div>
                        <div class="right">${masterEntry.insuranceQuote.autoDetail.autoMake}</div>
                    </li>
                </c:if>
                <c:if test="${not empty masterEntry.insuranceQuote.autoDetail.autoModel}">
                    <li>
                        <div class="left">
                            <spring:theme code="checkout.multi.quoteReview.auto.vehicle.model" text="Vehicle Model: "/>
                        </div>
                        <div class="right">${masterEntry.insuranceQuote.autoDetail.autoModel}</div>
                    </li>
                </c:if>
                <c:if test="${not empty masterEntry.insuranceQuote.autoDetail.autoLicense}">
                    <li>
                        <div class="left">
                            <spring:theme code="checkout.multi.quoteReview.auto.vehicle.license" text="Vehicle License: "/>
                        </div>
                        <div class="right">${masterEntry.insuranceQuote.autoDetail.autoLicense}</div>
                    </li>
                </c:if>
                <c:if test="${not empty masterEntry.insuranceQuote.autoDetail.autoPrice}">
                    <li>
                        <div class="left">
                            <spring:theme code="checkout.multi.quoteReview.auto.vehicle.value" text="Vehicle Value: "/>
                        </div>
                        <div class="right">${masterEntry.insuranceQuote.autoDetail.autoPrice}</div>
                    </li>
                </c:if>
                
                <c:if test="${masterEntry.insuranceQuote.quoteType eq 'LIFE'}">
                    <quote:quotePlanInfoLifeInsuranceSection quoteData="${masterEntry.insuranceQuote}"/> 
                </c:if>
            </ul>
        </div>
    </div>
