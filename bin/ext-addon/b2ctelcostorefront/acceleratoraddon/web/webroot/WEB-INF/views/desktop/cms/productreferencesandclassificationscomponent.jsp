<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:if test="${not empty productAccessories}">
	<c:if test="${component.maximumNumberProducts > 0}">

	<div class="scroller">
            <div class="title">${component.title}</div>
			<ul class="carousel jcarousel-skin">
                <c:forEach end="${component.maximumNumberProducts}" items="${productAccessories}" var="compatibleProduct">
                    <c:url value="${compatibleProduct.url}" var="productUrl"/>
                    <li>
                        <a href="${productUrl}" class="scrollerProduct">
                            <div class="thumb">
                                <product:productPrimaryImage product="${compatibleProduct}" format="product"/>
                            </div>
                            <c:if test="${component.displayProductPrices}">
                                <div class="priceContainer"><format:fromPrice priceData="${compatibleProduct.price}"/></div>
                            </c:if>
                            <c:if test="${component.displayProductTitles}">
                                <div class="details">${compatibleProduct.name}</div>
                            </c:if>
                        </a>
                    </li>
                </c:forEach>
			</ul>
		</div>
	</c:if>
</c:if>