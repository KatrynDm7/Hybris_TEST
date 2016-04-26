<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/desktop/formElement" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/desktop/cart" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<form:form id="selectPaymentTypeForm" commandName="paymentTypeForm" action="${request.contextPath}/checkout/multi/payment-type/choose" method="post">
	<formElement:formSelectBox idKey="paymentTypeSelect" labelKey="checkout.multi.paymentType" path="paymentType" skipBlank="true" itemValue="code" itemLabel="displayName" items="${paymentTypes}" mandatory="true" />
	<div id="costCenter">
		<formElement:formSelectBox idKey="costCenterSelect" labelKey="checkout.multi.costCenter.label" path="costCenterId" skipBlank="false" skipBlankMessageKey="checkout.multi.costCenter.title.pleaseSelect" itemValue="code" itemLabel="name" items="${costCenters}" mandatory="true" />
	</div>
	<formElement:formInputBox idKey="PurchaseOrderNumber" labelKey="checkout.multi.purchaseOrderNumber.label" path="purchaseOrderNumber" inputCSS="text" />
	<div class="form-actions">
		<a class="button" href="${request.contextPath}/cart"><spring:theme code="checkout.multi.cancel" text="Cancel"/></a>
		<button id="choosePaymentType_continue_button" class="positive right show_processing_message">
			<spring:theme code="checkout.multi.paymentType.continue" text="Next"/>
		</button>
	</div>
</form:form>