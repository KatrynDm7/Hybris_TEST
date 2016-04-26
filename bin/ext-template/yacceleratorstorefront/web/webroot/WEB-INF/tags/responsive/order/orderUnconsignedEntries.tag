<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="order" required="true" type="de.hybris.platform.commercefacades.order.data.OrderData" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="order" tagdir="/WEB-INF/tags/responsive/order" %>

<div>
<c:forEach items="${order.unconsignedEntries}" var="entry">

	<div class="account-orderdetail-item-section-header">

        <div class="orderDetail_itemHeader orderPending">
            <div class="col-xs-6 text-uppercase">
                <spring:theme code="text.account.order.unconsignedEntry.status.pending" />
            </div>
        </div>
		
		<c:choose>
			<c:when test="${entry.deliveryPointOfService ne null}">
				<order:storeAddressItem deliveryPointOfService="${entry.deliveryPointOfService}" />
			</c:when>
			<c:otherwise>
                <div class="col-md-5 order-ship-to">
                    <div class="label-order"><spring:theme code="text.account.order.shipto" text="Ship To" /></div>
					<order:addressItem address="${orderData.deliveryAddress}"/>
				</div>
			</c:otherwise>
		</c:choose>

	</div>
	
	<div class="account-orderdetail-item-section-body">
		<order:orderEntryDetails orderEntry="${entry}" order="${order}"/>
	</div>
</c:forEach>

</div>
