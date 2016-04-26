<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/desktop/formElement"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common" %>
<%@ taglib prefix="org-common" tagdir="/WEB-INF/tags/addons/commerceorgaddon/desktop/common" %>
<%@ taglib prefix="breadcrumb" tagdir="/WEB-INF/tags/desktop/nav/breadcrumb" %>
<%@ taglib prefix="company" tagdir="/WEB-INF/tags/addons/commerceorgaddon/desktop/company" %>

<spring:url value="/my-company/organization-management/manage-usergroups/edit"
			var="formUrl">
	<spring:param name="usergroup" value="${b2BUserGroupForm.originalUid}"/>
</spring:url>
<spring:url value="/my-company/organization-management/manage-usergroups/details"
			var="cancelUrl">
	<spring:param name="usergroup" value="${b2BUserGroupForm.originalUid}"/>
</spring:url>
<template:page pageTitle="${pageTitle}">
	<div id="globalMessages">
		<common:globalMessages/>
	</div>
	<company:myCompanyNav selected="manageUsergroups"/>
	<div class="column companyContentPane clearfix">
		<cms:pageSlot position="TopContent" var="feature" element="div" class="span-20 wide-content-slot cms_disp-img_slot">
			<cms:component component="${feature}"/>
		</cms:pageSlot>	
		<div class="headline"><spring:theme code="text.company.manageUsergroups.unit.edit" text="Edit Usergroup"/></div>
		<company:b2bUserGroupForm formUrl="${formUrl}" b2BUserGroupForm="${b2BUserGroupForm}" cancelUrl="${cancelUrl}"/>
			<org-common:back cancelUrl="${cancelUrl}"/>
	</div>
</template:page>
