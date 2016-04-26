<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="address" tagdir="/WEB-INF/tags/responsive/address"%>

<spring:url value="/my-account/address-book" var="addressBookUrl"/>

<button type="button" class="btn btn-default addressBackBtn" data-back-to-addresses="${addressBookUrl}">
	<span class="glyphicon glyphicon-chevron-left"></span><spring:theme code="text.account.addressBook.back.btn" text=" Back"/>
</button>
<c:choose>
	<c:when test="${edit eq true }">
		<div class="account-section-header"><spring:theme code="text.account.addressBook.updateAddress" text="Update Address"/></div>
	</c:when>
	<c:otherwise>
		<div class="account-section-header"><spring:theme code="text.account.addressBook.addAddress" text="New Address"/></div>
	</c:otherwise>
</c:choose>
<div class="account-section-content	 account-section-content-small">
	<address:addressFormSelector supportedCountries="${countries}" regions="${regions}" cancelUrl="/my-account/address-book" addressBook="true" />
</div>
