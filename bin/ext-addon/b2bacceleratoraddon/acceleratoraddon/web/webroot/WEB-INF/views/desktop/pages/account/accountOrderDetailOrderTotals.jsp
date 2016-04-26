<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="order" tagdir="/WEB-INF/tags/desktop/order" %>
<%@ taglib prefix="b2bOrder" tagdir="/WEB-INF/tags/addons/b2bacceleratoraddon/desktop/order" %>

	
<div class="column accountContentPane clearfix orderList">
	<div class="span-20">
		<div class="span-9 left">
			<order:receivedPromotions order="${orderData}"/>
		</div>
		<c:if test="${orderData.triggerData ne null}">
			<b2bOrder:replenishmentScheduleInformation order="${orderData}"/>
		</c:if>
		<div class="span-8 last right">
			<order:orderTotalsItem order="${orderData}"/>
		</div>
	</div>
</div>
