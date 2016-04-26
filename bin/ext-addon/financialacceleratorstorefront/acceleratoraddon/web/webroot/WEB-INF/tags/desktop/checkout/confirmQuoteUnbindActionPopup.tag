<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div id="popup_confirm_unbind_quote_wrapper" style="display:none">
    <div id="popup_confirm_unbind_quote">
        <div class="confirmation">
            <h3><spring:theme code="text.confirm.change.quote.notification" text="Change Quote Notification"/></h3>
            <spring:theme code="text.change.quote.notification.text" text="Making changes to your quote application could alter the quote price"/>
            <div class="buttons">
                <a id="yesButton" class="button positive changePlanConfirmButton">
                    <spring:theme code="text.dialog.confirm" text="Continue"/>
                </a>
            </div>
        </div>
    </div>
</div>