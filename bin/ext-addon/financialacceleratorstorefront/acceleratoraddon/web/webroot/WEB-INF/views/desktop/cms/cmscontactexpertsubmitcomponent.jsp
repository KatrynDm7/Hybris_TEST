<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div class="contactExpert">
      <c:if test="${not empty param.agent}">
      	  	<div id="form_button_panel">
            <ycommerce:testId code="multicheckout_saveForm_button">
                <a class="button positive left" href="find-agent?activeCategory=${fn:escapeXml(activeCategory)}">
                   <spring:theme code="text.agent.contactExpert.back" text="Back"/>
                </a>
                <a id="continueBtn" class="button positive right show_processing_message" href="?sendStatus=send&agent=${fn:escapeXml(param.agent)}&activeCategory=${fn:escapeXml(activeCategory)}">
                   <spring:theme code="text.findAgent.contactExpert.send" text="Send"/>
                </a>
            </ycommerce:testId>
            </div>      
      </c:if>
</div>
