<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/addons/b2bacceleratoraddon/desktop/product" %>

<product:productAddToCartPanel product="${product}" allowAddToCart="${empty showAddToCart ? true : showAddToCart}" isMain="true" />