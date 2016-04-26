<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="confirmActionButtonId" required="true" type="java.lang.String"%>
<%@ attribute name="cartData" required="true" type="de.hybris.platform.commercefacades.order.data.CartData"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div id="popup_confirm_plan_removal_wrapper" style="display:none">
    <div id="popup_confirm_plan_removal">
        <div class="confirmation">
            <h3><spring:theme code="text.plan.confirm.change.plan" text="Confirm change plan"/></h3>
            <spring:theme code="text.plan.remove.confirmation" text="Changing product plans may alter the form questions. Do you still wish to proceed?"/>
            <div class="buttons">
                <a id="cancelButton" class="button negative">
                    <spring:theme code="text.dialog.cancel" text="Cancel"/>
                </a>   
                <a id="saveProceedButton" class="button positive changePlanConfirmButton">
                    <spring:theme code="text.plan.save.proceed" text="Save and Proceed"/>
                </a>
            </div>
            <div class="saveData">
                <input type="checkbox" id="changePlanStoreFormDataCheckbox" checked>
                <spring:theme code="text.save.form.data" text="If possible, I would like to save the information I have already entered."/>
            </div>
        </div>
    </div>
</div>