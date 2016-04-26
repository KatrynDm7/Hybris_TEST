<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/responsive/formElement" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>


<div class="account-section-header">Update Email Address</div>
<div class="account-section-content account-section-content-small">
	<form:form action="update-email" method="post" commandName="updateEmailForm">
		<formElement:formInputBox idKey="profile.email" labelKey="profile.email" path="email" inputCSS="text" mandatory="true"/>
		<formElement:formInputBox idKey="profile.checkEmail"  labelKey="profile.checkEmail" path="chkEmail" inputCSS="text" mandatory="true"/>
		<formElement:formPasswordBox idKey="profile.pwd" labelKey="profile.pwd" path="password" inputCSS="text form-control" mandatory="true"/>
		<input type="hidden" id="recaptchaChallangeAnswered" value="${requestScope.recaptchaChallangeAnswered}"/>	
		<div class="form_field-elements control-group js-recaptcha-captchaaddon"></div>
		<div class="form-actions">
			<div class="container accountActions">
				<div class="col-sm-6 col-sm-push-6 accountButtons">
					<ycommerce:testId code="email_saveEmail_button">
						<button type="submit" class="btn btn-primary btn-block">
							<spring:theme code="text.account.profile.saveUpdates" text="Save Updates"/>
						</button>
					</ycommerce:testId>
				</div>
				<div class="col-sm-6 col-sm-pull-6 accountButtons">
					<ycommerce:testId code="email_cancelEmail_button">
						<button type="button" class="btn btn-default btn-block backToHome">
							<spring:theme code="text.account.profile.cancel" text="Cancel"/>
						</button>
					</ycommerce:testId>
				</div>
			</div>
		</div>
	</form:form>
</div>