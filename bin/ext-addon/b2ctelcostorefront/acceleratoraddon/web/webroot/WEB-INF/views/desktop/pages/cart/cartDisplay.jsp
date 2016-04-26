<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/addons/b2ctelcostorefront/desktop/cart" %>

<c:if test="${not empty cartData.entries}">
    <cart:cartItems cartData="${cartData}"/>
</c:if>
