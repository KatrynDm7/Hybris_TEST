<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/desktop/nav"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="breadcrumb" tagdir="/WEB-INF/tags/desktop/nav/breadcrumb"%>
<%@ taglib prefix="action" tagdir="/WEB-INF/tags/desktop/action"%>

<div class="headline">
	<spring:theme code="text.account.myPolicies" text="My Policies" />
</div>

<c:choose>
	<c:when test="${empty policies}">
		<p>
			<spring:theme code="text.account.myPolicies.noPolicies" text="You have no policies" />
		</p>
	</c:when>
	<c:otherwise>
        <p>
        <spring:theme code="text.account.myPolicies.viewYourPolicies" text="View your Policies" />
        </p>
		<table class="policyListTable">
			<tr>
				<th colspan="2" class="planname"><spring:theme code="text.account.myPolicies.policy.name" text="Policy Name"/></th>
				<%--<th><spring:theme code="text.account.myPolicies.policy.name" text="Policy Name"/></th>
				<th><spring:theme code="text.account.myPolicies.policy.number" text="Policy Number"/></th>--%>
				<th></th>
                <th></th>
                <th></th>
                <th></th>
			</tr>
			<c:forEach items="${policies}" var="policy">
                <c:forEach items="${policy.policyImages}" var="image">
                    <c:if test="${image.format == '40Wx40H_policy'}">
                        <c:set var="thumbnail_img" value="${image}"/>
                    </c:if>
                </c:forEach>
				<tr>
                    <td><span class="insImg"><c:if test="${not empty thumbnail_img}"><img src="${thumbnail_img.url}"/></c:if></span></td>
					<td>${policy.policyProduct.defaultCategory.name}<br/>${policy.policyNumber}</td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td><a target="_blank" class="button secondary" href="${policy.policyUrl}"><spring:theme code="text.account.myPolicies.policy.view" text="View Policy"/></a></td>
				</tr>
			</c:forEach>
		</table>
	</c:otherwise>
</c:choose>