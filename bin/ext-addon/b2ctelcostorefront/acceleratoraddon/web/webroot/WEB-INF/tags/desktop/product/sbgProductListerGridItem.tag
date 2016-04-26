<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="product" required="true" type="de.hybris.platform.commercefacades.product.data.ProductData" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="/WEB-INF/common/tld/ycommercetags.tld" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<spring:theme code="text.addToCart" var="addToCartText"/>
<c:url value="${product.url}" var="productUrl"/>

<ycommerce:testId code="product_wholeProduct">
		<div class="thumb">
			<a href="${productUrl}" title="${product.name}">
				<product:productPrimaryImage product="${product}" format="product"/>
			</a>
		</div>

		<div class="name">
			<ycommerce:testId code="product_productName">
				<a href="${productUrl}" title="${product.name}">${product.name}</a>
			</ycommerce:testId>
		</div>
		
		<c:if test="${not empty product.averageRating}">
			<product:productStars rating="${product.averageRating}"/><br/>
		</c:if>
		<c:if test="${not empty product.summary}">
			<div class="summary">${product.summary}</div>
		</c:if>
			
      <product:productListerClassifications product="${product}"/>				
</ycommerce:testId>
