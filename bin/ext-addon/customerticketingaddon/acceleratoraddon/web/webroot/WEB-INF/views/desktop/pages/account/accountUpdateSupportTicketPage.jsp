<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/desktop/formElement" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>

<div class="span-24">
	<div class="span-20 last">
		<div class="accountContentPane clearfix">
			<div class="headline"><spring:theme code="text.account.supportTicket.updateTicket.heading" text="Request Customer Support"/></div>
			<div class="required right"><spring:theme code="form.required" text="Fields marked * are required"/></div>
			<form:form method="post" commandName="supportTicketForm">
				<input id="currentTicketStatus" type="hidden" value="${ticketData.status.id}">
				<table class="supportTicketTableUpdate">
					<tr>
						<td><label for="text.account.supporttickets.createTicket.subject"><spring:message code="text.account.supporttickets.createTicket.subject" text="Subject"/> </label></td>
					</tr>
					<tr>
						<td class="supportTicketBR">
							<div class="control-group">
								<form:input path="subject" value="${ticketData.subject}" readonly="true" size="52"/>
							</div>
						</td>
					</tr>

					<tr>
						<td><label><spring:message code="text.account.supporttickets.createTicket.message.history"/> &nbsp;</label></td>
					</tr>
					<tr>
						<td class="supportTicketBR">
							<div class="control-group">
								<div class="controls">
									<textarea readonly="readonly" >${ticketData.messageHistory}</textarea>
								</div>
							</div>
						</td>
					</tr>

					<c:if test="${ticketData.status.id ne 'CLOSED'}">
						<tr>
							<td>
								<form:hidden path="id" value="${ticketData.id}"/>
								<form:hidden path="subject" value="${ticketData.subject}"/>
								<formElement:formTextArea idKey="message" labelKey="text.account.supporttickets.createTicket.message" path="message"/>
							</td>
						</tr>
						<tr>
							<td class="supportTicketBR" colspan="2" align="center" ><spring:theme code="text.account.supporttickets.updateTicket.message.desc" text="Update Message"/></td>
						</tr>
					</c:if>

					<tr>
						<td><label for="text.account.supportTicket.updateTicket.status"><spring:message code="text.account.supportTicket.updateTicket.status" text="Status"/> </label></td>
					</tr>
					<tr>
						<td class="supportTicketBR">
							<form:select path="status">
								<form:option value="${ticketData.status.id}"><spring:message code="ticketstatus.${fn:toUpperCase(ticketData.status.id)}"/> (<spring:message code="text.account.supporttickets.currentStatus=" text="Current Status"/>)</form:option>
								<c:forEach items="${ticketData.availableStatusTransitions}" var="status">
									<form:option value="${status.id}" ><spring:message code="ticketstatus.box.${status.id}"/> </form:option>
								</c:forEach>
							</form:select>
						</td>
					</tr>



				</table>

				<div id="addressform_button_panel" class="form-actions">
					<c:if test="${ticketData.status.id ne 'CLOSED'}">
						<ycommerce:testId code="supportTicket_update_button">
							<button class="positive right" id="updateTicket" type="submit">
								<spring:theme code="text.account.supporttickets.createTicket.submit" text="Submit"/>
							</button>
						</ycommerce:testId>
					</c:if>
					<a href="../support-tickets" class="button">
						<spring:theme code="text.account.supporttickets.createTicket.back" text="Back" />
					</a>
				</div>
			</form:form>
		</div>
	</div>
</div>