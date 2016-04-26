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
    <cms:pageSlot position="Section1" var="feature">
        <cms:component component="${feature}" element="div" class="span-24 section1 cms_disp-img_slot"/>
    </cms:pageSlot>

    <cms:pageSlot position="Section2A" var="feature">
        <cms:component component="${feature}" element="div" class="span-24 section2A cms_disp-img_slot"/>
    </cms:pageSlot>

    <div class="span-12">
        <cms:pageSlot position="Section2B" var="feature">
            <cms:component component="${feature}" element="div" class="narrow section2B cms_disp-img_slot"/>
        </cms:pageSlot>
    </div>

    <div class="span-12 last">
        <cms:pageSlot position="Section2C" var="feature">
            <cms:component component="${feature}" element="div" class="narrow section2C cms_disp-img_slot"/>
        </cms:pageSlot>
    </div>

    <cms:pageSlot position="Section3" var="feature">
        <cms:component component="${feature}" element="div" class="span-24 section4 cms_disp-img_slot"/>
    </cms:pageSlot>

    <cms:pageSlot position="Section4" var="feature">
        <cms:component component="${feature}" element="div" class="span-24 section5 cms_disp-img_slot"/>
    </cms:pageSlot>

    <cms:pageSlot position="Section5" var="feature">
        <cms:component component="${feature}" element="div" class="span-24 section6 cms_disp-img_slot"/>
    </cms:pageSlot>

</template:page>