<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="product" required="true" type="de.hybris.platform.commercefacades.product.data.ProductData" %>
<%@ attribute name="bundleNo" required="true" type="java.lang.Integer" %>
<%@ attribute name="componentId" required="true" type="java.lang.String" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<spring:theme code="text.addToCart" var="addToCartText"/>
<c:url value="${product.url}" var="productUrl"/>
<c:url value="/bundle/addEntry" var="addToCartBundle" />

<ycommerce:testId code="product_wholeProduct">


	<div class="productListItem <c:if test="${product.disabled}">productListItemDisabled</c:if> ${fn:contains(product.url, 'Add-Ons') ? 'productListAddon' : ''}">
		
		<div class="thumb">
			<a><product:productPrimaryImage product="${product}" format="thumbnail"/></a>
		</div>
		
		<!-- TELCO_START -->

		<div class="price">
			<ycommerce:testId code="product_productPrice">
				<c:if test="${product.thisBundleProductPrice.value ne null and product.thisBundleProductPrice.value lt product.price.value}">
					<format:price priceData="${product.thisBundleProductPrice}" displayFreeForZero="true"/>
                    <br/>
                    <del><format:price priceData="${product.price}"/></del>
				</c:if>
				<c:if test="${product.thisBundleProductPrice.value eq null or product.thisBundleProductPrice.value ge product.price.value}">
					<format:price priceData="${product.price}" displayFreeForZero="true"/>					
				</c:if>						
				<div class="price-frequency"> ${product.subscriptionTerm.billingPlan.billingTime.name} </div>	
			</ycommerce:testId>
			<c:if test="${product.stock.stockLevelStatus.code eq 'outOfStock' }">
				<c:set var="buttonType">button</c:set>
				<spring:theme code="text.addToCart.outOfStock" var="addToCartText"/>
			</c:if>
		</div>

		<div class="head">
			<ycommerce:testId code="product_productName">
				<a>${product.name}</a>
			</ycommerce:testId>
		</div>
		<c:if test="${not empty product.averageRating}">
			<product:productStars rating="${product.averageRating}"/>
		</c:if>
		<c:if test="${not empty product.summary}">
			<div class="details">${product.summary}
                <c:if test="${product.disabled}">
                    <span class="product-disabled-message">
                        <spring:theme code="product.list.cannotPurchaseReason" text="Product cannot be purchased because of: {0}" arguments="${product.disabledMessage}" argumentSeparator="!!!!"/>
                    </span>
                </c:if>
            </div>
		</c:if>
		<!-- TELCO_END -->

        <c:if test="${!product.disabled}">
            <form:form id="bundleAddToCartForm${product.code}" action="${addToCartBundle}" method="post" class="product-action">
                <div id="${product.code}" class="select-device">
                    <button type="submit" class="positive small delayed-button" title="<spring:theme code="guidedselling.select.device.select.button" />">
                        <spring:theme code="guidedselling.select.device.select.button" />
                    </button>
                </div>
                <input type="hidden" name="productCodePost" value="${product.code}"/>
                <input type="hidden" name="quantity" value="1"/>
                <input type="hidden" name="bundleNo" value="${bundleNo}"/>
                <input type="hidden" name="bundleTemplateId" value="${componentId}"/>
                <input type="hidden" name="removeCurrentProducts" value="true"/>
                <input type="hidden" name="navigation" value="NEXT"/>
            </form:form>
        </c:if>
			
		<product:productListerClassifications product="${product}"/>				

	</div>

</ycommerce:testId>
