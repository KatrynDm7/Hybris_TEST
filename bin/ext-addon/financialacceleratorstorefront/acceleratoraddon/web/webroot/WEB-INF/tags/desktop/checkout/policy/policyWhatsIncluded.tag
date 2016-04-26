<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="policyData" required="true" type="de.hybris.platform.commercefacades.insurance.data.InsurancePolicyData" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


<h2 class="valid js-toggle">
	<spring:theme code="checkout.multi.quoteReview.included" text="Whats Included" />
</h2>

<span class="open js top right" data-open="whatsIncludedPanel"></span>
<div class="hidden" id="whatsIncludedPanel">
	<h3>
		${policyData.mainProduct.coverageProduct.name}
	</h3>
	
	<c:set var="benefits" value="${policyData.mainProduct.benefits}"/>
	<ul>
		<c:forEach items="${benefits}" var="benefit">
			<li>${benefit.name}</li>
		</c:forEach>
	</ul>
</div>