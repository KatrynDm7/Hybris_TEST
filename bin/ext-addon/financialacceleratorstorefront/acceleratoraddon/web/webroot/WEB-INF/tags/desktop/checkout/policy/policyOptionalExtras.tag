<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="policyData" required="true" type="de.hybris.platform.commercefacades.insurance.data.InsurancePolicyData" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<h2 class="valid js-toggle">
    <spring:theme code="checkout.orderConfirmation.optional.extras" text="Optional Extras"/>
</h2>

<span class="open js top right" data-open="optionalExtras"></span>
<div class="hidden" id="optionalExtras">
	<c:if test="${empty policyData.optionalProducts}">
	    <div>
	        <p>
	            <spring:theme code="checkout.multi.quoteReview.extras.empty" text="No optional extras were chosen" />
	        </p>
	    </div>
	</c:if>
	<ul>
	    <c:set var="extraBenefints" value="${policyData.optionalProducts}"/>
	    <c:forEach items="${extraBenefints}" var="benefit">
	        <li>${benefit.coverageProduct.name}</li>
	    </c:forEach>
	</ul>
</div>