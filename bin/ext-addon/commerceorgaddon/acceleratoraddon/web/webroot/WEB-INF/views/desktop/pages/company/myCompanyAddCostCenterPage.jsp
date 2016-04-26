<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common" %>
<%@ taglib prefix="org-common" tagdir="/WEB-INF/tags/addons/commerceorgaddon/desktop/common" %>
<%@ taglib prefix="breadcrumb" tagdir="/WEB-INF/tags/desktop/nav/breadcrumb" %>
<%@ taglib prefix="company" tagdir="/WEB-INF/tags/addons/commerceorgaddon/desktop/company"%>

<c:if test="${empty saveUrl}">
	<c:url value="/my-company/organization-management/manage-costcenters/add" var="saveUrl"/>
</c:if>
<c:if test="${empty cancelUrl}">
	<c:url value="/my-company/organization-management/manage-costcenters" var="cancelUrl"/>
</c:if>
<template:page pageTitle="${pageTitle}">

	<div id="globalMessages">
		<common:globalMessages/>
	</div>
	<company:myCompanyNav selected="costCenters"/>
	<div class="column companyContentPane clearfix orderList">
		<div class="headline">${title}</div>
		<company:b2bCostCenterForm cancelUrl="${cancelUrl}" saveUrl="${saveUrl}" b2BCostCenterForm="${b2BCostCenterForm}"/>
		<org-common:back cancelUrl="${cancelUrl}"/>
	</div>
</template:page>
