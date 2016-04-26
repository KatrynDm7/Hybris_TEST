<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/desktop/nav" %>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/desktop/formElement"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<h2>
	<spring:theme code="text.account.replenishment.remove.confirmation.title" arguments="${arguments}"/>
</h2>

<p>
	<spring:theme code="text.account.replenishment.confirm.cancel" arguments="${arguments}"/>

	<form:form action="${disableUrl}">
		<a class="cancel_button" href="${cancelUrl}">
			<button type="button" class="form">
				<spring:theme code="text.account.no.button"/>
			</button>
		</a>
		<button type="submit" class="form">
			<spring:theme code="text.account.yes.button"/>
		</button>
	</form:form>
</p>
				
