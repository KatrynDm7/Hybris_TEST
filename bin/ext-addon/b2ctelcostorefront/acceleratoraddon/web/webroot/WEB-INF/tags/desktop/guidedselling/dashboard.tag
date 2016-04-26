<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="dashboard" required="true" type="de.hybris.platform.b2ctelcofacades.data.DashboardData" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product" %>

<div class="dashboard">
	<c:set var="isBundleValid" value="true"/>
	<c:forEach items="${dashboard.dashboardBoxes}" var="dashboardBox" varStatus="dashboardBoxCounter">		
		<div class="dashboard-item <c:if test="${dashboardBox.active}">dashboard-item-active</c:if>	">
			<div class="dashboard-headline">
				<c:if test="${dashboardBox.active}">
					<span class="hidden">Active Tab: </span>
					<c:if test="${empty dashboardBox.dashboardBoxEntries}">
						<c:set var="isBundleValid" value="false"/>
					</c:if>
				</c:if>		
				<spring:theme code="guidedselling.dashboard.step.${dashboardBox.type}"/> 			
			</div>
			<c:if test="${!dashboardBox.active and not empty dashboardBox.mainComponent and isBundleValid}">	
				<div class="dashboard-edit">
					<a href="<c:url value="/bundle/edit-component/${dashboard.bundleNo}/component/${dashboardBox.mainComponent.id}"/>"><spring:theme code="guidedselling.dashboard.button.edit" text="Edit"/></a>
				</div>
			</c:if>					
			<c:if test="${empty dashboardBox.dashboardBoxEntries and empty dashboardBox.mainComponent}">
                <div class="dashboard-default"><spring:theme code="guidedselling.dashboard.default.${dashboardBox.type}"/></div>
            </c:if>
			<c:if test="${empty dashboardBox.dashboardBoxEntries and not empty dashboardBox.mainComponent}">
                <div class="dashboard-default"><spring:theme code="guidedselling.dashboard.default.${dashboardBox.type}"/></div>
            </c:if>
			<div class="dashboard-item-row-container">
				<c:forEach items="${dashboardBox.dashboardBoxEntries}" var="dashboardBoxEntry" varStatus="dashboardBoxEntryCounter">
					<div class="dashboard-item-row">
						<c:if test="${not empty ycommerce:productImage(dashboardBoxEntry.orderEntry.product, 'thumbnail')}">	
							<div class="dashboard-item-thumb">
								<product:productPrimaryImage product="${dashboardBoxEntry.orderEntry.product}" format="thumbnail"/>
							</div>
						</c:if>
						<div class="dashboard-item-name">
							<span class="hidden">The following is selected:&nbsp;</span>${dashboardBoxEntry.orderEntry.product.name}&nbsp;
						</div>
						<div class="dashboard-item-price">
						
							<c:choose>
								<c:when test="${not empty dashboardBoxEntry.orderEntry.product.subscriptionTerm}">						        
							        <c:forEach items="${dashboardBoxEntry.orderEntry.orderEntryPrices}" var="oderEntryPrice">
							        	<c:if test="${oderEntryPrice.defaultPrice}">
								        	<c:if test="${(oderEntryPrice.basePrice.value - oderEntryPrice.totalPrice.value) > 0}">
									   			<del>
									   				<format:price priceData="${oderEntryPrice.basePrice}" displayFreeForZero="false"/>
									   			</del>
											</c:if>
											<format:price priceData="${oderEntryPrice.totalPrice}" displayFreeForZero="true"/>															
											<span class="dashboard-item-frequency">
												${dashboardBoxEntry.orderEntry.product.subscriptionTerm.billingPlan.billingTime.name}
											</span>
							        	</c:if>
							        </c:forEach>
								</c:when>
								<c:otherwise>
									<c:if test="${(dashboardBoxEntry.orderEntry.basePrice.value - dashboardBoxEntry.orderEntry.totalPrice.value) > 0}">
							   			<del>
							   				<format:price priceData="${dashboardBoxEntry.orderEntry.basePrice}" displayFreeForZero="false"/>
							   			</del>
									</c:if>
									<format:price priceData="${dashboardBoxEntry.orderEntry.totalPrice}" displayFreeForZero="true"/>
									<span class="dashboard-item-frequency">
										<spring:theme code="product.payNow" text="pay now"/>
									</span>
								</c:otherwise>
							</c:choose>
						</div>
					</div>
				</c:forEach>
			</div>
		</div>					
	</c:forEach>	
</div>