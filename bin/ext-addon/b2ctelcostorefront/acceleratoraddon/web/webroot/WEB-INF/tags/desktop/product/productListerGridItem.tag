<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="product" required="true" type="de.hybris.platform.commercefacades.product.data.ProductData" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product" %>
<%@ taglib prefix="b2ctelcoProduct" tagdir="/WEB-INF/tags/addons/b2ctelcostorefront/desktop/product" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="/WEB-INF/tld/ycommercetags.tld" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>

<spring:theme code="text.addToCart" var="addToCartText"/>
<c:url value="${product.url}" var="productUrl"/>

<ycommerce:testId code="product_wholeProduct">

	<div class="productListItem ${fn:contains(product.url, 'Add-Ons') ? 'productListAddon' : ''}">

		<div class="thumb">
			<a href="${productUrl}" title="${product.name}">
				<product:productPrimaryImage product="${product}" format="thumbnail"/>
			</a>
		</div>

		<div class="price">
			<ycommerce:testId code="product_productPrice">
				<spring:theme code="text.withinPackage" var="withinPackage"/>
				<c:if test="${product.lowestBundlePrice ne null and product.lowestBundlePrice.value < product.price.value}">
					<span class="price-label">${withinPackage}</span>
                    <format:price priceData="${product.lowestBundlePrice}"/>
					<br/>
                    <del><format:price priceData="${product.price}"/></del>
				</c:if>
				<c:if test="${product.lowestBundlePrice eq null or (product.lowestBundlePrice ne null and product.lowestBundlePrice.value >= product.price.value)}">
					<format:price priceData="${product.lowestBundlePrice}" displayFreeForZero="true"/>
				</c:if>
				<c:if test="${fn:contains(product.url, 'Add-Ons')}"><div class="price-frequency">${product.subscriptionTerm.billingPlan.billingTime.name}</div></c:if>
				<c:if test="${fn:contains(product.url, 'Plans')}"><div class="price-frequency">${product.subscriptionTerm.billingPlan.billingTime.name}</div></c:if>
			</ycommerce:testId>
			<c:if test="${product.stock.stockLevelStatus.code eq 'outOfStock' }">
				<c:set var="buttonType">button</c:set>
				<spring:theme code="text.addToCart.outOfStock" var="addToCartText"/>
			</c:if>
		</div>

		<div class="head"> <!-- telco change -->
			<ycommerce:testId code="product_productName">
				<a href="${productUrl}" title="${product.name}">
					${product.name}
				</a>
			</ycommerce:testId>
		</div>
		<c:if test="${not empty product.averageRating}">
			<product:productStars rating="${product.averageRating}"/>
		</c:if>
		<c:if test="${not empty product.summary}">
			<div class="details">${product.summary}</div>
		</c:if>

		<div class="grid-product-entitlements"> <!-- telco change -->
		    <b2ctelcoProduct:productEntitlements product="${product}"/>
        </div>

        <div class="clear"></div>

		<product:productListerClassifications product="${product}"/>				

		<div class="clearfix">&nbsp;</div>

	</div>

</ycommerce:testId>
