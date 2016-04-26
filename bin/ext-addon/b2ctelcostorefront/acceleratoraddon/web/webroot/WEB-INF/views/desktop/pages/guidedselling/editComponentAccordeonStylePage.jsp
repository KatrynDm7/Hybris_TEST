<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common" %>
<%@ taglib prefix="extra" tagdir="/WEB-INF/tags/addons/b2ctelcostorefront/desktop/guidedselling" %>
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
		
		<div class="span-20">
		
			<div class="span-20" id="productDetailUpdateable">
				<extra:editComponentAccordeonStyle bundleTemplateData="${bundleTemplateData}" bundleNo="${bundleNo}"/>
			</div>
			<div class="span-20">
				<div class="span-4">
					<cms:pageSlot var="comp" position="${slots.CrossSelling}">
						<cms:component component="${comp}"/>
					</cms:pageSlot>
				</div>
			</div>
		</div>
		<div class="span-4 last">
			<cms:pageSlot var="comp" position="${slots.Accessories}">
				<cms:component component="${comp}"/>
			</cms:pageSlot>
		</div>
	</jsp:body>
 
 
 
</template:page>