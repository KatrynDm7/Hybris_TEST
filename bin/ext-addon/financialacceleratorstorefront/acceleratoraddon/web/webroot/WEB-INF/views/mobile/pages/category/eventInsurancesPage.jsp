<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="financial" tagdir="/WEB-INF/tags/addons/financialacceleratorstorefront/mobile" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<h3>Mobile Event Insurance Category Page</h3>

Single Product: <financial:addSingleProductToCart productCode="WED_3STAR" bundleTemplateId="STAR_PLAN" />

Multiple Products: <financial:addToCart productCodes="${fn:split('WED_2STAR,WED_EXCESSWAIVER', ',')}" bundleTemplateIds="${fn:split('STAR_PLAN,STAR_PRODUCT', ',')}"/>