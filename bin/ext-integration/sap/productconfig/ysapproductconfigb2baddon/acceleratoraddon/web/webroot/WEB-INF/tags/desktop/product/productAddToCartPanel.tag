<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="product" required="true" type="de.hybris.platform.commercefacades.product.data.ProductData" %>
<%@ attribute name="allowAddToCart" required="true" type="java.lang.Boolean" %>
<%@ attribute name="isMain" required="true" type="java.lang.Boolean" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>


	<c:if  test="${product.configurable}">
		<spring:url
			value="${product.code}/configEntry"
			var="configureUrl">
		</spring:url>
	
		<div class="productAddToCartPanel clearfix">
			<form id="addToCartForm" class="configure_form" action="${configureUrl}" method="get">
				<button id="configButton" type="submit" class="addToCartButton">
					<spring:theme code="configure.product.link"/>
				</button>
			</form>
		</div>
	</c:if>
	
