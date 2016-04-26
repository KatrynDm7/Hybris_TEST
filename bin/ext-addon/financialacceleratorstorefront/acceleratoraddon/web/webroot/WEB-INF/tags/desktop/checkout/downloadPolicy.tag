<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="order" required="true" type="de.hybris.platform.commercefacades.order.data.AbstractOrderData" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>

<div class="downloadPolicy">
    <c:forEach items="${order.entries}" var="entry">
        <c:set value="${entry.product}" var="product"/>
        <c:forEach items="${product.specifications}" var="downloadUrl">
            <spring:url value="${downloadUrl}" var="url"/>
            <a href="${downloadUrl}" target="_blank" class="button positive right">
                <spring:theme code="checkout.link.download.policy" text="Download policy"/>
            </a>
        </c:forEach>
    </c:forEach>
</div>
