<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>



		<div class="item_container_holder paymentDetails manageSubscriptions">
            <div class="headline"><spring:theme code="text.account.paymentDetails.manageSubscriptions" text="Manage Associated Subscriptions"/></div>

			<div class="item_container">
			    <div class="description">
			        <spring:theme code="text.account.paymentDetails.manageAssociatedSubscriptions"/>
			    </div>

			    <table class="paymentAccount">
			    	<tbody>
						<tr>
							<td>
								<ul>
									<li>${paymentInfo.cardNumber}</li>
									<li>${paymentInfo.cardTypeData.name}</li>
									<li><spring:theme code="text.expires" text="Expires"/>&nbsp;${paymentInfo.expiryMonth}/${paymentInfo.expiryYear}</li>
								</ul>															
							</td>
							<td>
								<ul>
									<li><c:out value="${paymentInfo.billingAddress.title} ${paymentInfo.billingAddress.firstName} ${paymentInfo.billingAddress.lastName}"/></li>
									<li>${paymentInfo.billingAddress.line1}</li>
									<li>${paymentInfo.billingAddress.line2}</li>
									<li>${paymentInfo.billingAddress.town}</li>
									<li>${paymentInfo.billingAddress.postalCode}</li>
									<li>${paymentInfo.billingAddress.country.name}</li>
								</ul>
							</td>
							<td class="actions">
								<ul>
									<li>
										<c:choose>
											<c:when test="${empty subscriptions}">
													<c:url value="/my-account/my-payment-details/edit?paymentInfoId=${paymentInfo.id}&targetArea=accountArea" var="continueToEditPaymentDetailsUrl" />
                                        <a href="${continueToEditPaymentDetailsUrl}" value="edit" id="editWithSubscriptions-edit" class="function_btn">
                                            <spring:theme code="text.edit" text="Edit"/>
                                        </a>
											</c:when>
											<c:otherwise>
         										<c:url value="/my-account/my-payment-details/payment-method-subscriptions?paymentInfoId=${paymentInfo.id}" var="manageSubscriptionsUrl"/>
			                               <a href="#" value="manage" id="editWithSubscriptions-manage" class="function_btn" data-url="${manageSubscriptionsUrl}">
                                            <spring:theme code="text.edit" text="Edit"/>
                                        </a>
											</c:otherwise>
										</c:choose>

										<c:url value="/my-account/my-payment-details/check-remove?paymentInfoId=${paymentInfo.id}" var="removePaymentActionUrl"/>
                                        <a href="#" class="checkSubmitRemove-manage function_btn remove ${empty subscriptions? 'black':'grey'}" data-url="${removePaymentActionUrl}">
                                            <spring:theme code="text.account.paymentDetails.removePaymentDetails" text="Remove"/>
                                        </a>
									</li>
								</ul>
							</td>
						</tr>
					</tbody>
			    </table>
			    
			    <c:choose>
				    <c:when test="${not empty subscriptions}">
				       
				    <div class="infoText"><spring:theme code="text.account.paymentDetails.associatedSubscriptions"/> </div>
	
				    <c:url value="/my-account/my-payment-details/change-payment-method-subscription" var="changePaymentMethodSubscriptionUrl" />
				    <form:form id="idManageAssociatedSubscriptions" class="orderList" method="post" commandName="paymentSubscriptionsForm" action="${changePaymentMethodSubscriptionUrl}">
						    <input type="hidden" name="paymentInfoId" value="${paymentInfo.id}"/>
						
						    <table class="orderListTable">
						        <thead>
						            <tr>
						                <th id="header1"><spring:theme code="text.account.subscription.productName" text="Product Name"/></th>
						                <th id="header2"><spring:theme code="text.account.subscription.startDate" text="Start Date"/></th>
						                <th id="header3"><spring:theme code="text.account.subscription.endDate" text="End Date"/></th>
						                <th id="header4"><spring:theme code="text.account.subscription.status" text="Status"/></th>
						                <c:if test="${not empty subscriptions and not empty paymentMethods}">
						                	<th id="header5"><input type="checkbox" id="selectall"/>&nbsp;&nbsp;<spring:theme code="text.account.subscription.selectAll" text="Select All"/></th>
						                </c:if>
						            </tr>
						        </thead>
						        <tbody>
	
						            <c:forEach items="${subscriptions}" var="subscription" varStatus="loop">
						                <tr>
						                    <td headers="header1">${subscription.name}</td>
						                    <td headers="header2" class="background"><fmt:formatDate type="date" value="${subscription.startDate}"/></td>
						                    <td headers="header3"><fmt:formatDate type="date" value="${subscription.endDate}"/></td>
						                    <td headers="header4" class="background">${subscription.subscriptionStatus}</td>
						                    <c:if test="${not empty paymentMethods}">
						                        <td headers="header5">
						                            <form:checkbox path="subscriptionsToChange" value="${subscription.id}"/>
						                        </td>
						                    </c:if>
						                </tr>
						            </c:forEach>
						        </tbody>
						    </table>
	
						    <div class="infoText">
						    	<spring:theme code="text.account.paymentDetails.change.selectedSubscriptions"/>

						        <c:if test="${empty paymentMethods}">
					               <div class="no-payment">
					                	<spring:theme code="text.account.paymentDetails.remove.changeImpossible" text="Remove impossible"/>
					                	<br/>
					                </div>
						        </c:if>
						    </div>
	
						    <c:if test="${not empty paymentMethods}">
				             <div class="payment-box">
							        <div class="credit-card">${paymentInfo.cardTypeData.name}</div>
							        <spring:theme code="text.account.paymentDetails.payment.details" arguments="${fn:replace(paymentInfo.cardNumber,'*','')}" text="cardNumber"/><br>
						        	<spring:theme code="text.expires" text="Expires"/>:&nbsp;${paymentInfo.expiryMonth}/${paymentInfo.expiryYear}
							    </div>
						  		<div class="change-box right">
								    <div class="actions">								  
								            <table>
								                <tr>
								                    <td>
								                        <form:select id="id_changeToPM" path="newPaymentMethodId" onchange="switchChangeButton();">
								                            <option value="" label="<spring:theme code='text.account.paymentDetails.selectPaymentMethod'/>" selected="selected">
								                            	<spring:theme code="text.account.paymentDetails.selectPaymentMethod" text="Remove impossible"/>
								                            </option>
								                            <form:options items="${paymentMethods}" itemValue="code" itemLabel="name" />
								                        	</form:select>
								                        <button id="buttonChangeTo" type="${buttonTypeUpgrade}" class="function_btn secondary" disabled="disabled">
								                            <spring:theme code="text.account.paymentDetails.changeTo"/>
								                        </button>
								                    </td>
								                </tr>
								            </table>								           
								    </div>
								</div>
							</c:if>
						    
				    </form:form>
			    </c:when>
				 <c:otherwise>
				  	<div class="description">
				  		 <spring:theme code="text.account.paymentDetails.noAssociatedSubscriptions" text="There are currently no subscriptions associated to this payment method."/>
				 	</div>
				 </c:otherwise>
				</c:choose>
				<div class="accountAction">
					<c:url value="/my-account/my-payment-details/add" var="addPaymentMethodUrl" />
					<button type="button" onclick="window.location='${addPaymentMethodUrl}?targetArea=accountArea'" class="function_btn positive right">
				      <spring:theme code="text.account.paymentDetails.addNewPaymentDetails" text="Add New Payment Details"/>
					</button>
	
					<c:url value="/my-account/my-payment-details" var="backToPaymentInfoUrl" />
					<a href="${backToPaymentInfoUrl}" class="r_action_btn cancel">
						<spring:theme code="text.account.paymentDetails.returnToPaymentDetails" text="Return To Payment Details"/>
					</a>
				</div>
			</div>
        </div>
