<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template" %>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/desktop/nav" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product" %>
<%@ taglib prefix="sbgproduct" tagdir="/WEB-INF/tags/addons/b2ctelcostorefront/desktop/product" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="cms" uri="/WEB-INF/common/tld/cmstags.tld" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ycommerce" uri="/WEB-INF/common/tld/ycommercetags.tld" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common" %>
<%@ taglib prefix="breadcrumb" tagdir="/WEB-INF/tags/desktop/nav/breadcrumb" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>

<template:page pageTitle="${pageTitle}">
	<jsp:attribute name="pageScripts">
		<script  type="text/javascript">
			$(function() {
				$(".sbgProduct:first-child div[data-row]").each(function(i){
					 var maxheight=0 
					$(".sbgProduct form > div[data-row="+(i+1)+"]").each(function(){
					    var height=$(this).height();
					    maxheight=(maxheight<height)?height:maxheight; 
					})
					$(".sbgProduct form > div[data-row="+(i+1)+"]").css("height",maxheight);
				});
			});
		</script>
	</jsp:attribute>
	<jsp:body>
		<div id="globalMessages">
			<common:globalMessages/>
		</div>
	
		<cms:pageSlot var="feature" position="${slots['Section1']}">
			<div class="span-24 section1 advert">
				<cms:component component="${feature}"/>
			</div>
		</cms:pageSlot>
		<div class="span-24 sbgProductList">

        <table class="sub-tabbody-table">
                <tbody>
                    <tr>
                        <th></th>
                        <c:forEach items="${searchPageData.results}" var="product" varStatus="status">
                            <c:url value="${product.url}" var="productUrl"/>
                            <th>
                                <sbgproduct:sbgProductListerGridItem product="${product}"/>
                            </th>
                        </c:forEach>
                    </tr>
                    <tr>
                        <td><spring:theme code="product.list.viewplans.billingFrequency" text="Billing Frequency"/></td>
                        <c:forEach items="${searchPageData.results}" var="product" varStatus="status">
                            <td>
                                ${product.subscriptionTerm.billingPlan.billingTime.name}
                            </td>
                        </c:forEach>
                    </tr>
                    <tr>
                        <td><spring:theme code="product.list.viewplans.price" text="Price"/></td>
                        <c:forEach items="${searchPageData.results}" var="product" varStatus="status">
                            <td>
                                <c:if test="${not empty product.price.recurringChargeEntries}">
                                    <c:set var="recurringChargeCount" value="${fn:length(product.price.recurringChargeEntries)}"/>
                                    <c:forEach items="${product.price.recurringChargeEntries}" var="recurringPrice" varStatus="recurringPricesCounter">
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
                                    <div class="pay">${product.subscriptionTerm.billingPlan.billingTime.name}</div>
                                </c:if>
                                <c:if test="${not empty product.price.oneTimeChargeEntries}">
                                    <c:if test="${not empty product.price.recurringChargeEntries}">
                                        <br>
                                    </c:if>
                                    <c:forEach items="${product.price.oneTimeChargeEntries}" var="oneTimePrice" varStatus="oneTimePricesCounter">
                                        <c:if test="${not oneTimePricesCounter.first}">
                                            <br>
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
                        <c:forEach items="${searchPageData.results}" var="product" varStatus="status">
                            <td>
                                <c:if test="${product.subscriptionTerm.termOfServiceNumber gt 0}">${product.subscriptionTerm.termOfServiceNumber} &nbsp;</c:if>${product.subscriptionTerm.termOfServiceFrequency.name}
                            </td>
                        </c:forEach>
                    </tr>
                    <tr>
                        <td><spring:theme code="product.list.viewplans.termOfServiceRenewal" text="Term of service renewal"/></td>
                        <c:forEach items="${searchPageData.results}" var="product" varStatus="status">
                            <td>
                                ${product.subscriptionTerm.termOfServiceRenewal.name}
                            </td>
                        </c:forEach>
                    </tr>
                    <tr>
                        <td><spring:theme code="product.list.viewplans.entitlements" text="Entitlements"/></td>
                        <c:forEach items="${searchPageData.results}" var="product" varStatus="status">
                            <td>
                                <c:if test="${not empty product.entitlements}">
                                    <c:forEach items="${product.entitlements}" var="entitlement">
                                        <div>
                                            ${entitlement.description}
                                        </div>
                                    </c:forEach>
                                </c:if>
                            </td>
                        </c:forEach>
                    </tr>
                    <tr>
                        <td><spring:theme code="product.list.viewplans.usage.charges" text="Usage Charges"/></td>
                        <c:forEach items="${searchPageData.results}" var="product" varStatus="status">
                            <td>
                                <c:if test="${not empty product.price and not empty product.price.usageCharges}">
                                    <c:forEach items="${product.price.usageCharges}" var="usageCharge">
                                        <div>
                                            <b>${usageCharge.name}</b><br>
                                            <c:if test="${not empty usageCharge.usageChargeEntries}">
                                                <c:set var="isFirstChargeEntry" value="true"/>
                                                <c:forEach items="${usageCharge.usageChargeEntries}" var="usageChargeEntry">
                                                    <c:if test="${usageChargeEntry['class'].simpleName eq 'TierUsageChargeEntryData'}">
                                                        <c:if test="${usageCharge['class'].simpleName eq 'PerUnitUsageChargeData'}">
                                                            <c:if test="${isFirstChargeEntry}">
                                                                <spring:theme code="product.list.viewplans.tierUsageChargeEntry" arguments="${usageChargeEntry.tierStart}^${usageChargeEntry.tierEnd}^^${usageChargeEntry.price.formattedValue}^${usageCharge.usageUnit.name}" argumentSeparator="^"/><br>
                                                            </c:if>
                                                            <c:if test="${not isFirstChargeEntry}">
                                                                <c:if test="${usageCharge.usageChargeType.code eq 'each_respective_tier'}">
                                                                    <spring:theme code="product.list.viewplans.tierUsageChargeEntry" arguments="${usageChargeEntry.tierStart}^${usageChargeEntry.tierEnd}^^${usageChargeEntry.price.formattedValue}^${usageCharge.usageUnit.name}" argumentSeparator="^"/><br>
                                                                </c:if>
                                                                <c:if test="${usageCharge.usageChargeType.code eq 'highest_applicable_tier'}">
                                                                    <spring:theme code="product.list.viewplans.tierUsageChargeEntryHighestTier" arguments="${usageChargeEntry.tierStart}^${usageChargeEntry.tierEnd}^^${usageChargeEntry.price.formattedValue}^${usageCharge.usageUnit.name}" argumentSeparator="^"/><br>
                                                                </c:if>
                                                            </c:if>
                                                        </c:if>
                                                        <c:if test="${usageCharge['class'].simpleName eq 'VolumeUsageChargeData'}">
                                                            <c:if test="${isFirstChargeEntry}">
                                                                <spring:theme code="product.list.viewplans.volumeTierUsageChargeEntry" arguments="${usageChargeEntry.tierStart}^${usageChargeEntry.tierEnd}^^${usageChargeEntry.price.formattedValue}" argumentSeparator="^"/><br>
                                                            </c:if>
                                                            <c:if test="${not isFirstChargeEntry}">
                                                                <spring:theme code="product.list.viewplans.volumeTierUsageChargeEntryNotFirstTier" arguments="${usageChargeEntry.tierStart}^${usageChargeEntry.tierEnd}^^${usageChargeEntry.price.formattedValue}" argumentSeparator="^"/><br>
                                                            </c:if>
                                                        </c:if>
                                                    </c:if>

                                                    <c:if test="${usageChargeEntry['class'].simpleName eq 'OverageUsageChargeEntryData' and fn:length(usageCharge.usageChargeEntries) gt 1}">
                                                        <c:if test="${usageCharge['class'].simpleName eq 'PerUnitUsageChargeData' and usageCharge.usageChargeType.code eq 'each_respective_tier'}">
                                                            <spring:theme code="product.list.viewplans.thereafterOverageUsageChargeEntry" arguments="${usageChargeEntry.price.formattedValue},${usageCharge.usageUnit.name}"/>
                                                        </c:if>
                                                        <c:if test="${usageCharge['class'].simpleName eq 'PerUnitUsageChargeData' and usageCharge.usageChargeType.code eq 'highest_applicable_tier'}">
                                                            <spring:theme code="product.list.viewplans.thereafterOverageUsageChargeEntryHighestTier" arguments="${usageChargeEntry.price.formattedValue},${usageCharge.usageUnit.name}"/>
                                                        </c:if>
                                                        <c:if test="${usageCharge['class'].simpleName eq 'VolumeUsageChargeData'}">
                                                            <spring:theme code="product.list.viewplans.thereafterOverageUsageChargeEntryVolume" arguments="${usageChargeEntry.price.formattedValue},${usageCharge.usageUnit.name}"/>
                                                        </c:if>
                                                    </c:if>
                                                    <c:if test="${usageChargeEntry['class'].simpleName eq 'OverageUsageChargeEntryData' and fn:length(usageCharge.usageChargeEntries) eq 1}">
                                                        <spring:theme code="product.list.viewplans.overageUsageChargeEntry" arguments="${usageChargeEntry.price.formattedValue},${usageCharge.usageUnit.name}"/>
                                                    </c:if>
                                                    <c:set var="isFirstChargeEntry" value="false"/>
                                                </c:forEach>
                                            </c:if>
                                        </div>
                                        <br>
                                    </c:forEach>
                                </c:if>
                            </td>
                        </c:forEach>
                    </tr>
                    <tr>
                        <td></td>
                        <c:forEach items="${searchPageData.results}" var="product" varStatus="status">
                            <td>
                                <c:url value="/sbgaddtocart" var="sbgAddToCart"/>
                                <form:form id="addToCartForm" class="add_to_cart_form" action="${sbgAddToCart}" method="post">
                                    <input type="hidden" name="productCodePost" value="${product.code}"/>
                                    <button type="submit" class="positive large addtocartbutton">
                                        <spring:theme code="basket.add.to.basket" />
                                    </button>
                                </form:form>
                            </td>
                        </c:forEach>
                    </tr>
                </tbody>
            </table>
		</div>
	 </jsp:body>
</template:page>