<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="steps" required="true" type="java.util.List" %>
<%@ attribute name="progressBarId" required="true" type="java.lang.String" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<div class="yCmsComponent span-24 section1 cms_disp-img_slot">
    <c:if test="${not empty categoryName}">
    	<ul class="checkoutProgressHeading"><spring:theme code="checkout.progress.bar.insurance.label" text="Your Insurance Quote is in progress" arguments="${categoryName}"/></ul>
	</c:if>
	
    <ul data-role="navbar" id="checkoutProgress" class="steps-${fn:length(steps)}">   
    <c:forEach items="${steps}" var="checkoutStep" varStatus="status">
        <c:url value="${checkoutStep.url}" var="stepUrl"/>
        <c:url value="${requestScope['javax.servlet.forward.servlet_path']}" var="currentUrl"/>
        <c:set var="isEnabled" value="${checkoutStep.isEnabled}"/>
        <c:set var="excludeClickable" value="false" />
        <c:choose>
            <c:when test="${checkoutStep.status eq 'unset'}">
                <c:set var="currentStepIndicator" value="unset" />
            </c:when>
            <c:when test="${checkoutStep.status eq 'invalid'}">
                <c:set var="currentStepIndicator" value="invalid" />
            </c:when>
            <c:otherwise>
                <c:set var="currentStepIndicator" value="valid" />
            </c:otherwise>
        </c:choose>
        <c:set var="isEnabled" value="${checkoutStep.isEnabled}"/>
        <c:choose>
            <c:when test="${isEnabled == true}">
                <c:set var="hrefLink" value="href='${stepUrl}'"/>
            </c:when>
            <c:otherwise>
                <c:set var="hrefLink" value=""/>
            </c:otherwise>
        </c:choose>
        
        <c:choose>
            <c:when test="${checkoutStep.progressBarId eq 'paymentMethod' || checkoutStep.progressBarId eq 'confirmOrder'}">
                <c:set var="excludeClickable" value="true" />
            </c:when>
            <c:otherwise>
                 <c:set var="excludeClickable" value="false" />
            </c:otherwise>
        </c:choose>
        
        <%-- Perhaps need a better way.  But currently we are likely to have 2 entries of the same product. There's no code differenciate them yet.--%>
        <%--<c:when test="${progressBarId eq checkoutStep.progressBarId}">--%>
        <c:choose>
            <c:when test="${currentUrl eq stepUrl or fn:contains(currentUrl, stepUrl)}">
                <c:set scope="page"  var="currentStepActive"  value="${checkoutStep.stepNumber}"/>
                <li class="step active ${currentStepIndicator}">
                    <a ${hrefLink}>
                        <spring:theme code="checkout.multi.${checkoutStep.progressBarId}"/>
                    </a>
                </li>
            </c:when>
            <c:when test="${checkoutStep.stepNumber > currentStepActive }">            
                <li class="step disabled ${currentStepIndicator}">               
                 	<c:choose>                  		
                    	 <c:when test="${(excludeClickable eq 'false') and (currentStepIndicator eq 'valid' || (currentStepActive+1 == checkoutStep.stepNumber))}">	    
                    	             	 
                    	   <a ${hrefLink}>
                        		<spring:theme code="checkout.multi.${checkoutStep.progressBarId}"/>
                    		</a>
                    	 </c:when>
                    	 <c:otherwise>                    	    
	                             <spring:theme code="checkout.multi.${checkoutStep.progressBarId}"/>                      	 
                    	 </c:otherwise>	                    	
                   </c:choose>
                </li>
            </c:when>
            <c:otherwise>
                <li class="step visited ${currentStepIndicator}">                
                    <a ${hrefLink}>
                        <spring:theme code="checkout.multi.${checkoutStep.progressBarId}"/>
                    </a>
                </li>
            </c:otherwise>
        </c:choose>
    </c:forEach>
</ul>
</div>