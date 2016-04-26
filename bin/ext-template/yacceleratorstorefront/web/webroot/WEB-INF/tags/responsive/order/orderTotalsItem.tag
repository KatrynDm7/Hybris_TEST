<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="order" required="true" type="de.hybris.platform.commercefacades.order.data.OrderData" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>

<%@ attribute name="containerCSS" required="false" type="java.lang.String" %>

<div class="account-orderdetail-orderPromotion-section text-right">
	<c:if test="${not empty order.appliedOrderPromotions}">
	    <ycommerce:testId code="order_recievedPromotions_label">
	        <c:forEach items="${order.appliedOrderPromotions}" var="promotion">
	            <div class="orderPromotion">
	            	<ycommerce:testId code="orderDetails_orderPromotion_label">
	            		${promotion.description}
	            	</ycommerce:testId>
	            </div>
	        </c:forEach>
	    </ycommerce:testId>
	</c:if>
</div>

<div class="account-orderdetail-item-section-footer order-data col-sm-6 col-sm-offset-6 col-md-5 col-md-offset-7">
	<p class="total-headline"><spring:theme code="text.account.order.orderTotals" text="Order Total"/></p>
	<div class="orderTotal">

		<div class="subtotal row">
			<div class="col-xs-6"><spring:theme code="text.account.order.subtotal" text="Subtotal:"/></div>
			<div class="col-xs-6 text-right">
				<ycommerce:testId code="orderTotal_subTotalWithDiscounts_label">
					<format:price priceData="${order.subTotalWithDiscounts}"/>
				</ycommerce:testId>
			</div>
		</div>
		<div class="shipping row">
			<div class="col-xs-6"><spring:theme code="text.account.order.shipping" text="Shipping:"/></div>
			<div class="col-xs-6 text-right">
				<ycommerce:testId code="orderTotal_devlieryCost_label">
					<format:price priceData="${order.deliveryCost}" displayFreeForZero="true"/>
				</ycommerce:testId>
			</div>
		</div>
		<c:if test="${order.net}" >
			<div class="tax row">
				<div class="col-xs-6"><spring:theme code="text.account.order.netTax" text="Tax:"/></div>
				<div class="col-xs-6 text-right">
					<format:price priceData="${order.totalTax}"/>
				</div>
			</div>
		</c:if>
		<div class="totals row">
			<div class="col-xs-6">
				<spring:theme code="text.account.order.total" text="Total:"/>
			</div>

			<c:choose>
				<c:when test="${order.net}">
					<div class="col-xs-6">
						<format:price priceData="${order.totalPriceWithTax}"/>
					</div>
				</c:when>
				<c:otherwise>
					<div class="col-xs-6 text-right">
						<ycommerce:testId code="orderTotal_totalPrice_label">
							<format:price priceData="${order.totalPrice}"/>
						</ycommerce:testId>
					</div>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
</div>
<div class="col-xs-12 order-separator"></div>

<c:if test="${order.totalDiscounts.value > 0}">
    <div class="account-orderdetail-orderTotalDiscount-section">
        <c:if test="${not order.net}">
            <div class="order-total-taxes">
                <ycommerce:testId code="orderTotal_includesTax_label">
                    <spring:theme code="text.account.order.total.includesTax" arguments="${order.totalTax.formattedValue}"/>
                </ycommerce:testId>
            </div>
        </c:if>
		<br />
        <ycommerce:testId code="order_totalDiscount_label">
            <div class="order-total-savings">
                <spring:theme code="text.account.order.totalSavings" arguments="${order.totalDiscounts.formattedValue}"/>
            </div>
        </ycommerce:testId>
    </div>
</c:if>
