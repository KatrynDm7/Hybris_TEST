<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>

    <c:url value="${url}" var="linkUrl"/>
    <div ${component.styleAttributes}>
        <a href="${linkUrl}" ${target}>
            <div class="servicelinks">
                <cms:component component="${component.image}" evaluateRestriction="true"/>
            </div>${component.linkName}
        </a>
    </div>