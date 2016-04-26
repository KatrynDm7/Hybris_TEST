<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="cartData" required="true" type="de.hybris.platform.commercefacades.order.data.CartData" %>
<%@ taglib prefix="financialCart" tagdir="/WEB-INF/tags/addons/financialacceleratorstorefront/desktop/cart" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>


<c:if test="${not empty cartData.entries}">
    <div class="span-8 changePlan">
        <spring:url var="changePlanUrl" value="/cart/changePlan">
            <spring:param name="CSRFToken" value="${CSRFToken}" />
        </spring:url>
        <form class="changePlanForm" action="${changePlanUrl}" method="POST">
            <a href="#" type="submit" class="changePlanButton"><spring:theme code="checkout.link.change.plan" text="Change plan"/></a>
        </form>
    </div>
</c:if>

<financialCart:changePlanConfirmPopup confirmActionButtonId="changePlanConfirmButton" cartData="${cartData}"/>
