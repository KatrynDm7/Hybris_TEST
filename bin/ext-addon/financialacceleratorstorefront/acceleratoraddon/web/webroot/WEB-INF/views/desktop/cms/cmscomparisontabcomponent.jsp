<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>

<div class="CMSComparisonTabComponent">
    <div class="body">
        <cms:component component="${comparisonPanel}" evaluateRestriction="true"/>
    </div>
</div>
