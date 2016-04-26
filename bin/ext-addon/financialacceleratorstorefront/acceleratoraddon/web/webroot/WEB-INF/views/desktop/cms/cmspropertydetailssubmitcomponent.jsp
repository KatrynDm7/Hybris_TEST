<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>

<div class="propertyInfos">
	<c:choose>
		<c:when test="${param.viewStatus=='view'}">
			<table>
				<tr>
					<c:forEach items="${propertyFormSessionMap}" var="property">
						<tr>
							<c:set var="keyLabel" value="text.${fn:replace(property.key, '_', '.')}"/>
							<td><spring:theme code="${keyLabel}"/></td>
							<td>${property.value}</td>
					</c:forEach>
				</tr>
			</table>
			
			<div id="form_button_panel">
            	<ycommerce:testId code="multicheckout_cancel_button">
	                <a id="backBtn" class="button" href="?viewStatus=edit">
	                    <spring:theme code="text.cmsformsubmitcomponent.edit" text="Edit"/>
	                </a> 
				</ycommerce:testId>                     
      		</div>
		</c:when>
		<c:otherwise>
            <div id="form_button_panel">
                <ycommerce:testId code="multicheckout_saveForm_button">
                    <a id="continueBtn" class="button positive right show_processing_message"
                       href="?viewStatus=view"><spring:theme code="text.cmsformsubmitcomponent.find.prices" text="Find Prices"/></a>
                </ycommerce:testId>
            </div>
        </c:otherwise>
	</c:choose>
</div>