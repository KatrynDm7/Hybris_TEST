<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/desktop/nav"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="breadcrumb"
	tagdir="/WEB-INF/tags/desktop/nav/breadcrumb"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<div class="headline">
	<spring:theme code="text.account.supporttickets" text="Support Tickets" />
</div>
<a href="add-support-ticket" class="right button positive">
	<spring:theme code="text.account.supporttickets.requestSupport" text="Request Support" />
</a>

<c:if test="${not empty supportTickets}">
	<div class="description">
		<spring:theme code="text.account.supporttickets.subHeading" text="Manage your support tickets" />
	</div>

	<table class="supportTicketsTable">
		<thead>
			<tr>
				<th><spring:theme code="text.account.supporttickets.subject" text="Subject" /></th>
				<th><spring:theme code="text.account.supporttickets.dateCreated" text="Date Created" /></th>
				<th><spring:theme code="text.account.supporttickets.dateUpdated" text="Date Updated" /></th>
				<th class="supportTicketsTableState"><spring:theme code="text.account.supporttickets.status" text="Status" /></th>
				<th></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${supportTickets}" var="supportTicket">
				<c:url value="/my-account/support-ticket/${supportTicket.id}" var="myAccountsupportTicketDetailsUrl" />
				<tr>
					<td><c:out value="${supportTicket.subject}" /></td>
					<td><fmt:formatDate value="${supportTicket.creationDate}" dateStyle="long" timeStyle="short" type="date" /></td>
					<td><fmt:formatDate value="${supportTicket.lastModificationDate}" dateStyle="long" timeStyle="short" type="date" /></td>
					<td><spring:message code="ticketstatus.${fn:toUpperCase(supportTicket.status.id)}"/></td>
					<td>
						<ycommerce:testId code="supportTicket_view_details_link">
							<a href="${myAccountsupportTicketDetailsUrl}" class="right button positive"> 
								<spring:theme code="text.account.supporttickets.viewDetails" text="View Details" />
							</a>
						</ycommerce:testId>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</c:if>

<c:if test="${empty supportTickets}">
	<p class="emptyMessage">
		<spring:theme code="text.account.supporttickets.noSupporttickets" text="You have no support tickets" />
	</p>
</c:if>
