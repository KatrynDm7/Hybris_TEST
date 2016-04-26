<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="span-8 changeOption">
    <c:url value="/cart" var="optionPage"/>
    <a type="submit" class="changeOptionButton" href="${optionPage}"><spring:theme code="checkout.link.change.options" text="Change option"/></a>
</div>