<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common"%>
<%@ taglib prefix="agent" tagdir="/WEB-INF/tags/addons/financialacceleratorstorefront/desktop/agent"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<template:page pageTitle="${pageTitle}">
    <div id="globalMessages">
        <common:globalMessages/>
    </div>

    <div class="span-24">
	    <c:if test="${not empty param.agent}">
            <div class="xxforms-animate">
                <cms:pageSlot position="Section2A" var="component">
                    <c:if test="${empty thankyou}">
                      <div class="contactExpert">
                         <c:choose>
                            <c:when test="${not empty agentData}">
                               <div class="span-21">
                                   <h2>
                                      <spring:theme code="text.agent.contactExpert.expert.request" 
                                               arguments="${fn:escapeXml(agentData.firstName)} ${fn:escapeXml(agentData.lastName)}" 
                                               argumentSeparator=";" 
                                               text="Contact ${fn:escapeXml(agentData.firstName)} ${fn:escapeXml(agentData.lastName)}" />
                                   </h2>
                               </div>
                               <div class="span-3 last">
                                   <img src="${agentData.thumbnail.url}"/>
                               </div>
                            </c:when>
                            <c:otherwise>
                               <h2><spring:theme code="text.agent.contactExpert.fallback.request" text="Contact Expert" /></h2>
                            </c:otherwise>
                         </c:choose>
                      </div>
                      <cms:component component="${component}"/>
                   </c:if>
                   <c:if test="${not empty thankyou}">
                      <h2>
                         <spring:theme code="text.agent.contactExpert.thankyou" 
                                  arguments="${fn:escapeXml(agentData.firstName)} ${fn:escapeXml(agentData.lastName)}" 
                                  argumentSeparator=";" text="Thank you for your message. ${fn:escapeXml(agentData.firstName)} ${fn:escapeXml(agentData.lastName)} will reply to you shortly!"/>
                      </h2>
                   </c:if>
                </cms:pageSlot>
            </div>
        </c:if>
    </div>
</template:page>