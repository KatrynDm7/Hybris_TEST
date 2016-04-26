<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/desktop/formElement" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>

<div class="span-24">
	<div class="span-20 last">
		<div class="accountContentPane clearfix">
			<div class="headline"><spring:theme code="text.account.supporttickets.createTicket.heading" text="Request Customer Support"/></div>
			<div class="required right"><spring:theme code="form.required" text="Fields marked * are required"/></div>
			
			<form:form method="post" commandName="supportTicketForm">
				<table>
					<tr>
						<td>
							<formElement:formInputBox idKey="text.account.supporttickets.createTicket.subject" labelKey="text.account.supporttickets.createTicket.subject" path="subject" inputCSS="text" mandatory="true"/>
						</td>
					</tr>
					<tr>
						<td>
							<spring:theme code="text.account.supporttickets.createTicket.subject.desc" text="Enter Subject"/>
						</td>
					</tr>
					<tr>
						<td>
							<formElement:formTextArea idKey="text.account.supporttickets.createTicket.message" labelKey="text.account.supporttickets.createTicket.message" path="message" mandatory="true"/>
						</td>
					</tr>
					<tr>
						<td>
							<spring:theme code="text.account.supporttickets.createTicket.message.desc" text="Enter Message"/>
						</td>
					</tr>
				</table>
				
				<div id="addressform_button_panel" class="form-actions">
					<ycommerce:testId code="supportTicket_create_button">
						<button class="positive right" type="submit">
							<spring:theme code="text.account.supporttickets.createTicket.submit" text="Submit"/>
						</button>
					</ycommerce:testId>
					
					<a href="support-tickets" class="button"> 
						<spring:theme code="text.account.supporttickets.createTicket.back" text="Back" />
					</a>
				</div>
			</form:form>
		</div>
	</div>
</div>