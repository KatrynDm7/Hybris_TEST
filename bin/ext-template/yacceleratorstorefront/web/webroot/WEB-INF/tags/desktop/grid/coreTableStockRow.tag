<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="variants" required="true" type="java.util.List" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product" %>

<tr>
	<td>(<spring:theme code="product.grid.availability" />)</td>
	<c:forEach items="${variants}" var="variant">
		<c:set var="inStock" value="${variant.variantOption.stock.stockLevel > 0}"/>
		<td data-sku-id="${variant.variantOption.code}" class="td_stock ${inStock ? 'in-stock' : 'out-of-stock'}" >
			<c:if test="${inStock}">
				<product:productVariantStockThreshold variant="${variant}"/>
			</c:if>
		</td>
	</c:forEach>
</tr>

