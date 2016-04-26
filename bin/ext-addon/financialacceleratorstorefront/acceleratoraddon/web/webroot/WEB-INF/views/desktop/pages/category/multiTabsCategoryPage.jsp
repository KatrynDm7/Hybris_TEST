<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template" %>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/desktop/nav" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>

<template:page pageTitle="${pageTitle}">
   	
    <cms:pageSlot position="Section1" var="feature">
        <cms:component component="${feature}" element="div" class="span-24 section1 cms_disp-img_slot"/>
    </cms:pageSlot>

	<c:if test="${param.viewStatus eq'view'}">
    	<cms:pageSlot position="Section2" var="feature">
        	<cms:component component="${feature}" element="div" class="span-24 section2 cms_disp-img_slot"/>
    	</cms:pageSlot>
    </c:if>
</template:page>