<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="product" required="true" type="de.hybris.platform.commercefacades.product.data.ProductData" %>
<%@ attribute name="isOrderForm" required="false" type="java.lang.Boolean" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>

<c:choose>
	<c:when test="${empty product.volumePrices}">
		<c:choose>
			<c:when test="${(not empty product.priceRange) and (product.priceRange.minPrice.value ne product.priceRange.maxPrice.value) and ((empty product.baseProduct) or (not empty isOrderForm and isOrderForm))}">
				<span class="big-price right">
					<format:price priceData="${product.priceRange.minPrice}"/>
				</span>
				<span class="big-price right">
					-
				</span>
				<span class="big-price right">
					<format:price priceData="${product.priceRange.maxPrice}"/>
				</span>
			</c:when>
			<c:otherwise>
				<p class="big-price right">
					<format:fromPrice priceData="${product.price}"/>
				</p>
			</c:otherwise>
		</c:choose>
	</c:when>
	<c:otherwise>
		<table class="volume-prices" cellpadding="0" cellspacing="0" border="0">
			<thead>
			<th class="volume-prices-quantity"><spring:theme code="product.volumePrices.column.qa"/></th>
			<th class="volume-price-amount"><spring:theme code="product.volumePrices.column.price"/></th>
			</thead>
			<tbody>
			<c:forEach var="volPrice" items="${product.volumePrices}">
				<tr>
					<td class="volume-price-quantity">
						<c:choose>
							<c:when test="${empty volPrice.maxQuantity}">
								${volPrice.minQuantity}+
							</c:when>
							<c:otherwise>
								${volPrice.minQuantity}-${volPrice.maxQuantity}
							</c:otherwise>
						</c:choose>
					</td>
					<td class="volume-price-amount">${volPrice.formattedValue}</td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
	</c:otherwise>
</c:choose>