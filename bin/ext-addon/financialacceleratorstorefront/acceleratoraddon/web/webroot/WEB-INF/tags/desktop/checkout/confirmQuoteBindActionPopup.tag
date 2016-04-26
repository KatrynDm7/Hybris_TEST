<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div id="popup_confirm_bind_quote_wrapper" style="display:none">
    <div id="popup_confirm_bind_quote">
        <div class="confirmation">
            <h3><spring:theme code="text.confirm.quote.notification" text="Confirm Quote"/></h3>
            <spring:theme code="checkout.multi.quoteReview.certify.description"
                          text="By clicking on Continue I certify the information in this application is true and correct."/>
            <div class="buttons">
                <a id="cancelButton" class="button negative">
                    <spring:theme code="text.dialog.cancel" text="Cancel"/>
                </a>
                <a id="yesButton" class="button positive changePlanConfirmButton">
                    <spring:theme code="text.dialog.confirm" text="Continue"/>
                </a>
            </div>
        </div>
    </div>
</div>
