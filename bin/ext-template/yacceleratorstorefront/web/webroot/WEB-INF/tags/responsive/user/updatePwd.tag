<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/responsive/formElement" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>

<div class="row">
	<div class="col-md-9 col-lg-10">
		<div class="account-section">
			<div class="account-section-header"><spring:theme code="text.account.profile.resetPassword"/></div>
			<div class="account-section-content	 account-section-content-small ">
				<form:form method="post" commandName="updatePwdForm">
					<p><spring:theme code="text.account.profile.resetPasswordDetails"/></p>
					<br />
					<div class="form-group">
							<formElement:formPasswordBox idKey="updatePwd.pwd" labelKey="updatePwd.pwd" path="pwd" inputCSS="form-control password-strength" mandatory="true" />
					</div>
					<div class="form-group">
						<formElement:formPasswordBox idKey="updatePwd.checkPwd" labelKey="updatePwd.checkPwd" path="checkPwd" inputCSS="form-control" mandatory="true" />
					</div>


					<div class="container">
						<div class="col-sm-12 col-md-4 col-md-offset-4 col-md-push-4">
							<button type="submit" class="btn btn-primary btn-block">
								<spring:theme code="text.account.profile.resetPassword" text="Reset Password" />
							</button>
						</div>
					</div>
				</form:form>
			</div>
		</div>
	</div>
</div>
	
