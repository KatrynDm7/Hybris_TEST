<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="assistedserviceutils" uri="http://hybris.com/tld/assistedserviceutils" %>

<c:url value="/assisted-service/bind-cart" var="bindActionUrl" />
<form action="${bindActionUrl}" method="post" class="asmForm"
	id="_asmBindForm">
	<fieldset>
		<span class="ASM_icon ASM_icon-contacts hidden-xs hidden-sm hidden-md"></span>
		<div class="ASM_input_holder customerId">
			<c:choose>
				<c:when test="${emulatedUser.uid ne 'anonymous'}">
					<label for="customerName"><spring:theme
							code="asm.emulate.username.label" /></label>
					<input name="customerName" type="text" value="${emulatedUser.name}"
						class="ASM-input" readonly
						data-hover='{"name":"${emulatedUser.name}","email":"${emulatedUser.uid}","card":"${assistedserviceutils:shortCardNumber(emulatedUser)}","date":"${assistedserviceutils:creationDate(emulatedUser)}"}' />
					<input name="customerId" type="hidden" value="${emulatedUser.uid}" />
				</c:when>
				<c:otherwise>
					<c:set var="usernamePlaceholder">
						<spring:theme code="asm.emulate.username.placeholder" />
					</c:set>
					<label for="customerName"><spring:theme
							code="asm.emulate.username.label" /></label>
					<input name="customerId" type="hidden" value="${customerId}"
						placeholder="${usernamePlaceholder}" class="ASM-input" />
					<input name="customerName" type="text" value="${customerName}"
						placeholder="${usernamePlaceholder}" class="ASM-input" />
				</c:otherwise>
			</c:choose>
		</div>
		<span class="ASM_icon ASM_icon-chain invisible"></span> <span
			class="ASM_icon ASM_icon-cart hidden-xs hidden-sm hidden-md"></span>
		<div class="ASM_input_holder cartId">
			<label><spring:theme code="asm.emulate.cart.label" /></label> <input
				type="text" value="${cart.code}" orig_value="${cart.code}"
				class="ASM-input" name="cartId" />
		</div>

		<input type="hidden" name="CSRFToken" value="${CSRFToken}">

		<c:choose>
			<c:when test="${emulatedUser.uid ne 'anonymous'}">
				<button type="submit" class="ASM-btn ASM-btn-bind-cart hidden">
					<spring:theme code="asm.emulate.cart.bind" />
					<span class="hidden-xs hidden-sm"><spring:theme
							code="asm.emulate.cart.bind.ending" /></span>
				</button>
			</c:when>
			<c:otherwise>
				<button type="submit" class="ASM-btn ASM-btn-bind-cart hidden">
					<spring:theme code="asm.emulate.cart.bind" />
					<span class="hidden-xs hidden-sm"><spring:theme
							code="asm.emulate.customer.bind.ending" /></span>
				</button>
			</c:otherwise>
		</c:choose>
	</fieldset>
</form>