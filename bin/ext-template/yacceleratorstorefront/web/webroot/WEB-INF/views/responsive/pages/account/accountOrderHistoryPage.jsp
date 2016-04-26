<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="pagination" tagdir="/WEB-INF/tags/responsive/nav/pagination" %>


<spring:url value="/my-account/order/" var="orderDetailsUrl"/>
<c:set var="searchUrl" value="/my-account/orders?sort=${searchPageData.pagination.sort}"/>

<c:if test="${empty searchPageData.results}">
	<div class="account-section-header">
		<spring:theme code="text.account.orderHistory.emptyOrderHistory" />
	</div>
	<div class="account-section-content	 account-section-content-small ">
		<ycommerce:testId code="orderHistory_noOrders_label">
			<spring:theme code="text.account.orderHistory.noOrders" />
		</ycommerce:testId>
	</div>
</c:if>
<c:if test="${not empty searchPageData.results}">
	<div class="account-section-content	">
		<div class="account-orderhistory">
			<div class="account-orderhistory-pagination">
				<nav:pagination top="true" msgKey="text.account.orderHistory.page" showCurrentPageInfo="true" hideRefineButton="true" supportShowPaged="${isShowPageAllowed}" supportShowAll="${isShowAllAllowed}" searchPageData="${searchPageData}" searchUrl="${searchUrl}"  numberPagesShown="${numberPagesShown}"/>
			</div>
			
			<div class="account-orderhistory-list">
				<ul>
					<c:forEach items="${searchPageData.results}" var="order">
						<li class="account-orderhistory-list-item">
							<ycommerce:testId code="orderHistoryItem_orderDetails_link">
								<a href="${orderDetailsUrl}${order.code}">
									<div class="row">
										<div class="col-xs-6">
											<div class="order-id"><spring:theme code="text.account.order.order"/>&nbsp;<span class="order-list-id">${order.code}</span></div>
										</div>
										<div class="col-xs-6">
											<div class="place-at text-right">
												<fmt:formatDate value="${order.placed}" dateStyle="medium" timeStyle="short" type="both"/>
											</div>
										</div>
										<br>
										<div class="col-xs-6">
											<div class="status">
												<spring:theme code="text.account.order.status.display.${order.statusDisplay}"/>
											</div>
										</div>
									</div>
								</a>
							</ycommerce:testId>
						</li>
					</c:forEach>
				</ul>
			</div>
		</div>
		<div class="account-orderhistory-pagination">
			<nav:pagination top="false" msgKey="text.account.orderHistory.page" showCurrentPageInfo="true" supportShowPaged="${isShowPageAllowed}" supportShowAll="${isShowAllAllowed}" searchPageData="${searchPageData}" searchUrl="${searchUrl}"  numberPagesShown="${numberPagesShown}"/>
		</div>
	</div>
</c:if>