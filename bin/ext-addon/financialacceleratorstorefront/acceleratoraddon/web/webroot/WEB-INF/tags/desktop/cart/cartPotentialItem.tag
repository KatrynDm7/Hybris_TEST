<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="orderEntryData" required="true" type="de.hybris.platform.commercefacades.order.data.OrderEntryData" %>
<%@ attribute name="planProductData" required="true" type="de.hybris.platform.commercefacades.product.data.ProductData" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<div class="span-11">
    <div class="span-3 thumb">
        <product:productPrimaryImage product="${orderEntryData.product}" format="96Wx96H"/>
    </div>
    <div class="span-7">
        <ycommerce:testId code="entry_product_productName">
            <h3>${orderEntryData.product.name}</h3>
        </ycommerce:testId>
        <ycommerce:testId code="entry_product_productDescription">
            <p>${orderEntryData.product.description}</p>
        </ycommerce:testId>
    </div>
    <c:choose>
        <c:when test="${not orderEntryData.removeable and not orderEntryData.addable}">
            <div class="span-15 disabled-option"></div>
        </c:when>
    </c:choose>
</div>
<div class="span-3">
    <c:choose>
        <c:when test="${not orderEntryData.removeable and not orderEntryData.addable}">
            <button class="button" ${not orderEntryData.removeable and not orderEntryData.addable ? "disabled": ""}>
           		<c:choose>
	            	<c:when test="${fn:length(orderEntryData.product.bundleTemplates) == 3}">
	            		 <spring:theme code="text.product.mandated"/>
	            	</c:when>
	            	<c:otherwise>
	            		 <spring:theme code="checkout.notAvailable"/>
	            	</c:otherwise>
	            </c:choose> 
            </button>
        </c:when>
        <c:when test="${not orderEntryData.removeable and orderEntryData.addable}">
            <spring:url var="cartUrl" value="/cart/addBundle">
                <spring:param name="CSRFToken" value="${CSRFToken}"/>
            </spring:url>
            <form class="addPotentialProductToCartForm" action="${cartUrl}" method="post">
	            <c:choose>
	            	<c:when test="${fn:length(orderEntryData.product.bundleTemplates) == 3}">
	            		<input type="hidden" name="bundleTemplateIds" value="${orderEntryData.product.bundleTemplates[2].id}">
	            	</c:when>
	            	<c:otherwise>
	            		<input type="hidden" name="bundleTemplateIds" value="${orderEntryData.product.bundleTemplates[1].id}">
	            	</c:otherwise>
	            </c:choose>
            
                <input type="hidden" name="productCodes" value="${orderEntryData.product.code}">
                <%--  Temporary Fix to INSA-238 --%>
                <input type="hidden" name="bundleNo" value="${orderEntryData.bundleNo}">
                <button type="submit" class="button submitProduct"><spring:theme code="checkout.add"/></button>
            </form>
        </c:when>
    </c:choose>

    <c:if test="${orderEntryData.removeable}">
        <spring:url value="/cart/update" var="cartUpdateFormAction"/>
        <form:form id="updateCartForm${orderEntryData.entryNumber}" action="${cartUpdateFormAction}" method="post"
                   commandName="updateQuantityForm${orderEntryData.entryNumber}">
            <input type="hidden" name="entryNumber" value="${orderEntryData.entryNumber}"/>
            <input type="hidden" name="productCode" value="${orderEntryData.product.code}"/>
            <input type="hidden" name="initialQuantity" value="${orderEntryData.quantity}"/>
            <input type="hidden" name="quantity" value="0"/>
        </form:form>
        <ycommerce:testId code="cart_product_removeProduct">
            <spring:theme code="text.iconCartRemove" var="iconCartRemove"/>
            <a href="#" id="RemoveProduct_${orderEntryData.entryNumber}" class="submitRemoveProduct button">${iconCartRemove}</a>
        </ycommerce:testId>
    </c:if>
</div>