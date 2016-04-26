<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="orderData" required="true" type="de.hybris.platform.commercefacades.order.data.OrderData" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="financialCart" tagdir="/WEB-INF/tags/addons/financialacceleratorstorefront/desktop/cart" %>
<%@ attribute name="displayChangeOptionLink" required="false" type="java.lang.Boolean" %>

<div id="checkoutOrderDetails" class="span-8 last cartData">
	<div class="span-8 cartItemsHeadline">
	    <div class="quoteName">
            <span class="productname">
                <spring:theme code="checkout.orderConfirmation.my.policy" text="My Policy"/>
            </span>
            <p>
                <spring:theme code="checkout.orderConfirmation.my.policy.number" text="My Policy Number"/>:
                <span class="price">
                    ${orderData.insurancePolicyResponses[0].policyNumber}
                </span>
            </p>
	    </div>

        <div class="orderDetails">
            <span> <spring:theme code="checkout.orderConfirmation.payment.frequency"/>:</span>
            <c:choose>
                <c:when test="${not empty orderData.entries[0].product.price.recurringChargeEntries }">
                    <span class="price"><spring:theme code="checkout.cart.payment.frequency.monthly" text="Monthly" /></span>
                    <span><c:set var="paymentFrequency"></c:set></span>
                </c:when>
                <c:otherwise>
                    <span class="price"><spring:theme code="checkout.cart.payment.frequency.annual" text="Annual" /></span>
                </c:otherwise>
            </c:choose>
        </div>

        <div class="orderDetails">
           <span><spring:theme code="checkout.orderConfirmation.policy.start.date"/>: </span>
           <span class="price">${orderData.insuranceQuote.formattedStartDate}</span>
	    </div>
	    
	    <c:if test="${orderData.insuranceQuote.quoteType eq 'TRAVEL' }">
		    <c:if test="${not empty orderData.insuranceQuote.tripEndDate}">
				<div class="orderDetails">
                    <span><spring:theme code="checkout.cart.end.date"/>:</span>
					<span class="price">
						${orderData.insuranceQuote.tripEndDate}
					</span>
				</div>
			</c:if>
			
		    <div class="orderDetails">
                <span><spring:theme code="text.cmstripdetailssubmitcomponent.number.of.travellers"/>: </span>
                <span class="price">
                    ${orderData.insuranceQuote.tripNoOfTravellers}
                </span>
			</div>
			<div class="orderDetails">
                <span><spring:theme code="text.cmstripdetailssubmitcomponent.ages.of.travellers"/>:</span>
	           	<span class="price">
					<c:forEach var="travellerAge" items="${orderData.insuranceQuote.tripTravellersAge}" varStatus="status">
						<c:if test="${status.index ne 0 }">,&nbsp;</c:if>${travellerAge}
					</c:forEach>
				</span>
		    </div>
	    </c:if>
	    <c:forEach items="${orderData.entries}" var="entry" varStatus="status">
	        <c:if test="${status.first}">
	            <div class="orderDetails">
                    <span>
                        <ycommerce:testId code="cart_product_name">
                            ${entry.product.name}:
                        </ycommerce:testId>
                    </span>
                    <span class="price">
                        <ycommerce:testId code="cart_totalProductPrice_label">
                            <format:price priceData="${entry.totalPrice}" displayFreeForZero="true"/>
                        </ycommerce:testId>
                    </span>
	            </div>
	        </c:if>
	    </c:forEach>
		<div class="span-12 cartItems">
		    <c:forEach items="${orderData.entries}" var="entry" varStatus="status">
		        <c:if test="${not status.first}">
		            <div class="cartItem">
		                <c:url value="${entry.product.url}" var="productUrl"/>
		                <span>
                            <ycommerce:testId code="cart_product_name">
                                ${entry.product.name}:
                            </ycommerce:testId>
                        </span>
                        <span class="price">
                            <ycommerce:testId code="cart_totalProductPrice_label">
                                <format:price priceData="${entry.totalPrice}" displayFreeForZero="true"/>
                            </ycommerce:testId>
                        </span>
		            </div>
		        </c:if>
		    </c:forEach>
		</div>
		
		<div id="orderTotals" class="span-12">
		    <div>
                <spring:theme code="basket.page.totals.total"/>&nbsp;
                <span class="price">
                <ycommerce:testId code="cart_totalPrice_label">
                    <c:choose>
                        <c:when test="${showTax}">
                            <format:price priceData="${orderData.totalPriceWithTax}"/>
                        </c:when>
                        <c:otherwise>
                            <format:price priceData="${orderData.totalPrice}"/>
                        </c:otherwise>
                    </c:choose>
                </ycommerce:testId>
                </span>
		    </div>
		</div>
	</div>
</div>
