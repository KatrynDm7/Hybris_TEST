<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<div class="account-section-header"><spring:theme code="text.account.paymentDetails" text="Payment Details" /></div>
<div class="account-section-content	 account-section-content-small ">
	<div class="account-paymentdetails">
		<c:choose>
			<c:when test="${not empty paymentInfoData}">
				<div class="account-paymentdetails-list container">
					<c:forEach items="${paymentInfoData}" var="paymentInfo">
						<div class="col-md-6 col-lg-4 accountPaymentDetailsItem">
							<div class="remove">
								<ycommerce:testId code="paymentDetails_deletePayment_button" >
									<a class="btn btn-default removePaymentDetailsButton" data-payment-id="${paymentInfo.id}" data-popup-title="<spring:theme code="text.account.paymentDetails.delete.popup.title" text="Delete Payment"/>">
										<span class="glyphicon glyphicon-trash"></span>
									</a>
								</ycommerce:testId>
							</div>
							
							<c:if test="${paymentInfo.defaultPaymentInfo}" ><strong></c:if>
							${fn:escapeXml(paymentInfo.accountHolderName)}<c:if test="${paymentInfo.defaultPaymentInfo}" >&nbsp;(<spring:theme code="text.default" text="Default" />)</c:if>
							<br>${fn:escapeXml(paymentInfo.cardTypeData.name)}
							<br><ycommerce:testId code="paymentDetails_item_cardNumber_text" >${fn:escapeXml(paymentInfo.cardNumber)}</ycommerce:testId>
							<br><c:if test="${paymentInfo.expiryMonth lt 10}">0</c:if>${fn:escapeXml(paymentInfo.expiryMonth)}&nbsp;/&nbsp;${fn:escapeXml(paymentInfo.expiryYear)}
							
							<c:if test="${paymentInfo.billingAddress ne null}">
								<br>${fn:escapeXml(paymentInfo.billingAddress.line1)}
								<br>${fn:escapeXml(paymentInfo.billingAddress.town)}&nbsp;${fn:escapeXml(paymentInfo.billingAddress.region.isocodeShort)}
								<br>${fn:escapeXml(paymentInfo.billingAddress.country.name)}&nbsp;${fn:escapeXml(paymentInfo.billingAddress.postalCode)}
							</c:if>
							<c:if test="${paymentInfo.defaultPaymentInfo}" ></strong></c:if>
							
							<div class="actions">
								<c:if test="${not paymentInfo.defaultPaymentInfo}" >
									<c:url value="/my-account/set-default-payment-details" var="setDefaultPaymentActionUrl"/>
									<form:form id="setDefaultPaymentDetails${paymentInfo.id}" action="${setDefaultPaymentActionUrl}" method="post">
										<input type="hidden" name="paymentInfoId" value="${paymentInfo.id}"/>
										<ycommerce:testId code="paymentDetails_setAsDefault_button" >
											<button type="submit" class="btn btn-default">
												<spring:theme code="text.setDefault" text="Set as Default" />
											</button>
										</ycommerce:testId>
									</form:form>
								</c:if>
							</div>
						</div>
						<div style="display:none">
							<div id="popup_confirm_payment_removal_${paymentInfo.id}">
								<spring:theme code="text.account.paymentDetails.delete.following" text="The following payment method will be deleted"/>	
								<br />
								<strong>
									<br>${fn:escapeXml(paymentInfo.accountHolderName)}
									<br>${fn:escapeXml(paymentInfo.cardTypeData.name)}
									<br>${fn:escapeXml(paymentInfo.cardNumber)}
									<br><c:if test="${paymentInfo.expiryMonth lt 10}">0</c:if>${fn:escapeXml(paymentInfo.expiryMonth)}&nbsp;/&nbsp;${fn:escapeXml(paymentInfo.expiryYear)}
									
									<c:if test="${paymentInfo.billingAddress ne null}">
										<br>${fn:escapeXml(paymentInfo.billingAddress.line1)}
										<br>${fn:escapeXml(paymentInfo.billingAddress.town)}&nbsp;${fn:escapeXml(paymentInfo.billingAddress.region.isocodeShort)}
										<br>${fn:escapeXml(paymentInfo.billingAddress.country.name)}&nbsp;${fn:escapeXml(paymentInfo.billingAddress.postalCode)}
									</c:if>
								</strong>
								<c:url value="/my-account/remove-payment-method" var="removePaymentActionUrl"/>
								<form:form id="removePaymentDetails${paymentInfo.id}" action="${removePaymentActionUrl}" method="post">
									<input type="hidden" name="paymentInfoId" value="${paymentInfo.id}"/>
									<br />
									<div class="container paymentsDeleteActions">
										<ycommerce:testId code="paymentDetailsDelete_delete_button" >
											<button type="submit" class="btn btn-default btn-primary col-xs-12 paymentsDeleteBtn">
												<spring:theme code="text.account.paymentDetails.delete" text="Delete"/>
											</button>
										</ycommerce:testId>
										<a class="btn btn-default closeColorBox col-xs-12 paymentsDeleteBtn" data-payment-id="${paymentInfo.id}">
											<spring:theme code="text.button.cancel" text="Cancel"/>
										</a>
									</div>
								</form:form>
							</div>
						</div>
					</c:forEach>
				</div>
			</c:when>
			<c:otherwise>
				<p><spring:theme code="text.account.paymentDetails.noPaymentInformation" text="No Saved Payment Details"/></p>
			</c:otherwise>
		</c:choose>
	</div>
</div>
