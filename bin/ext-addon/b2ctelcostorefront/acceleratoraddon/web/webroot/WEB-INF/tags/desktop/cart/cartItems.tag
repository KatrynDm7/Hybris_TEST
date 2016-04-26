<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="cartData" required="true" type="de.hybris.platform.commercefacades.order.data.CartData" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="telcoycommerce" uri="/WEB-INF/tld/addons/b2ctelcostorefront/ycommercetags.tld" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product" %>
<%@ taglib prefix="telcoProduct" tagdir="/WEB-INF/tags/addons/b2ctelcostorefront/desktop/product" %>
<%@ taglib prefix="storepickup" tagdir="/WEB-INF/tags/desktop/storepickup" %>


    <div id="cartItems" class="clear">
	<div class="headline">
		<spring:theme code="basket.page.title.yourItems"/>
		<span class="cartId"><spring:theme code="basket.page.cartId"/>&nbsp;${cartData.code}</span>
    </div>
	

    <!--  taken as is from telco  start-->
    <c:set var="lastBundleNo" value="-9"/> <!-- some illegal value -->
    <c:set var="firstBundleNo" value="-1"/>
    <c:set var="displayPackageNo" value="false"/>
    <c:forEach items="${cartData.entries}" var="entry">
        <c:if test="${entry.bundleNo > 0 and firstBundleNo < 0}">
             <c:set var="firstBundleNo" value="${entry.bundleNo}"/>
        </c:if>
        <c:if test="${entry.bundleNo > 0 and firstBundleNo > 0 and firstBundleNo != entry.bundleNo}">
             <c:set var="displayPackageNo" value="true"/>
        </c:if>
    </c:forEach>

		<c:forEach var="invalidMap" items="${cartData.firstIncompleteBundleComponentsMap}"> 
			<div class="notification">
				<div class="notification_headline">
					<div class="notification_emark"></div>
					<div class="notification_headline_text">
						<spring:theme code="basket.page.notification" text="Notification"/>
					</div>
					<div class="clear"></div>
				</div>
				<div class="notification_content">
					<h3><spring:theme code="basket.page.message.edit0" arguments="${invalidMap.value.name},${invalidMap.key}"/></h3>
					<div class="clear"></div>	
					<p><spring:theme code="basket.page.message.info"/></p>
					<div class="invalid-bundle-wizard">
						<a href="<c:url value="/bundle/edit-component/${invalidMap.key}/component/${invalidMap.value.id}"/>" class="invalid-bundle-wizard-editable"><spring:theme code="basket.page.message.return" arguments="${invalidMap.value.name}"/></a>
					</div>		
					<div class="clear"></div>				
				</div>
			</div>
		</c:forEach>
		
     <!--  taken as is from telco  end-->
    <c:forEach items="${cartData.entries}" var="entry">


        <!--  taken as is from telco  start-->
        <c:if test="${entry.bundleNo != lastBundleNo}">
            <c:set var="uniqueBundleId" value="${entry.rootBundleTemplate.id }${entry.bundleNo}" />


            <c:if test="${lastBundleNo != -9}">
                </tbody>
                </table>
            </c:if>

            <table class="cart">
                <thead title="<c:choose><c:when test='${not empty entry.rootBundleTemplate.name}'>${entry.rootBundleTemplate.name}</c:when><c:otherwise><spring:theme code='basket.standalone.name'/></c:otherwise></c:choose>">
                   <tr>
                    <th class="cart-bundle-package">
                    <c:choose>
                            <c:when test="${not empty entry.rootBundleTemplate.name}">
                                <span class="cart-bundle-name">
                                    ${entry.rootBundleTemplate.name}
                                    <c:if test="${displayPackageNo == true}">
                                        <span class="cart-bundle-number">(<spring:theme code="basket.page.bundleNo"/> ${entry.bundleNo})</span>
                                    </c:if>
                                </span>
                            </c:when>
                            <c:otherwise>
                              <spring:theme code="basket.standalone.name"/>
                            </c:otherwise>
                    </c:choose>
                    </th>
                    <th class="cart-bundle-product">
                        <spring:theme code="basket.product.name"/>
                    </th>
                    <th class="cart-bundle-quantity">
                        <spring:theme code="basket.page.quantity"/>
                    </th>
                    <c:if test="${ycommerce:checkIfPickupEnabledForStore()}">
                        <th class="cart-bundle-shipping">
                            <spring:theme code="basket.page.shipping"/>
                        </th>
                    </c:if>
                    <c:forEach items="${cartData.orderPrices}" var="tpentry" varStatus="rowCounter">
                      <th class="cart-bundle-itemPrice">
                        ${tpentry.billingTime.name}
                      </th>
                    </c:forEach>
                    <th class="cart-bundle-remove <c:if test='${entry.updateable}'>updateable-items</c:if>">
                    <%-- this will only be displayed for a bundle --%>
                        <c:choose>
                            <c:when test="${not empty entry.rootBundleTemplate.name}">
                                <c:url var="bundleDeleteAction" value="/cart/delete" />
                                <form:form id="deleteBundleForm${uniqueBundleId}" action="${bundleDeleteAction}" method="post" commandName="deleteBundleForm${uniqueBundleId}">
                                    <input type="hidden" name="bundleNo" value="${entry.bundleNo}"/>
                                    <ycommerce:testId code="cart_product_removeBundle">
                                        <spring:theme code="text.iconCartRemove" var="iconCartRemove"/>
                                        <a href="javascript:ACC.cart.submitBundleRemove('${uniqueBundleId}');" title="Remove Bundle" id="RemoveProduct_${entry.entryNumber}" class="submitRemoveBundle">${iconCartRemove}</a>
                                    </ycommerce:testId>
                                </form:form>
                            </c:when>
                            <c:otherwise><spring:theme code="text.iconCartRemove"/></c:otherwise>
                        </c:choose>
                    </th>
                   </tr>
                    <c:set var="lastBundleNo" value="${entry.bundleNo }"/>
                </thead>
                <tbody>
        </c:if>

        <c:url value="${entry.product.url}" var="productUrl"/>
        <tr class="cartItem">
            <td class="cart-bundle-package">
                ${entry.component.name}
           <c:if test="${entry.editable}">
                <c:if test="${entry.valid}">
                  <a href="<c:url value="/bundle/edit-component/${entry.bundleNo}/component/${entry.component.id}"/>" class="cart-bundle-editable"><spring:theme code="basket.page.edit" text="Edit"/></a>
               </c:if>
           </c:if>
                <c:if test="${entry.bundleNo == 0}">
                    <a href="${productUrl}"> <product:productPrimaryImage product="${entry.product}" format="thumbnail" /></a>
                </c:if>
            </td>
                <td class="cart-bundle-product">
                    <c:if test="${entry.bundleNo > 0}">
						<c:set value="${ycommerce:productImage(entry.product, 'thumbnail')}" var="primaryImage"/>
						<c:if test="${not empty primaryImage}">		
							<a href="${productUrl}">
								<product:productPrimaryImage product="${entry.product}" format="thumbnail"/>
							</a>
						</c:if>
                    </c:if>
                    <ycommerce:testId code="cart_product_name">
                        <div class="itemName"><a href="${productUrl}">${entry.product.name}</a></div>
                        <div class="cart-product-entitlements">
                            <telcoProduct:productEntitlements product="${entry.product}"/>
                        </div>
                        <c:if test="${not empty entry.entryMessage}">
                            <div class="cart-entry-message">${entry.entryMessage}</div>
                        </c:if>
                    </ycommerce:testId>

                    <c:set var="entryStock" value="${entry.product.stock.stockLevelStatus.code}"/>

                    <c:forEach items="${entry.product.baseOptions}" var="option">
                        <c:if test="${not empty option.selected and option.selected.url eq entry.product.url}">
                            <c:forEach items="${option.selected.variantOptionQualifiers}" var="selectedOption">
                                <div>
                                    <strong>${selectedOption.name}:</strong>
                                    <span>${selectedOption.value}</span>
                                </div>
                                <c:set var="entryStock" value="${option.selected.stock.stockLevelStatus.code}"/>
                                <div class="clear"></div>
                            </c:forEach>
                        </c:if>
                    </c:forEach>

                <c:forEach items="${cartData.orderPrices}" var="orderPrice" varStatus="rowCounter">
                    <c:if test="${telcoycommerce:doesPotentialPromotionExistForOrderEntryAndBillingTime(cartData, entry.entryNumber, orderPrice.billingTime)}">
                        <ul class="cart-promotions">
                            <li class="">${orderPrice.billingTime.name}:</li>
                            <c:forEach items="${orderPrice.potentialProductPromotions}" var="promotion">
                                <c:set var="displayed" value="false" />
                                <c:forEach items="${promotion.consumedEntries}" var="consumedEntry">
                                    <c:if test="${not displayed && consumedEntry.orderEntryNumber == entry.entryNumber && not empty promotion.description}">
                                        <c:set var="displayed" value="true" />
                                        <li class="cart-promotions-potential"><ycommerce:testId code="cart_potentialPromotion_label">
                                                <span>${promotion.description}</span>
                                            </ycommerce:testId></li>
                                    </c:if>
                                </c:forEach>
                            </c:forEach>
                        </ul>
                    </c:if>
                </c:forEach>

                    <c:forEach items="${cartData.orderPrices}" var="orderPrice" varStatus="rowCounter">
                        <c:if test="${telcoycommerce:doesAppliedPromotionExistForOrderEntryAndBillingTime(cartData, entry.entryNumber, orderPrice.billingTime)}">
                            <ul class="cart-promotions">
                                <li>${orderPrice.billingTime.name}:</li>
                                <c:forEach items="${orderPrice.appliedProductPromotions}" var="promotion">
                                    <c:set var="displayed" value="false"/>
                                    <c:forEach items="${promotion.consumedEntries}" var="consumedEntry">
                                        <c:if test="${not displayed && consumedEntry.orderEntryNumber == entry.entryNumber}">
                                            <c:set var="displayed" value="true"/>
                                            <li class="cart-promotions-applied">
                                                <ycommerce:testId code="cart_appliedPromotion_label">
                                                    <span>${promotion.description}</span>
                                                </ycommerce:testId>
                                            </li>
                                        </c:if>
                                    </c:forEach>
                                </c:forEach>
                            </ul>
                        </c:if>
                    </c:forEach>


                    <c:if test="${not entry.valid}">
                        <div class="invalid-cartitem">
                            <spring:theme code="basket.page.bundle.message.missing.option" text="Sorry, there seems to be a missing option."/>
                            <c:forEach var="invalidMap" items="#">
                                <c:if test="${entry.editable}">
                          <a href="<c:url value="/bundle/edit-component/${entry.bundleNo}/component/${entry.component.id}"/>" class="cart-bundle-editable invalid-cartitem-editable"><spring:theme code="basket.page.message.addComponent" text="Please add {0}" arguments="${entry.component.name}"/></a>
                       </c:if>
                            </c:forEach>
                        </div>
                    </c:if>
                </td>
                <c:set value="${entry.product.code}${entry.entryNumber}" var="uniqueFormId"/>

                <td class="cart-bundle-quantity quantity">
                    <c:url value="/cart/update" var="cartUpdateFormAction" />
                    <c:if test="${entry.product !=null }">
                        <form:form id="updateCartForm${entry.entryNumber}" action="${cartUpdateFormAction}" method="post" commandName="updateQuantityForm${entry.entryNumber}"
                           data-cart='{"cartCode" : "${cartData.code}","productPostPrice":"${entry.basePrice.value}","productName":"${entry.product.name}"}'>
                            <input type="hidden" name="entryNumber" value="${entry.entryNumber}"/>
                            <input type="hidden" name="productCode" value="${entry.product.code}"/>
                            <input type="hidden" name="initialQuantity" value="${entry.quantity}"/>

                            <c:set var="isSubscriptionProduct" value="false"/>
                            <c:if test="${not empty entry.product.subscriptionTerm}">
                                <c:set var="isSubscriptionProduct" value="true"/>
                            </c:if>
                            <ycommerce:testId code="cart_product_quantity">
								<c:choose>
									<c:when test="${not entry.updateable or isSubscriptionProduct}">
										<input type="hidden" name="quantity" class="qty" value="${entry.quantity}"/>
								  		<span class="quantity-not-updateable">${entry.quantity}</span>
									</c:when>
									<c:otherwise>
										<form:label cssClass="skip" path="quantity" for="quantity${entry.entryNumber}"><spring:theme code="basket.page.quantity"/></form:label>
										<form:input disabled="${not entry.updateable}" type="text" size="1" id="quantity${entry.entryNumber}" class="qty" path="quantity" />
								  	</c:otherwise>
								</c:choose>						
                            </ycommerce:testId>
                            <c:if test="${entry.updateable and !isSubscriptionProduct}" >
                                <ycommerce:testId code="cart_product_updateQuantity">
                                    <a href="javascript:ACC.cart.submitUpdate('${entry.entryNumber}');" id="QuantityProduct_${entry.entryNumber}"><spring:theme code="basket.page.update"/></a>
                                </ycommerce:testId>
                            </c:if>
                        </form:form>
                    </c:if>

                </td>


                <c:if test="${ycommerce:checkIfPickupEnabledForStore()}">
                    <td class="cart-bundle-shipping">
                        <c:url value="/store-pickup/cart/update/delivery" var="cartEntryShippingModeAction" />
                        <form:form id="cartEntryShippingModeForm_${entry.product.code}${entry.entryNumber}" class="cartForm cartEntryShippingModeForm clear_fix"  action="${cartEntryShippingModeAction}" method="post">
                            <input type="hidden" name="entryNumber" value="${entry.entryNumber}"/>
                            <input type="hidden" name="hiddenPickupQty" value="${entry.quantity}"/>
                            <c:if test="${entry.product.purchasable}">
                                <c:if test="${not empty entryStock and entryStock ne 'outOfStock'}">
                                    <label for="shipMode${entry.entryNumber}" class="nostyle">
                                        <input type="radio" name="shipMode" value="ship" id="shipMode${entry.entryNumber}" class="updateToShippingSelection"
                                            <c:if test="${entry.deliveryPointOfService eq null or not entry.product.availableForPickup}">
                                                   checked="checked"
                                            </c:if>
                                        />
                                        <spring:theme code="basket.page.shipping.ship"/>
                                    </label>
                                    <br>
                                </c:if>
                                <c:if test="${entry.product.availableForPickup}">
                                    <label for="shipModePickUp${entry.entryNumber}" class="nostyle">
                                        <input type="radio" name="shipMode" value="pickUp" id="shipModePickUp${entry.entryNumber}" class="basket-page-shipping-ship pickupstoreSelection"
                                            <c:if test="${not empty entry.deliveryPointOfService}">
                                                checked="checked"
                                            </c:if>
                                        />
                                        <spring:theme code="basket.page.shipping.pickup"/>
                                    </label>
                                    <div class="basket-page-shipping-pickup pointOfServiceName">
                                        ${entry.deliveryPointOfService.name}
                                    </div>
                                    <c:set var="canBePickedUp" value="${entry.product.availableForPickup and not empty entry.deliveryPointOfService.name}" />
                                    <c:set var="hideChangeStoreLink" value="${not canBePickedUp ? 'style=display:none' : ''}" />
                                    <div ${hideChangeStoreLink} class="changeStoreLink">
                                        <storepickup:clickPickupInStore product="${entry.product}" cartPage="true"  entryNumber="${entry.entryNumber}"
                                            deliveryPointOfService="${entry.deliveryPointOfService.name}" quantity="${entry.quantity}"/>
                                    </div>
                                </c:if>
                            </c:if>
                        </form:form>
                    </td>
                </c:if>

                <c:choose>
                    <c:when test="${not empty entry.product}">
                        <c:forEach items="${entry.orderEntryPrices}" var="orderEntryPrice"> <!-- please ensure that these TDs get rendered always to avoid a uneven number of TDs per Row -->
                            <td class="cart-bundle-itemPrice">
                            <c:choose>
                                <c:when test="${not empty orderEntryPrice.totalPrice}">
                                    <c:if test="${(orderEntryPrice.basePrice.value - orderEntryPrice.totalPrice.value) > 0}">
                                        <del><format:price priceData="${orderEntryPrice.basePrice}" displayFreeForZero="true"/></del>
                                    </c:if>
                                    <format:price priceData="${orderEntryPrice.totalPrice}" displayFreeForZero="false"/>
                                </c:when>
                                <c:otherwise>
                                 &mdash;
                                </c:otherwise>
                            </c:choose>
                            </td>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                       <c:forEach items="${cartData.orderPrices}" varStatus="rowCounter">
                         <td class="cart-bundle-itemPrice">
                            &mdash;
                         </td>
                       </c:forEach>
                    </c:otherwise>
                </c:choose>
                <td class="cart-bundle-remove">
                    <c:if test="${entry.removeable}" >
                        <ycommerce:testId code="cart_product_removeProduct">
                            <spring:theme code="text.iconCartRemove" var="iconCartRemove"/>
                            <a href="javascript:ACC.cart.submitRemove('${entry.entryNumber}');" id="RemoveProduct_${entry.entryNumber}" class="iconRemove">${iconCartRemove}</a>
                        </ycommerce:testId>
                    </c:if>
                </td>
            </tr>
    </c:forEach>
		</tbody>
    </table>

</div>
<storepickup:pickupStorePopup />
