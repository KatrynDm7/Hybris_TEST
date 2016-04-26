<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<div class="container">
    <c:forEach items="${simpleCMSComponents}" var="tabComponent" varStatus="step">
        <c:if test="${tabComponent.visible}">
            <cms:component component="${tabComponent}" element="${elementType}" evaluateRestriction="true" id="${idKey}${step.count}" class="${styleCss}"/>
        </c:if>
    </c:forEach>
</div>