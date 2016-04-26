<%@ attribute name="insuranceQuoteReviewData" required="true"
	type="de.hybris.platform.commercefacades.insurance.data.InsuranceQuoteReviewData"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:if test="${not empty insuranceQuoteReviewData}">
	<c:if test="${not empty insuranceQuoteReviewData.mainProduct}">
		<li>
			<div class="left">
				<spring:theme code="checkout.multi.quoteReview.plan" text="Plan" />
			</div>
			<div class="right">${insuranceQuoteReviewData.mainProduct.coverageProduct.name}</div>
		</li>
		<c:forEach items="${insuranceQuoteReviewData.mainProduct.benefits}" var="benefit">
			<li>
				<div class="right">${benefit.name}</div>
			</li>
		</c:forEach>
	</c:if>
</c:if>