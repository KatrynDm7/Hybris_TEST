<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template" %>
<%@ taglib prefix="financial" tagdir="/WEB-INF/tags/addons/financialacceleratorstorefront/desktop" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="financialtags" uri="http://hybris.com/tld/financialtags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/desktop/nav" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/addons/financialacceleratorstorefront/desktop/product" %>
<%@ taglib prefix="financialCart" tagdir="/WEB-INF/tags/addons/financialacceleratorstorefront/desktop/cart" %>

<template:page pageTitle="${pageTitle}">
	
    <cms:pageSlot position="Section1" var="feature">
        <cms:component component="${feature}" element="div" class="span-24 section1 cms_disp-img_slot"/>
    </cms:pageSlot>
	
    <div class="span-24">
        <%--TODO the columns parameter need implement by using configurable properties.--%>
         <cms:pageSlot position="ImageSlot" var="component" /> 
    	
        <product:productListerGrid columns="4" searchPageData="${searchPageData}" imageComponent="${component}" addToCartBtn_label_key="basket.add.to.basket.getaquote" />
    </div>
    <financialCart:changePlanConfirmPopup confirmActionButtonId="addNewPlanConfirmButton" cartData="${cartData}"/>
</template:page>
