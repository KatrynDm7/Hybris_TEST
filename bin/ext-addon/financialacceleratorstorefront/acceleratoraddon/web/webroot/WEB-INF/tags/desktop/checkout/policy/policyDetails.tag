<%@ attribute name="policyData" required="true" type="de.hybris.platform.commercefacades.insurance.data.InsurancePolicyData" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>

<c:if test="${not empty policyData.policyHolderDetail}">
    <c:set var="policyHolderDetail" value="${policyData.policyHolderDetail}"/>
    <h2 class="valid js-toggle"><spring:theme code="checkout.orderConfirmation.details.your.details" text="Your Details"/></h2>

	<span class="open js top right" data-open="policyDetails"></span>
	<div class="hidden" id="policyDetails">
	    <div class="span-10">
	        <h3><spring:theme code="checkout.orderConfirmation.details.policy.holder" text="Policy Holder"/></h3>
	        <ul>
	            <li>${policyHolderDetail.firstName} &nbsp; ${policyHolderDetail.lastName}</li>
	            <li>${policyHolderDetail.addressLine1}</li>
	            <li>${policyHolderDetail.addressLine2}</li>
	            <li>${policyHolderDetail.addressCity}</li>
	            <li>${policyHolderDetail.postcode}</li>
	            <li>${policyHolderDetail.addressCountry}</li>
	        </ul>
	        <c:if test="${not empty policyData.travellers}">
	            <h3 class="borderHeadlineBottom"><spring:theme code="checkout.orderConfirmation.details.beneficiaries" text="Travellers"/></h3>
	            <c:forEach items="${policyData.travellers}" var="traveller" varStatus="status">
	                <ul class="traveller">
	                    <li class="name"><spring:theme code="checkout.orderConfirmation.details.traveller"
	                                    text="Traveller"/>&nbsp;${status.count}</li>
	                    <li>${traveller.firstName}&nbsp;${traveller.lastName}</li>
	                    <li><spring:theme code="checkout.orderConfirmation.details.age" text="Age:"/>&nbsp;${traveller.age}</li>
	                </ul>
	            </c:forEach>
	        </c:if>
	    </div>
    </div>
</c:if>

