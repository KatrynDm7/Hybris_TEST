<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="order" tagdir="/WEB-INF/tags/desktop/order" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="telcoorder" tagdir="/WEB-INF/tags/addons/b2ctelcostorefront/desktop/order" %>

<c:if test="${not empty orderData.unconsignedEntries}">
	<order:orderUnconsignedEntries order="${orderData}"/>
</c:if>
<c:set var="headingWasShown" value="false"/>
<c:forEach items="${orderData.consignments}" var="consignment">
	<c:if test="${consignment.status.code ne 'WAITING' and consignment.status.code ne 'PICKPACK' and consignment.status.code ne 'READY'}">
		<div class="productItemListHolder fulfilment-states-${consignment.status.code}">
			<telcoorder:accountOrderDetailsItem order="${orderData}" consignment="${consignment}"/>
		</div>
	</c:if>
</c:forEach>