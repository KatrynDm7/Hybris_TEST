<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ attribute name="cartData" required="true" type="de.hybris.platform.commercefacades.order.data.CartData" %>
<%@ attribute name="checkoutUrl" required="true" type="java.lang.String" %>

<c:set value="false" var="isBinded"/>
<c:if test="${not empty cartData.insuranceQuote && cartData.insuranceQuote.state eq 'BIND'}">
    <c:set value="true" var="isBinded"/>
</c:if>

<div class="span-8 certifySection">
    <form class="certifyForm" action="${checkoutUrl}">
        <c:if test="${isValidQuote}">
        <span class="certifyCheckoutBtn" >
            <ycommerce:testId code="multicheckout_next_button">
                <c:if test="${isBinded}">
                    <c:set var="show_processing_message" value="show_processing_message"/>
                </c:if>
                <c:choose>
                    <c:when test="${isBinded}">
                        <input type="submit" name="checkoutBtn" value="<spring:theme code="checkout.multi.quoteReview.checkout" text="Checkout Now"/>" class="button positive checkoutBtn ${show_processing_message}"/>
                    </c:when>
                    <c:otherwise>
                        <input type="submit" name="checkoutBtn" value="<spring:theme code="checkout.multi.quoteReview.continue" text="Continue"/>" class="button positive checkoutBtn ${show_processing_message}"/>
                    </c:otherwise>
                </c:choose>
            </ycommerce:testId>
        </span>
        </c:if>
    </form>
    <%--TODO No story for the change quote button, below just a place holder for now.--%>
    <%--<c:if test="${isBinded}">--%>
    <%--<span class="changeQuoteButton">--%>
        <%--<input type="submit" name="checkoutBtn" value="<spring:theme code="checkout.multi.quoteReview.change.quote" text="Change Quote"/>" class="button positive show_processing_message"/>--%>
    <%--</span>--%>
    <%--</c:if>--%>
</div>