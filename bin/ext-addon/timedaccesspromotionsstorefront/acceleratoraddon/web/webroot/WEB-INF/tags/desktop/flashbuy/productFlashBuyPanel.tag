<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="storepickup"
	tagdir="/WEB-INF/tags/desktop/storepickup"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>



<c:url value="" var="addToCartUrl" />
<c:set var="scValue" value="secondContent"/>
<c:if test="${orderCompleteStrategy == 'B2C'}">
   <c:set var="scValue" value="secondContentB2C"/>
</c:if>
<form:form method="post" id="addToCartForm" action="${addToCartUrl}">
<div class="flashbuy-frame">
	<div class="flashbuy-line">
		<span class="flashbuy-label"> <spring:theme code="flashbuy.label.account" /></span> 
		<input class="firstContent" name="count" id="flashbuy-input-qty" value="1"></input>
		<input style="display:none" mce_style="display:none"/> 
		<input type="hidden" class="firstContent" id="max-available-qty" value="${product.specialPromotions[0].availableUnitsPerUserAndProduct}"/>
		<c:choose>
			<c:when test="${soldout}">
				<span class="flashbuy-label secondLabel late"> 
					<spring:theme code="flashbuy.label.late" />
				</span>
			</c:when>
			<c:otherwise>
				<span class="flashbuy-label secondLabel"> <spring:theme code="flashbuy.label.maxmum" />
				</span> 
				<span class="flashbuy-label ${scValue} }"> ${product.specialPromotions[0].availableUnitsPerProduct} </span>
				<input type="hidden" id="process-text" value="<spring:theme code="flashbuy.waiting.queue"/>"/>
			</c:otherwise>
		</c:choose>
	</div>
	<div class="errorMessage" id="errorMessage">
		<input type="hidden" id="input-not-digit" value="<spring:theme code="flashbuy.message.amount.error"/>"/>
		<input type="hidden" id="input-over-max" value="<spring:theme code="flashbuy.message.amount.overmaxmum"/>"/>
	</div>
	<div class="flashbuy-line">
		<span class="flashbuy-label"> <spring:theme code="flashbuy.label.flashbuy.price" />
		</span> <span class="firstContent">  <format:fromPrice priceData="${product.specialPromotions[0].fixedPrice}" /> </span> <span
			class="flashbuy-label secondLabel"> <spring:theme code="flashbuy.label.market.price" />
		</span>
		<del class="${scValue}"> <format:fromPrice priceData="${product.price}" /> </del>
	</div>
	<div class="countdownbox">
		<input type="hidden" id="unit_d" value="<spring:theme code="flashbuy.label.countdown.day"/>"/>
		<input type="hidden" id="unit_h" value="<spring:theme code="flashbuy.label.countdown.hour"/>" />
		<input type="hidden" id="unit_m" value="<spring:theme code="flashbuy.label.countdown.minute"/>" />
		<input type="hidden" id="unit_s" value="<spring:theme code="flashbuy.label.countdown.second"/>" />
		<input type="hidden" id="bottom_text" value="<spring:theme code="flashbuy.until.toStart"/>" />
		<input type="hidden" id="startTime" value="${product.specialPromotions[0].countDownTime}" />
		<input type="hidden" id="showResultInterval" value="${showResultInterval}" />
		<input type="hidden" id="h-soldout" value="${soldout}" />
	</div>
	<div id="addToCartButton" class="addToCartButton disableButton">
		<c:choose>
			<c:when test="${soldout}">
				<spring:theme code="flashbuy.label.btn.soldout"/>
			</c:when>
			<c:otherwise>
				<spring:theme code="flashbuy.label.btn.buy" />
			</c:otherwise>
		</c:choose>
	</div>	
</div>

</form:form>


