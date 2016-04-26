<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/desktop/nav" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/addons/b2ctelcostorefront/desktop/product" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="guidedselling" tagdir="/WEB-INF/tags/addons/b2ctelcostorefront/desktop/guidedselling" %>

<template:page pageTitle="${pageTitle}">
 	<jsp:body>
		<c:if test="${not empty message}">
			<spring:theme code="${message}"/>
		</c:if>
		<div id="globalMessages">
			<common:globalMessages/>
		</div>

		<guidedselling:dashboard dashboard="${dashboard}"/>

	<h1 class="guided-selling-headline"><spring:theme code="guidedselling.select.text.${productType}"/></h1>
	
	<div class="span-24 product-list searchlistpage">
		<div class="span-6 facetNavigation">
			<nav:categoryNav pageData="${searchPageData}"/>
			<cms:pageSlot position="Section4" var="feature">
				<cms:component component="${feature}" element="div" class="section4 small_detail"/>
			</cms:pageSlot>
		</div>

		<div class="span-18 last searchlist-results">
				
			<div class="span-18 last spellingSuggestion">
			<nav:searchSpellingSuggestion spellingSuggestion="${searchPageData.spellingSuggestion}" />
		</div>		

			<nav:pagination supportShowPaged="${isShowPageAllowed}" numberPagesShown="${numberPagesShown}" top="true" supportShowAll="${isShowAllAllowed}" searchPageData="${searchPageData}" searchUrl="${searchPageData.currentQuery.url}"/>
			
			<input type="hidden" id="refreshed" value="no">

			<div class="productList">
				<c:forEach items="${searchPageData.results}" var="product">
					<!-- TELCO_START -->
					<product:productListerGridItemForGuidedSelling product="${product}" bundleNo="${bundleNo}" componentId="${componentId}"/>
					<!-- TELCO_END -->					
			</c:forEach>
		</div>
		
		<nav:pagination supportShowPaged="${isShowPageAllowed}" numberPagesShown="${numberPagesShown}" top="false" supportShowAll="${isShowAllAllowed}" searchPageData="${searchPageData}" searchUrl="${searchPageData.currentQuery.url}"/>
			
	</div>
</div>	
		
	</jsp:body> 
</template:page>