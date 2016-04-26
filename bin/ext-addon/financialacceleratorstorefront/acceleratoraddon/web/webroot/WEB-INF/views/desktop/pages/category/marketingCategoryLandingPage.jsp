<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template" %>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/desktop/nav" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common" %>

<template:page pageTitle="${pageTitle}">
    <div id="globalMessages">
    <common:globalMessages/>
    </div>

    <cms:pageSlot position="Section1" var="feature">
        <cms:component component="${feature}" element="div" class="span-24 section1 cms_disp-img_slot"/>
    </cms:pageSlot>

    <cms:pageSlot position="Section2" var="feature">
        <cms:component component="${feature}" element="div" class="span-24 section2 cms_disp-img_slot"/>
    </cms:pageSlot>

    <cms:pageSlot position="Section3" var="feature">
        <cms:component component="${feature}" element="div" class="span-24 section3 cms_disp-img_slot"/>
    </cms:pageSlot>

    <div id="productTabs">
        <cms:pageSlot position="Tabs" var="tabs">
            <cms:component component="${tabs}"/>
        </cms:pageSlot>
    </div>
</template:page>