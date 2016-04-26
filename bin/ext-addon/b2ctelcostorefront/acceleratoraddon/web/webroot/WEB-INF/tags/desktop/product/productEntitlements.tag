<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="product" required="true" type="de.hybris.platform.commercefacades.product.data.ProductData" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${not empty product.entitlements}">
    <ul class="product-entitlements-list">
        <c:set var="entitlementsString" value=""/>
        <c:forEach items="${product.entitlements}" var="entitlement">
            <c:if test="${not empty entitlement.description}">
                <c:if test="${not empty entitlementsString}">
                    <c:set var="entitlementsString" value="${entitlementsString}${', '}"/>
                </c:if>
                <c:set var="entitlementsString" value="${entitlementsString}${entitlement.description}"/>
            </c:if>
        </c:forEach>
        <c:if test="${not empty entitlementsString}">
            <b>Includes: </b>${entitlementsString}
        </c:if>
    </ul>
</c:if>
