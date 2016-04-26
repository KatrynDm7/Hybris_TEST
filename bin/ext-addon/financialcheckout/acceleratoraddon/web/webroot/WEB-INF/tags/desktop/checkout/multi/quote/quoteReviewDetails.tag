<%@ attribute name="insuranceQuoteReviews" required="true" type="java.util.List<de.hybris.platform.commercefacades.insurance.data.InsuranceQuoteReviewData>" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ attribute name="isValidStep" required="false" type="java.lang.Boolean" %>

<c:if test="${not empty insuranceQuoteReviews}">
    <c:forEach items="${insuranceQuoteReviews}" var="quoteReview">
        <c:if test="${not empty quoteReview.policyHolderDetail}">
            <c:set var="policyHolderDetail" value="${quoteReview.policyHolderDetail}"/>
            <c:choose>
                <c:when test="${isValidStep == 'true'}"> <c:set var="isValidClass" value="valid js-toggle"/></c:when>
                <c:otherwise><c:set var="isValidClass" value="invalid"/></c:otherwise>
            </c:choose>
            <h2 class="${isValidClass}">
                <c:if test="${cartData.insuranceQuote.state eq 'UNBIND'}">
                    <spring:theme code="checkout.multi.quoteReview.details.policy.holder.details" text="Policyholder Details"/>
                </c:if>
                <c:if test="${cartData.insuranceQuote.state eq 'BIND'}">
                    <spring:theme code="checkout.multi.quoteReview.details.your.details" text="Your Details"/>
                </c:if>
            </h2>
            <span class="open js top right" data-open="quoteReviewYourDetails"></span>

            <div class="hidden" id="quoteReviewYourDetails">
                <h3><spring:theme code="checkout.multi.quoteReview.details.policy.holder" text="Policy Holder"/></h3>
                <ul class="policyHolder">

                    <li class="name"><c:if test="${not empty policyHolderDetail.title}">${policyHolderDetail.title}. </c:if>${policyHolderDetail.firstName} ${policyHolderDetail.lastName}</li>
                   	
                   	<c:if test="${not empty policyHolderDetail.maritalStatus}">
                    	<li class="name"><spring:theme code="checkout.multi.quoteReview.details.policy.holder.maritalStatus"/> : ${policyHolderDetail.maritalStatus}</li>
                    </c:if>
                    
                    <c:if test="${not empty policyHolderDetail.emailAddress}">
                    	<li class="name"><spring:theme code="checkout.multi.quoteReview.details.policy.holder.emailAddress"/> : ${policyHolderDetail.emailAddress}</li>
                    </c:if>
                    
                    <c:if test="${not empty policyHolderDetail.phoneNumber}">
                        <li class="name"><spring:theme code="checkout.multi.quoteReview.details.policy.holder.phoneNumber"/> : ${policyHolderDetail.phoneNumber}</li>
                    </c:if>

                   	<c:if test="${not empty policyHolderDetail.homePhoneNumber}">
                    	<li class="name"><spring:theme code="checkout.multi.quoteReview.details.policy.holder.homePhoneNumber"/> : ${policyHolderDetail.homePhoneNumber}</li>
                    </c:if>
                    
                    <c:if test="${not empty policyHolderDetail.mobilePhoneNumber}">
                    	<li class="name"><spring:theme code="checkout.multi.quoteReview.details.policy.holder.mobilePhoneNumber"/> : ${policyHolderDetail.mobilePhoneNumber}</li>
                    </c:if>
                    
                    <c:if test="${not empty policyHolderDetail.homePhoneNumber or policyHolderDetail.mobilePhoneNumber}">
                    	<li class="name"><spring:theme code="checkout.multi.quoteReview.details.policy.holder.preferredContactNumber"/> : ${policyHolderDetail.preferredContactNumber}</li>
                    </c:if>
                    
                    <c:if test="${not empty policyHolderDetail.propertyCorrespondenceAddress }">
                    	<li class="name"><spring:theme code="checkout.multi.quoteReview.details.policy.holder.property.correspondance.address"/> : ${policyHolderDetail.propertyCorrespondenceAddress}</li>
                    </c:if>
                    
                    <c:if test="${empty policyHolderDetail.propertyCorrespondenceAddress or policyHolderDetail.propertyCorrespondenceAddress eq 'no' }">
	                    <li>${policyHolderDetail.addressLine1}</li>
	                    <li>${policyHolderDetail.addressLine2}</li>
	                    <li>${policyHolderDetail.addressCity}</li>
	                    <li>${policyHolderDetail.postcode}</li>
	                    <li>${policyHolderDetail.addressCountry}</li>
                    </c:if>
                </ul>
                <c:if test="${not empty quoteReview.travellers}">
                    <hr/>
                    <c:forEach items="${quoteReview.travellers}" var="traveller" varStatus="status">
                        <ul class="traveller">
                            <li class="name"><spring:theme code="checkout.multi.quoteReview.details.traveller" text="Traveller"/>&nbsp;${status.count}</li>
                            <li>${traveller.firstName}&nbsp;${traveller.lastName}</li>
                            <li><spring:theme code="checkout.multi.quoteReview.details.age" text="Age:"/>&nbsp;${traveller.age}</li>
                        </ul>
                    </c:forEach>
                </c:if>

                <c:if test="${cartData.insuranceQuote.state eq 'UNBIND'}">
                    <spring:url var="updatePersonalDetailsUrl" value="/checkout/multi/form"/>
                    <div class="edit">
                        <a class="button" href="${updatePersonalDetailsUrl}"><spring:theme code="text.cmsformsubmitcomponent.edit" text="Edit"/></a>
                    </div>
                </c:if>
            </div>
        </c:if>
    </c:forEach>
</c:if>
