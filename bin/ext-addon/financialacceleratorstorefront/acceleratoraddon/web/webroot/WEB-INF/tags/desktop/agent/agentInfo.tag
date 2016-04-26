<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="agent" required="true" type="de.hybris.platform.financialfacades.findagent.data.AgentData"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="agent-info">
    <div class="agent-info-column1">
        <img src="${agent.thumbnail.url}"/>
    </div>
    <div class="agent-info-column2">
        <ul>
            <li><b>${agent.firstName}&nbsp;${agent.lastName}</b></li>
            <c:if test="${not empty agent.enquiryData}">
                <ul>
                    <li>${agent.enquiryData.line1}</li>
                    <li>${agent.enquiryData.line2}</li>
                    <li>${agent.enquiryData.town}</li>
                    <li>${agent.enquiryData.region.isocodeShort}&nbsp;${agent.enquiryData.postalCode}</li>
                </ul>
            </c:if>
        </ul>
    </div>
    <div class="agent-info-column3">
        <a id="backBtn" class="button" href="contact-agent?agent=${agent.uid}&activeCategory=${activeCategory}">
            <spring:theme code="text.agent.contactExpert.request" text="Contact"/>
        </a>
    </div>
</div>