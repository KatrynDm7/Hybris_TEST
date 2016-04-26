<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:choose>
      <c:when test="${param.viewStatus=='view'}">

<div class="${component.itemtype}">
    <div class="header">
        <ul class="multiTabs">
            <c:if test="${fn:length(tabComponents) gt 1}">
                <c:forEach items="${tabComponents}" var="tabComponent" varStatus="status">
                    <li id="${tabComponent.uid}" class="tab <c:if test="${status.count == 1}">active</c:if>">
                        <div>
                             ${tabComponent.title}
                        </div>
                    </li>
                </c:forEach>
            </c:if>
        </ul>
    </div>
    <div id="tab_content" class="content">
        <c:set var="initial_content" value="false"/>
        <c:forEach items="${component.simpleCMSComponents}" var="tabComponent">
            <c:if test="${tabComponent.visible && initial_content != true}">
                <c:set var="initial_content" value="true"/>
                <cms:component component="${tabComponent}" evaluateRestriction="true"/>
            </c:if>
        </c:forEach>
    </div>
</div>

      </c:when>
      <c:otherwise>


      </c:otherwise>
</c:choose>    
