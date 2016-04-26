<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="policyResponse" required="true" type="de.hybris.platform.commercefacades.insurance.data.InsurancePolicyResponseData" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ attribute name="images" required="true" type="java.util.Collection" %>

<c:forEach items="${images}" var="image">
    <c:if test="${image.format == '40Wx40H_policy'}">
        <c:set var="thumbnail_img" value="${image}"/>
    </c:if>
</c:forEach>

<div id="policySummaryLine" class="information">
	<c:if test="${not empty thumbnail_img}"><img src="${thumbnail_img.url}"/></c:if>
	<div class="number">
		<spring:theme code="checkout.link.policy.name" text="Your policy name is" />&nbsp;${categoryName} <br/>
		<spring:theme code="checkout.link.policy.number" text="Your policy number is" />: ${policyResponse.policyNumber}
	</div>
	<spring:url value="${policyResponse.policyUrl}" var="url"/>
    <a href="${url}" target="_blank" class="button positive right">
        <spring:theme code="checkout.link.download.your.policy" text="Download Your Policy"/>
    </a>
</div>