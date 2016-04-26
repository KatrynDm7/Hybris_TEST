<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="order" required="true" type="de.hybris.platform.commercefacades.order.data.OrderData" %>
<%@ attribute name="consignment" required="true" type="de.hybris.platform.commercefacades.order.data.ConsignmentData" %>
<%@ attribute name="inProgress" required="false" type="java.lang.Boolean" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="order" tagdir="/WEB-INF/tags/responsive/order" %>

<div class="account-orderdetail-item-section-header">
	<ycommerce:testId code="orderDetail_itemHeader_section">			
		<div class="orderDetail_itemHeader">
            <ycommerce:testId code="orderDetail_consignmentStatus_label">
                <span class="col-xs-6 text-uppercase">
                    <spring:theme code="text.account.order.consignment.status.${consignment.statusDisplay}" />
                </span>
            </ycommerce:testId>

			<ycommerce:testId code="orderDetail_consignmentStatusDate_label">
				<div class="text-right">
                    <fmt:formatDate value="${consignment.statusDate}" dateStyle="medium" timeStyle="short" type="both"/>
                </div>
			</ycommerce:testId>

		</div>

		<c:choose>
			<c:when test="${consignment.deliveryPointOfService ne null}">
				<ycommerce:testId code="orderDetail_storeDetails_section">
					<order:storeAddressItem deliveryPointOfService="${consignment.deliveryPointOfService}" inProgress="${inProgress}" statusDate="${consignment.statusDate}"/>
				</ycommerce:testId>
			</c:when>
			<c:otherwise>
                <div class="col-md-5 order-ship-to">
                    <ycommerce:testId code="orderDetail_deliveryAddress_section">
                        <div class="label-order"><spring:theme code="text.account.order.shipto" text="Ship To" /></div>
                        <order:addressItem address="${orderData.deliveryAddress}"/>
                    </ycommerce:testId>
                </div>

                <div class="col-md-3 order-shipping-method">
                    <ycommerce:testId code="orderDetail_deliveryMethod_section">
                        <order:deliveryMethodItem order="${orderData}"/>
                    </ycommerce:testId>
                </div>

                <c:if test="${not inProgress}">
                    <c:choose>
                        <c:when test="${consignment.status.code eq 'SHIPPED' and not empty consignment.trackingID}" >
                            <div class="col-md-3 order-tracking-no">
                                <ycommerce:testId code="orderDetail_trackingId_label">
                                    <span class="label-order"><spring:theme code="text.account.order.tracking" text="Tracking No." /></span>
                                    <br>
                                    <span class="order-track-number">${consignment.trackingID}</span>
                                </ycommerce:testId>
                            </div>
                        </c:when>
                    </c:choose>
                </c:if>
			</c:otherwise>
		</c:choose>
	</ycommerce:testId>
</div>
	
<div class="account-orderdetail-item-section-body">
	<ycommerce:testId code="orderDetail_itemBody_section">
		<c:forEach items="${consignment.entries}" var="entry">
			<order:orderEntryDetails orderEntry="${entry.orderEntry}" consignmentEntry="${entry}" order="${order}"/>
		</c:forEach>
	</ycommerce:testId>
</div>