<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/addons/financialacceleratorstorefront/desktop/product" %>
<%@ taglib prefix="financialCart" tagdir="/WEB-INF/tags/addons/financialacceleratorstorefront/desktop/cart" %>

<div class="span-24">
    <product:productListerGrid columns="4" searchPageData="${searchPageData}" hideOptionProducts="true" addToCartBtn_label_key="basket.add.to.basket.select" />
</div>

<financialCart:changePlanConfirmPopup confirmActionButtonId="addNewPlanConfirmButton" cartData="${cartData}"/>
