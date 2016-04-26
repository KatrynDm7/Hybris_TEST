<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="order" required="true" type="de.hybris.platform.commercefacades.order.data.OrderData" %>
<%@ attribute name="orderEntry" required="true" type="de.hybris.platform.commercefacades.order.data.OrderEntryData" %>
<%@ attribute name="consignmentEntry" required="false" type="de.hybris.platform.commercefacades.order.data.ConsignmentEntryData" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>


<c:url value="${orderEntry.product.url}" var="productUrl"/>
<c:set var="entryStock" value="${orderEntry.product.stock.stockLevelStatus.code}"/>
<ul>
	<li class="product-item">
		<div class="thumb">
            <div class="col-lg-12">
                <ycommerce:testId code="orderDetail_productThumbnail_link">
                    <a href="${productUrl}">
                        <product:productPrimaryImage product="${orderEntry.product}" format="thumbnail"/>
                    </a>
                </ycommerce:testId>
            </div>
		</div>
		<div class="price-total">
			<ycommerce:testId code="orderDetails_productTotalPrice_label">
				<format:price priceData="${orderEntry.totalPrice}" displayFreeForZero="true"/>
			</ycommerce:testId>
		</div>
        <div class="col-md-3 col-lg-4">
            <div class="details">
                <div class="name">
                    <ycommerce:testId code="orderDetails_productName_link">
                        <a href="${orderEntry.product.purchasable ? productUrl : ''}">${orderEntry.product.name}</a>
                    </ycommerce:testId>
                </div>
            </div>
            <div class="price">
                <ycommerce:testId code="orderDetails_productItemPrice_label">
                    <format:price priceData="${orderEntry.basePrice}" displayFreeForZero="true"/>
                </ycommerce:testId>
                <ycommerce:testId code="orderDetails_productQuantity_label">
                    <br><spring:theme code="text.account.order.qty"/>&nbsp;
                    <c:choose>
                        <c:when test="${consignmentEntry ne null }">
                            ${consignmentEntry.quantity}
                        </c:when>
                        <c:otherwise>
                            ${orderEntry.quantity}
                        </c:otherwise>
                    </c:choose>
                </ycommerce:testId>
            </div>
            <div>
                <c:forEach items="${orderEntry.product.baseOptions}" var="option">
                    <c:if test="${not empty option.selected and option.selected.url eq orderEntry.product.url}">
                        <c:forEach items="${option.selected.variantOptionQualifiers}" var="selectedOption">
                               <div>
                                <ycommerce:testId code="orderDetail_variantOption_label">
                                    <span>${selectedOption.name}:</span>
                                    <span>${selectedOption.value}</span>
                                </ycommerce:testId>
                               </div>
                               <c:set var="entryStock" value="${option.selected.stock.stockLevelStatus.code}"/>
                        </c:forEach>
                    </c:if>
                </c:forEach>
            </div>
        </div>
		<div class="promo col-md-4 col-lg-3">
			<c:if test="${not empty order.appliedProductPromotions}">
				<ul>
					<c:forEach items="${order.appliedProductPromotions}" var="promotion">
						<c:set var="displayed" value="false"/>
						<c:forEach items="${promotion.consumedEntries}" var="consumedEntry">
							<c:if test="${not displayed and consumedEntry.orderEntryNumber == orderEntry.entryNumber}">
								<c:set var="displayed" value="true"/>
									<li>
										<ycommerce:testId code="orderDetail_productPromotion_label">
											<span>${promotion.description}</span>
										</ycommerce:testId>
									</li>
							</c:if>
						</c:forEach>
					</c:forEach>
				</ul>
			</c:if>
		</div>

        <div class="col-xs-12">
            <ycommerce:testId code="orderDetail_productStock_label">
                <c:choose>
                    <c:when test="${not empty entryStock and entryStock ne 'outOfStock'}">
                        <spring:theme code="basket.page.title"/>&nbsp;<span class="stock"><spring:theme code="product.variants.in.stock"/></span>
                    </c:when>
                    <c:when test="${orderEntry.deliveryPointOfService eq null}">
                        <spring:theme code="basket.page.title"/>&nbsp;<span class="stock"><spring:theme code="product.variants.out.of.stock"/></span>
                    </c:when>
                </c:choose>
            </ycommerce:testId>
        </div>
	</li>
</ul>