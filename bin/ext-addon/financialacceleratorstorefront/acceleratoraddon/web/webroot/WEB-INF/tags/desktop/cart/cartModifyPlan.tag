<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="cartData" required="true" type="de.hybris.platform.commercefacades.order.data.CartData" %>
<%@ attribute name="flowStartUrl" required="true" type="java.lang.String" %>
 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:forEach items="${cartData.entries}" var="entry" varStatus="status">
	<c:if test="${status.first}">
		<c:if test="${cartData.insuranceQuote.state eq 'UNBIND'}">
			<spring:url var="editInformationUrl" value="/c/${entry.product.defaultCategory.code}">
                <spring:param value="view" name="viewStatus"/>
            </spring:url>
			<span><a href="${editInformationUrl}"><spring:theme code="checkout.cart.modify.plan" text="Modify Plan" /></a></span>
		</c:if>
	</c:if>
</c:forEach>
