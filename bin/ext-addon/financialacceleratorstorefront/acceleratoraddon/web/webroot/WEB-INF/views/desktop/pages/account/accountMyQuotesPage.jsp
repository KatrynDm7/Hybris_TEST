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
<%@ taglib prefix="breadcrumb" tagdir="/WEB-INF/tags/desktop/nav/breadcrumb"%>
<%@ taglib prefix="action" tagdir="/WEB-INF/tags/desktop/action"%>
<%@ taglib prefix="financialMyAccount" tagdir="/WEB-INF/tags/addons/financialacceleratorstorefront/desktop/account" %>

<div class="headline">
    <spring:theme code="text.account.myQuotes" text="My Quotes" />
</div>

<c:choose>
    <c:when test="${empty quotes}">
        <p>
            <spring:theme code="text.account.myQuotes.noQuotes" text="You have no quotes" />
        </p>
    </c:when>
    <c:otherwise>
        <p>
            <spring:theme code="text.account.myQuotes.viewAndActivate.section.header" text="View and activate your Saved Quotes" />
        </p>
        <table id="myQuotesList" class="quotesListTable">
            <tr>
                <th colspan="2" class="planname"><spring:theme code="text.account.myQuotes.plan.name" text="Plan Name"/></th>
                <th><spring:theme code="text.account.myQuotes.quote.status" text="Quote status"/></th>
                <th><spring:theme code="text.account.myQuotes.quote.expiry.date" text="Expires"/></th>
                <th><spring:theme code="text.account.myQuotes.quote.price" text="Price"/></th>
                <th></th>
            </tr>
            <spring:url var="retrieveQuoteUrl" value="/checkout/multi/retrieveQuote">
                <spring:param name="CSRFToken" value="${CSRFToken}" />
            </spring:url>
            <c:forEach items="${quotes}" var="quote">
                <c:set var="thumbnail_img" value=""/>
                <c:forEach items="${quote.quoteImages}" var="image">
                    <c:if test="${image.format == '40Wx40H_quote'}">
                        <c:set var="thumbnail_img" value="${image}"/>
                    </c:if>
                </c:forEach>
                <tr>
                    <td><span class="insImg"><c:if test="${not empty thumbnail_img}"><img src="${thumbnail_img.url}"/></c:if></span></td>
                    <td>${quote.planName}<br/>${quote.quoteNumber}</td>
                    <c:set var="quoteStatus">
                        <spring:theme code="text.account.myQuotes.quote.status.${quote.quoteStatus}" text="${quote.quoteStatus}" />
                    </c:set>
                    <c:choose>
                        <c:when test="${quote.quoteWorkflowStatus eq 'REJECTED'}">
                            <c:set var="quoteStatus">
                                <spring:theme code="text.account.myQuotes.quote.status.rejected" text="Rejected" />
                            </c:set>
                        </c:when>
                        <c:otherwise>
                            <c:choose>
                                <c:when test="${quote.quoteIsExpired}">
                                    <c:set var="quoteStatus">
                                        <spring:theme code="text.account.myQuotes.quote.status.expired" text="Expired"/>
                                    </c:set>
                                </c:when>
                                <c:otherwise>
                                    <c:choose>
                                        <c:when test="${quote.quoteIsBind and quote.quoteWorkflowStatus eq 'APPROVED'}">
                                            <c:set var="quoteStatus">
                                                <spring:theme code="text.account.myQuotes.quote.status.readyForPurchase" text="Ready for purchase"/>
                                            </c:set>
                                        </c:when>
                                        <c:otherwise>
                                            <c:set var="quoteStatus">
                                                <spring:theme code="text.account.myQuotes.quote.status.unfinished" text="Unfinished"/>
                                            </c:set>
                                        </c:otherwise>
                                    </c:choose>
                                </c:otherwise>
                            </c:choose>
                        </c:otherwise>
                    </c:choose>
                    <td <c:if test="${quote.quoteIsExpired or quote.quoteWorkflowStatus eq 'REJECTED'}">class="expired"</c:if>>${quoteStatus}</td>
                    <td <c:if test="${quote.quoteIsExpired}">class="expired"</c:if>><c:if test="${quote.quoteWorkflowStatus ne 'REJECTED'}">${quote.quoteExpiryDate}</c:if></td>
                    <td><c:choose>
                        <c:when test="${!quote.quoteIsExpired and quote.quoteWorkflowStatus ne 'REJECTED'}">
                            ${quote.quotePrice}<br/>
                            <c:choose>
                                <c:when test="${quote.isMonthly }">
                                    <spring:theme code="checkout.cart.payment.frequency.monthly" text="Monthly" />
                                </c:when>
                                <c:otherwise>
                                    <spring:theme code="checkout.cart.payment.frequency.annual" text="Annual" />
                                </c:otherwise>
                            </c:choose>
				        </c:when>
				        <c:otherwise>
                            <c:if test="${quote.quoteWorkflowStatus ne 'REJECTED'}">
                                <spring:theme code="checkout.cart.payment.notApplicable" text="n/a" />
                            </c:if>
				        </c:otherwise>
				        </c:choose>
                    </td>
                    <c:choose>
                    <c:when test="${!quote.quoteIsExpired and quote.quoteWorkflowStatus ne 'REJECTED'}">
	                    <td class="insActionBtn">
	                            <form class="retrieveQuoteForm" action="${retrieveQuoteUrl}" method="POST">
	                                <input name="code" value="${quote.retrieveQuoteCartCode}" type="hidden"/>
		                            <input name="cartCode" value="${quote.currentCartCode}" type="hidden"/>
	                                <a href="#" type="submit" class="retrieveBtn button secondary"><spring:theme code="text.account.myQuote.quote.retrieve" text="Retrieve"/></a>
	                            </form>
	                    </td>
                    </c:when>
	                    <c:otherwise>
	                    		<td>&nbsp;</td>
	                    </c:otherwise>
                    </c:choose>
                </tr>
            </c:forEach>
        </table>
    </c:otherwise>
</c:choose>

<financialMyAccount:confirmRetrieveQuoteActionPopup/>