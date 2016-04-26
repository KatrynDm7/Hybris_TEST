<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/desktop/nav" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>


<div class="headline">
	<spring:theme code="text.account.quotes.myquotes"/>
</div>
<c:if test="${not empty searchPageData.results}">
	<div class="description">
		<spring:theme code="text.account.quoteHistory.viewOrders"/>
	</div>
	<nav:pagination top="true"  supportShowPaged="${isShowPageAllowed}" supportShowAll="${isShowAllAllowed}" searchPageData="${searchPageData}" searchUrl="/my-account/my-quotes?sort=${searchPageData.pagination.sort}" msgKey="text.account.orderHistory.page" numberPagesShown="${numberPagesShown}"/>
	<div class="orderList">
		<table id="order_history" class="orderListTable">
			<thead>
				<tr>
					<th id="header1"><spring:theme code="text.account.quoteHistory.orderNumber"/></th>
					<th id="header2"><spring:theme code="text.account.quoteHistory.orderStatus"/></th>
					<th id="header3"><spring:theme code="text.account.quoteHistory.purchaseOrderNumber"/></th>
					<th id="header4"><spring:theme code="text.account.quoteHistory.datePlaced"/></th>
					<th id="header5"><spring:theme code="text.account.quoteHistory.accountManager"/></th>
					<th id="header6"><spring:theme code="text.account.quoteHistory.actions"/></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${searchPageData.results}" var="order">
					<c:url value="/my-account/my-quote/${order.code}" var="orderQuoteLink" />
					<tr>
						<td headers="header1">
							<ycommerce:testId code="quoteHistory_orderNumber_link">
								<a href="${orderQuoteLink}">${order.code}</a>
							</ycommerce:testId>
						</td>
						<td headers="header2">
							<ycommerce:testId code="quoteHistory_orderStatus_label">
								<p><spring:theme code="text.account.order.status.display.${order.statusDisplay}"/></p>
							</ycommerce:testId>
						</td>
						<td headers="header3">
							<ycommerce:testId code="quoteHistory_purchaseOrderNumber_label">
								<p>${order.purchaseOrderNumber}</p>
							</ycommerce:testId>
						</td>
						<td headers="header4">
							<ycommerce:testId code="quoteHistory_orderDate_label">
								<p>${order.placed}</p>
							</ycommerce:testId>
						</td>
						<td headers="header5">
							<ycommerce:testId code="quoteHistory_accountManager_label">
								<c:forEach items="${order.managers}" var="manager">
									<p>${manager.name} [${manager.uid}]</p>
								</c:forEach>
							</ycommerce:testId>
						</td>
						<td headers="header6">
							<ycommerce:testId code="quoteHistory_Actions_links">
								<ul class="updates">
									<li><a href="${orderQuoteLink}">
										<spring:theme code="text.view"/></a></li>
								</ul>
							</ycommerce:testId>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	<nav:pagination top="false"  supportShowPaged="${isShowPageAllowed}" supportShowAll="${isShowAllAllowed}" searchPageData="${searchPageData}" searchUrl="/my-account/my-quotes?sort=${searchPageData.pagination.sort}" msgKey="text.account.orderHistory.page" numberPagesShown="${numberPagesShown}"/>
</c:if>
<c:if test="${empty searchPageData.results}">
	<p><spring:theme code="text.account.quotes.noQuotes"/></p>
</c:if>
