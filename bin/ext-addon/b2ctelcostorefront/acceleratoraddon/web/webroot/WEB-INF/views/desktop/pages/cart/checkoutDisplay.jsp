<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/desktop/cart" %>

<div class="checkoutActions">
    <a href="${continueShoppingUrl}">
        <spring:theme text="Continue Shopping" code="cart.page.continue"/>
    </a>
	<c:choose>
		<c:when test="${cartData.cartInvalidMessage != null}">
		  	<button id="checkoutButtonBottom" class="positive right cart-invalid-button" type="button" onclick="ACC.cart.showCartError()">
				<spring:theme code="checkout.checkout" />
			</button>
				<!-- This contains the hidden content for inline calls -->
				<div class="hidden">
					<div id="cart_error_pop">
						<div class="small_popup_container">
							<div class="small_popup_big_headline">
								<div class="small_popup_emark">
								</div>
								<div class="small_popup_headline_text">
									<spring:theme code="basket.page.notification" text="Notification"/>
								</div>
								<div class="clear"></div>
							</div>
							<div class="small_popup_content">
								<div class="clear"></div>	
								<p>
									<c:if test="${cartData.cartInvalidMessage != null}">
									  ${cartData.cartInvalidMessage}
									</c:if>
								</p>
								<div><spring:theme code="basket.page.message.howProceed"/></div>
                                <div class="actions">
                                    <div class="invalid-bundle-wizard">
                                        <c:forEach var="invalidMap" items="${cartData.firstIncompleteBundleComponentsMap}">
                                            <a href="<c:url value="/bundle/edit-component/${invalidMap.key}/component/${invalidMap.value.id}"/>" class="invalid-bundle-wizard-editable button positive">
                                                <spring:theme code="basket.page.message.return" arguments="${invalidMap.value.name}"/>
                                            </a>
                                        </c:forEach>
                                    </div>
                                    <div class="invalid-bundle-returntocart cancel" onclick="jQuery.colorbox.close();">
                                        <spring:theme code="basket.page.message.returnCheckout"/>
                                    </div>
                                </div>
							</div>
						</div>
					</div>
				</div>
		</c:when>
		<c:otherwise>
		    <button id="checkoutButtonBottom" class="doCheckoutBut positive right continueCheckout" type="button" data-checkout-url="${checkoutUrl}">
		        <spring:theme code="checkout.checkout" />
		    </button>
		    <cart:cartExpressCheckoutEnabled />
		</c:otherwise>
	</c:choose>
</div>
<c:if test="${showCheckoutStrategies && not empty cartData.entries}" >
	<div class="span-24">
		<div class="right">
			<input type="hidden" name="flow" id="flow"/>
			<input type="hidden" name="pci" id="pci"/>
			<select id="selectAltCheckoutFlow" class="doFlowSelectedChange">
				<option value="multistep"><spring:theme code="checkout.checkout.flow.select"/></option>
				<option value="multistep"><spring:theme code="checkout.checkout.multi"/></option>
				<option value="multistep-pci"><spring:theme code="checkout.checkout.multi.pci"/></option>
			</select>
			<select id="selectPciOption" class="hidden">
				<option value=""><spring:theme code="checkout.checkout.multi.pci.select"/></option>
				<c:if test="${!isOmsEnabled}">
					<option value="hop"><spring:theme code="checkout.checkout.multi.pci-hop"/></option>
				</c:if>
				<option value="sop"><spring:theme code="checkout.checkout.multi.pci-sop" text="PCI-SOP" /></option>
			</select>
		</div>
	</div>
</c:if>
