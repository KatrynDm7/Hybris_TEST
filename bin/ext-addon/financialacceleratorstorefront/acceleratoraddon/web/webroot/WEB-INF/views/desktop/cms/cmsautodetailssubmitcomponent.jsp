<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>

<div class="autoInfos">
    <c:choose>
        <c:when test="${param.viewStatus=='view'}">
            <table><tr>
            	<tr>
					<td>
						<spring:theme code="text.insurance.auto.vehicle.make" text="Vehicle Make:"/>&nbsp;${sessionScope.vehicleMake}
					</td>
					<td>
						<spring:theme code="text.insurance.auto.vehicle.model" text="Vehicle Model:"/>&nbsp;${sessionScope.vehicleModel}
					</td>
				</tr>
				<tr>
					<td>
						<spring:theme code="text.insurance.auto.vehicle.value" text="Vehicle Value:"/>&nbsp;${sessionScope.vehicleValue}
					</td>
					<td>
							<spring:theme code="text.insurance.auto.vehicle.license" text="Vehicle License:"/>&nbsp;${sessionScope.vehicleLicense}
					</td>
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
	                <a id="continueBtn" class="button positive right show_processing_message" href="?viewStatus=view"><spring:theme code="text.cmsformsubmitcomponent.find.prices" text="Find Prices"/></a>
	            </ycommerce:testId>
            </div>      
      </c:otherwise>
</c:choose>
</div>


