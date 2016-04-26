<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>

<div class="lifeInfos">
    <c:choose>
        <c:when test="${param.viewStatus=='view'}">
           <table>
	            <tr>
		            <td>
		            	<c:if test="${not empty sessionScope.lifeWhoCovered}">
	                          <spring:theme code="text.insurance.life.covered" text="Who is being covered:"/>&nbsp;${sessionScope.lifeWhoCovered}
	                     </c:if>
		            </td>
		            
		            <td>
		            	 <c:if test="${not empty sessionScope.lifeCoverageRequire}">
	                         <spring:theme code="text.insurance.life.covered.required" text="How much life insurance coverage do you require:"/>&nbsp;${sessionScope.lifeCoverageRequire}
	                      </c:if>
		            </td>
	            </tr>
	            	
	            <tr>
	            	<td>
		            	<c:if test="${not empty sessionScope.lifeCoverageLast}">
	                        <spring:theme code="text.insurance.life.coverage.last" text="How many years do you want the coverage to last:"/>&nbsp;${sessionScope.lifeCoverageLast}
	                    </c:if>
		            </td>
		            
		            <td>
		            	 <c:if test="${not empty sessionScope.lifeCoverageStartDate}">
	                          <spring:theme code="text.insurance.life.coverage.start.date" text="Coverage start date:"/>&nbsp;${sessionScope.lifeCoverageStartDate}
	                      </c:if>
		            </td>
	            </tr>
	            
	            <tr>
	            	<td>
		            	<c:if test="${not empty sessionScope.lifeMainDob}">
	                        <spring:theme code="text.insurance.life.date.of.birth" text="Your date of birth:"/>&nbsp;${sessionScope.lifeMainDob}
	                    </c:if>
		            </td>
		            
		            <td>
		            	 <c:if test="${not empty sessionScope.lifeMainSmoke}">
	                          <spring:theme code="text.insurance.life.main.smoke" text="Do you smoke:"/>&nbsp;${sessionScope.lifeMainSmoke}
	                      </c:if>
		            </td>
	            </tr>
	            <c:if test="${sessionScope.lifeWhoCovered eq 'yourself and second person'}">
		            <tr>
		            	<td>
			             <c:if test="${not empty sessionScope.lifeSecondDob}">
	                          <spring:theme code="text.insurance.life.second.date.of.birth" text="Second persons date of birth:"/>&nbsp;${sessionScope.lifeSecondDob}
	                      </c:if>
			            </td>
			            
			            <td>
			            	<c:if test="${not empty sessionScope.lifeSecondSmoke}">
	                            <spring:theme code="text.insurance.life.second.smoke" text="Dose the second person smoke:"/>&nbsp;${sessionScope.lifeSecondSmoke}
	                        </c:if>
			            </td>
		            </tr>
		            <tr>
		            	<td>
		            	 <c:if test="${not empty sessionScope.lifeRelationship}">
	                          <spring:theme code="text.insurance.life.relationship" text="Relationship to the second person:"/>&nbsp;${sessionScope.lifeRelationship}
	                      </c:if>
		            	</td>
		            </tr>
		            
	             </c:if>
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


