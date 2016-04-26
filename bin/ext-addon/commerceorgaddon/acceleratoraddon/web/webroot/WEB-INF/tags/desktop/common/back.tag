<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring"  uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ attribute name="cancelUrl" required="true" type="java.lang.String"%>

<div class="last right">
	<a class="back_button" href="${cancelUrl}">
		<button id="backButton" class="form" type="submit">
			<spring:theme code="text.back" text="Back"/>
		</button>
	</a>
</div>