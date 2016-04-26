<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div class="headline">
	<spring:theme code="text.account.profile" text="Profile"/>
</div>
<table class="account-profile-data">
	<tr>
		<td>${fn:escapeXml(customerData.firstName)}&nbsp;${fn:escapeXml(customerData.lastName)}</td>
		<td><a class="button secondary" href="update-profile"><spring:theme code="text.account.profile.updatePersonalDetails" text="Update personal details"/></a></td>
	</tr>
	<tr>
		<td>${fn:escapeXml(customerData.displayUid)}</td>
		<td><a class="button secondary" href="update-email"><spring:theme code="text.account.profile.updateEmail" text="Update email"/></a></td>
	</tr>
	<tr>
		<td>*******</td>
		<td><a class="button secondary" href="update-password"><spring:theme code="text.account.profile.changePassword" text="Change password"/></a></td>
	</tr>
	
</table> 

