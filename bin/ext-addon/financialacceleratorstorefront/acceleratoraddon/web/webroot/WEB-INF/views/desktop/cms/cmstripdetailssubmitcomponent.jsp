<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>

<div class="tripInfos">
<c:choose>
      <c:when test="${param.viewStatus eq 'view'}">
			<table >
				<tr>
					<td>${sessionScope.tripDestination}</td>
					<td>
						<ul>
							<li><spring:theme code="text.cmstripdetailssubmitcomponent.depart"/>&nbsp;${sessionScope.tripStartDate}</li>
							<li><spring:theme code="text.cmstripdetailssubmitcomponent.return"/>&nbsp;${sessionScope.tripEndDate}</li>
						</ul>
					</td>
					<td>
						<ul>
							<li><spring:theme code="text.cmstripdetailssubmitcomponent.number.of.travellers"/>&nbsp;${sessionScope.Travellers}</li>
							<li>
								<spring:theme code="text.cmstripdetailssubmitcomponent.ages.of.travellers"/>&nbsp;
								<c:forEach var="travellerAge" items="${sessionScope.tripDetailsTravellerAges}" varStatus="status">
									<c:if test="${status.index ne 0 }">,&nbsp;</c:if>${travellerAge}
								</c:forEach>
							</li>
						</ul>
						
					</td>					
				<tr>
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
                <a id="continueBtn" class="button positive right show_processing_message" href="?viewStatus=view"><spring:theme code="text.cmsformsubmitcomponent.find.prices" text="Find Prices"/></a>
            </ycommerce:testId>
            </div>      
      </c:otherwise>
</c:choose>
</div>


