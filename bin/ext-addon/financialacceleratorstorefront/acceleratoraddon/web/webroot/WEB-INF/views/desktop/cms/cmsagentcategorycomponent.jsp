<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="agent" tagdir="/WEB-INF/tags/addons/financialacceleratorstorefront/desktop/agent"%>

<c:if test="${not empty agents}">
    <div class="agent-category">

        <c:url value="${category.getUrl()}" var="linkUrl"/>
        <a href="${linkUrl}" ${target}>
            ${category.getName()}
        </a>

        <c:forEach items="${agents}" var="person">
            <agent:agentInfo agent="${person}"/>
        </c:forEach>
    </div>
</c:if>
