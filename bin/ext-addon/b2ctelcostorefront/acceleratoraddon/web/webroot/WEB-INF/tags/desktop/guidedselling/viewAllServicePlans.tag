<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="bundleTabs" required="true" type="java.util.List" %>
<%@ attribute name="selectProduct" required="true" type="java.lang.Boolean" %>
<%@ attribute name="showButtons" required="true" type="java.lang.Boolean" %>
<%@ attribute name="horizontalLayout" required="true" type="java.lang.Boolean" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="guidedselling" tagdir="/WEB-INF/tags/addons/b2ctelcostorefront/desktop/guidedselling" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:if test="${showButtons}">
	<guidedselling:dashboardEmpty type="ServicePlanModel"/>
</c:if>
		
<div class="prod_add_to_cart">
	<c:url value="/bundle/addEntry" var="addToCartBundle" />

	<div class="tabs">
	
		<c:forEach items="${bundleTabs}" var="bundleTab" varStatus="bundleTabCounter">

			<c:if test="${bundleTab.preselected}"></c:if>
			
			<h2 <c:if test="${bundleTab.preselected}"> id="preselected"</c:if>>${bundleTab.parentBundleTemplate.name}</h2>
			<div class="tabbody">
			<div class="headline-contract-length">
				<spring:theme code="product.subscription.termofservicefrequency" />
			</div>				
			<div class="subTabs">
			<c:forEach items="${bundleTab.frequencyTabs}" var="frequencyTab" varStatus="frequencyTabCounter">
				<h3 <c:if test="${frequencyTab.preselected}"> id="preselected_sub"</c:if>> <c:if test="${frequencyTab.termOfServiceNumber gt 0}">${frequencyTab.termOfServiceNumber} &nbsp;</c:if> ${frequencyTab.termOfServiceFrequency.name}</h3>
				<c:if test="${horizontalLayout}">		
					<div class="sub-tabbody">
						<table class="sub-tabbody-table">
							<tr>
								<th>&nbsp;</th>
								<c:forEach items="${frequencyTab.products}" var="plan" varStatus="plansCounter">
									<th <c:if test="${selectProduct and plan.preselected}">class="plan-selected"</c:if>>${plan.name}</th>
								</c:forEach>
							</tr>
							<tr>
								<td><spring:theme code="product.list.viewplans.billingFrequency" text="Billing Frequency"/></td>
								<c:forEach items="${frequencyTab.products}" var="plan" varStatus="plansCounter">	
									<td <c:if test="${selectProduct and plan.preselected}">class="plan-selected"</c:if>>${plan.subscriptionTerm.billingPlan.billingTime.name}</td>
								</c:forEach>
							</tr>
							<tr>
								<td><spring:theme code="product.list.viewplans.price" text="Price"/></td>					
								<c:forEach items="${frequencyTab.products}" var="plan" varStatus="plansCounter">	
									<td <c:if test="${selectProduct and plan.preselected}">class="plan-selected"</c:if>>
										<c:if test="${not empty plan.price.recurringChargeEntries}">
											<c:set var="recurringChargeCount" value="${fn:length(plan.price.recurringChargeEntries)}"/>
										    <c:forEach items="${plan.price.recurringChargeEntries}" var="recurringPrice" varStatus="recurringPricesCounter">
										    	<c:choose>
										    		<c:when test="${recurringPrice.cycleEnd == '-1'}">
										    			<c:if test="${recurringChargeCount gt 1}">
										    				<spring:theme code="product.list.viewplans.price.interval.unlimited" arguments="${recurringPrice.cycleStart}"/>
										    			</c:if>
										    			<c:if test="${recurringChargeCount eq 1 and recurringPrice.cycleStart gt 1}">
										    				<spring:theme code="product.list.viewplans.price.interval.unlimited" arguments="${recurringPrice.cycleStart}"/>
										    			</c:if>
										    			<format:price priceData="${recurringPrice.price}"/>
										    		</c:when>
										    		<c:otherwise>
										    			<spring:theme code="product.list.viewplans.price.interval" arguments="${recurringPrice.cycleStart}, ${recurringPrice.cycleEnd}"/>
										        		<format:price priceData="${recurringPrice.price}"/>
										        	</c:otherwise>
										        </c:choose>
										        <br>
										    </c:forEach>
										    <div class="pay">${plan.subscriptionTerm.billingPlan.billingTime.name}</div>
									    </c:if>
									    <c:if test="${not empty plan.price.oneTimeChargeEntries}">
									    	<c:if test="${not empty plan.price.recurringChargeEntries}">
									    		<br>
									    	</c:if>
									    	<c:forEach items="${plan.price.oneTimeChargeEntries}" var="oneTimePrice" varStatus="oneTimePricesCounter">
									    		<c:if test="${not oneTimePricesCounter.first}">
									    			<br/>
									    		</c:if>
									    		<spring:theme code="product.list.viewplans.price.onetime" arguments="${oneTimePrice.name}"/>
									    		<format:price priceData="${oneTimePrice.price}"/>
									    		<div class="pay">${oneTimePrice.billingTime.name}</div>
									    	</c:forEach>
									    </c:if>
									</td>		 				
								</c:forEach>
							</tr>
							<tr>
								<td><spring:theme code="product.list.viewplans.termOfServiceFrequency" text="Term of service frequency"/></td>					
								<c:forEach items="${frequencyTab.products}" var="plan" varStatus="plansCounter">	
									<td <c:if test="${selectProduct and plan.preselected}">class="plan-selected"</c:if>><c:if test="${plan.subscriptionTerm.termOfServiceNumber gt 0}">${plan.subscriptionTerm.termOfServiceNumber} &nbsp;</c:if>${plan.subscriptionTerm.termOfServiceFrequency.name}</td>		 				
								</c:forEach>
							</tr>
							<tr>
								<td><spring:theme code="product.list.viewplans.termOfServiceRenewal" text="Term of service renewal"/></td>					
								<c:forEach items="${frequencyTab.products}" var="plan" varStatus="plansCounter">	
									<td <c:if test="${selectProduct and plan.preselected}">class="plan-selected"</c:if>>${plan.subscriptionTerm.termOfServiceRenewal.name}</td>		 				
								</c:forEach>
							</tr>
							<tr>
								<td>
									<spring:theme code="product.list.viewplans.entitlements" text="Entitlements"/>
								</td>
								<c:forEach items="${frequencyTab.products}" var="plan" varStatus="plansCounter">
									<td <c:if test="${selectProduct and plan.preselected}">class="plan-selected"</c:if>>
										<c:if test="${not empty plan.entitlements}">
											<c:forEach items="${plan.entitlements}" var="entitlement">
												<div class="featureClass">
													${entitlement.description}
													<br/>
												</div>
											</c:forEach>
										</c:if>
									</td>
								</c:forEach>
							</tr>
	
							<tr>	
								<td>
									<spring:theme code="product.list.viewplans.usage.charges" text="Usage Charges"/>								
								</td>					
								<c:forEach items="${frequencyTab.products}" var="plan" varStatus="plansCounter">	
									<td <c:if test="${selectProduct and plan.preselected}">class="plan-selected"</c:if>>			
										<c:if test="${not empty plan.price and not empty plan.price.usageCharges}">
											<c:forEach items="${plan.price.usageCharges}" var="usageCharge">
											    <div class="featureClass">
												    <b>${usageCharge.name}</b><br/>
	                                                <c:if test="${not empty usageCharge.usageChargeEntries}">
	                                                    <c:forEach items="${usageCharge.usageChargeEntries}" var="usageChargeEntry">
	                                                        <c:if test="${usageChargeEntry['class'].simpleName eq 'TierUsageChargeEntryData'}">
	                                                            <spring:theme code="product.list.viewplans.tierUsageChargeEntry" arguments="${usageChargeEntry.tierStart}^${usageChargeEntry.tierEnd}^^${usageChargeEntry.price.formattedValue}^${usageCharge.usageUnit.name}" argumentSeparator="^"/><br/>
	                                                        </c:if>
	                                                        <c:if test="${usageChargeEntry['class'].simpleName eq 'OverageUsageChargeEntryData'}">
	                                                         	<spring:theme code="product.list.viewplans.overageUsageChargeEntry" arguments="${usageChargeEntry.price.formattedValue},${usageCharge.usageUnit.name}"/>
	                                                        </c:if>
	                                                    </c:forEach>
	                                                </c:if>
	                                                <br/><br/>
											    </div>
											</c:forEach>
										</c:if>
									</td>
								</c:forEach>
							</tr>
																											
							<c:if test="${showButtons}">
								<tr>
									<td>&nbsp;</td>					
									<c:forEach items="${frequencyTab.products}" var="plan" varStatus="plansCounter">	
										<td class="<c:if test="${selectProduct and plan.preselected}">plan-selected</c:if>">
											<form:form  action="${addToCartBundle}" method="post">
										
													<div id="${bundleTab.parentBundleTemplate.id}_${plan.code}">
													    <c:if test="${plan.stock.stockLevelStatus == 'inStock'}">
														<button type="submit" class="positive small delayed-button" title="<spring:theme code="basket.add.to.basket" />">
															<spring:theme code="basket.add.to.basket" />
														</button>
														</c:if>
													    <c:if test="${plan.stock.stockLevelStatus ne 'inStock'}">
															<spring:theme code="text.addToCart.outOfStock" />
														</c:if>
													</div>
												
												<input type="hidden" name="productCodePost" value="${plan.code}"/>
												<input type="hidden" name="quantity" value="1"/>
												<input type="hidden" name="bundleNo" value="-1"/>
												<input type="hidden" name="bundleTemplateId" value="${bundleTab.sourceComponent.id}"/>	
												<input type="hidden" name="navigation" value="PREVIOUS"/>	
											</form:form>
										</td>							
									</c:forEach>
								</tr>
							</c:if>
						</table>
					</div>
				</c:if>

				<c:if test="${!horizontalLayout}">
					<div class="sub-tabbody">
						<table class="sub-tabbody-table">
							<tr>
								<th>&nbsp;</th>
								<th><spring:theme code="product.list.viewplans.price" text="Price"/></th>
								<th><spring:theme code="product.list.viewplans.termOfServiceFrequency" text="Term of service frequency"/></th>
								<th><spring:theme code="product.list.viewplans.termOfServiceRenewal" text="Term of service renewal"/></th>
						    </tr>
							<c:forEach items="${frequencyTab.products}" var="plan" varStatus="plansCounter">	
								<tr>
									<td <c:if test="${selectProduct and plan.preselected}">class="plan-selected"</c:if>>${plan.name}</td>
									<td <c:if test="${selectProduct and plan.preselected}">class="plan-selected"</c:if>>
									    <c:set var="lastRecurringCharge" value="${plan.price}"/>
										<c:forEach items="${plan.price.recurringChargeEntries}" var="curRecurringCharge" varStatus="rowCounterRecurringCharge">
											<c:set var="lastRecurringCharge" value="${curRecurringCharge}"/>
										</c:forEach>									    
										<format:price priceData="${lastRecurringCharge.price}"/> <div class="pay">${plan.subscriptionTerm.billingPlan.billingTime.name}</div>
									</td>		 				
									<td <c:if test="${selectProduct and plan.preselected}">class="plan-selected"</c:if>><c:if test="${plan.subscriptionTerm.termOfServiceNumber gt 0}">${plan.subscriptionTerm.termOfServiceNumber} &nbsp;</c:if>${plan.subscriptionTerm.termOfServiceFrequency.name}</td>		 				
									<td <c:if test="${selectProduct and plan.preselected}">class="plan-selected"</c:if>>${plan.subscriptionTerm.termOfServiceRenewal.name}</td>		 				
									<c:if test="${showButtons}">
										<form:form id="bundleAddToCartForm${bundleTabCounter.count}_${plansCounter.count}" action="${addToCartBundle}" method="post">
											<td class="sub-tabbody-table-td4 <c:if test="${selectProduct and plan.preselected}">plan-selected</c:if>">
												<div id="${bundleTab.parentBundleTemplate.id}_${plan.code}">
													<button type="submit" class="positive small delayed-button" title="<spring:theme code="basket.add.to.basket" />">
														<spring:theme code="basket.add.to.basket" />
													</button>
												</div>
											</td>
											<input type="hidden" name="productCodePost" value="${plan.code}"/>
											<input type="hidden" name="quantity" value="1"/>
											<input type="hidden" name="bundleNo" value="-1"/>
											<input type="hidden" name="bundleTemplateId" value="${bundleTab.sourceComponent.id}"/>	
											<input type="hidden" name="navigation" value="PREVIOUS"/>	
										</form:form>							
									</c:if>
								</tr>
							</c:forEach>
						</table>
					</div>
				</c:if>

					
				</c:forEach>
				</div>	
			</div>		
		</c:forEach>
	</div>
</div>
