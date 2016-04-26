<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>

<%@ attribute name="languages" required="true" type="java.util.Collection" %>
<%@ attribute name="currentLanguage" required="true"
              type="de.hybris.platform.commercefacades.storesession.data.LanguageData" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>

<c:if test="${fn:length(languages) > 1}">
    <form:form action="${setLanguageActionUrl}" method="post" id="lang-form">
        <spring:theme code="text.language" var="languageText"/>
        <div class="control-group">
            <div class="controls">
                <label>${languageText}</label>
                <c:forEach items="${languages}" var="lang">
                    <c:url value="/_s/language" var="setLanguageActionUrl">
                        <c:param name="code" value="${lang.isocode}"/>
                    </c:url>
                    <span><a href="${setLanguageActionUrl}">${lang.nativeName}</a></span>
                </c:forEach>
            </div>
        </div>
    </form:form>
</c:if>
