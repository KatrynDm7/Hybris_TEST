<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>

<div id="converageConntent">

	<c:if test="${isSessionExpires eq 'true'}">
		<div id="isSessionExpires">${isSessionExpires}</div>
	</c:if>
	
	<cms:component component="${component}" evaluateRestriction="true"/>
	
</div>