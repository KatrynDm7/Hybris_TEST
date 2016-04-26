<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="agent" tagdir="/WEB-INF/tags/addons/financialacceleratorstorefront/desktop/agent"%>

    <c:url value="/find-agent" var="currentURL"/>
    <div class="insurance-category-group">
    <c:set var="category" value="${activeCategory}"/>

        <c:forEach items="${categories}" var="entry">
            <c:choose>
                <c:when test="${entry.category.code == category}">
                    <div class="tab active">
                        <div class="caption">
                            <a href="${currentURL}?activeCategory=${entry.category.code}">
                                ${entry.category.name}
                            </a>
                        </div>

                        <c:forEach items="${entry.agents}" var="person">
                                <agent:agentInfo agent="${person}"/>
                        </c:forEach>

                    </div>
                </c:when>
                <c:otherwise>
                    <div class="tab">
                        <div class="caption">
                            <a href="${currentURL}?activeCategory=${entry.category.code}">
                                ${entry.category.name}
                            </a>
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>
        </c:forEach>

    </div>