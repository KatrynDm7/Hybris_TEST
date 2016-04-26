<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:url value="/my-account/my-policies" var="accountUrl"/>
<c:choose>
	<c:when test="${isAnonymousUser == true}">
		<div class="viewPolicies">
			<a class="button special2" href="${accountUrl}"><spring:theme code="homepage.viewpoliciescomponent.anonymoususer.button.label" /></a>
		</div>
	</c:when>
	<c:otherwise>
		<c:if test="${quotesExists or not empty policies}">
			
			<c:set value="${numDisplayablePolicies}" var="maxItems" />
			<c:choose>
				<c:when test="${not empty policies}">
					<div class="myPolicies">
                        <h2><spring:theme code="text.home.viewPolicies" text="View Policies"/></h2>
                        <ul>
							<c:forEach items="${policies}" begin="0" end="${maxItems - 1}" var="policy">
								<c:set var="item" value="${policy.policyProduct}" />
								<c:set var="defaultCategory" value="${item.defaultCategory}" />
								<c:forEach items="${item.images}" var="image">
									<c:if test="${image.format == '40Wx40H_policy'}">
										<c:set var="thumbnail_img" value="${image}" />
									</c:if>
								</c:forEach>
								<a href="${policy.policyUrl}" target="_blank">
									<li class="my-policies">
										<div class="dataContainer">
											<span class="insImg"><img src="${thumbnail_img.url}" /></span>
											<span class="insName">${defaultCategory.name}&nbsp;<spring:theme code="homepage.viewpoliciescomponent.policy.label" /></span>
			                                <span class="insNumber">${policy.policyNumber}</span>
			                                <span class="addInfos">${policy.policyExpiryDate}</span>
										</div>
									</li>
								</a>
							</c:forEach>
						</ul>
						<c:if test="${policies.size() gt maxItems}">
							<p><a href="${accountUrl}"><spring:theme code="homepage.viewpoliciescomponent.too.many" /></a></p>
						</c:if>
					</div>
				</c:when>
				<c:otherwise>
					<div class="viewPolicies nonePolicies">
						<p>
							<spring:theme code="homepage.viewquotescomponent.empty.policy.title1" /><br />
							<spring:theme code="homepage.viewquotescomponent.empty.policy.title2" />
						</p>
					</div>
				</c:otherwise>
			</c:choose>
		</c:if>
	</c:otherwise>
</c:choose>