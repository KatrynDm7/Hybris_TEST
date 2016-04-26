<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="financialMyAccount" tagdir="/WEB-INF/tags/addons/financialacceleratorstorefront/desktop/account" %>

<c:url value="/my-account/my-quotes" var="myAccQuotePageUrlLink"/>
<c:choose>
	<c:when test="${isAnonymousUser == true}">
       <div class="retrieveQuote">
	        <a class="button special1" href="${myAccQuotePageUrlLink}"><spring:theme code="homepage.viewquotescomponent.anonymoususer.button.label"/></a>
        </div>
    </c:when>
    <%--@elvariable id="quote" type="de.hybris.platform.commercefacades.insurance.data.InsuranceQuoteListingData"--%>
	<c:otherwise>
		<c:if test="${not empty quotes or policiesExists}">
			<c:set value="${numDisplayableQuotes}" var="maxItems" />
			<c:choose>
			    <c:when test="${not empty quotes}">
			        <div id="myQuotesList" class="myQuotes">
                          <h2><spring:theme code="text.home.retrieveQuotes" text="Retrieve quotes"/></h2>
			              <ul>
			                <spring:url var="retrieveQuoteUrl" value="/checkout/multi/retrieveQuote">
			                    <spring:param name="CSRFToken" value="${CSRFToken}" />
			                </spring:url>
			                <c:forEach items="${quotes}" begin="0" end="${maxItems - 1}" var="quote">
			                    <c:set var="thumbnail_img" value=""/>
                                <c:set var="expiredCSS" value=""/>
			                    <c:forEach items="${quote.quoteImages}" var="image">
			                        <c:if test="${image.format == '40Wx40H_quote'}">
			                            <c:set var="thumbnail_img" value="${image}"/>
			                        </c:if>
			                    </c:forEach>
                                <c:set var="canProgress" value="${!(quote.quoteIsExpired or quote.quoteWorkflowStatus eq 'REJECTED')}"/>
                                <c:if test="${!canProgress}">
                                    <c:set var="expiredCSS" value="expired"/>
                                </c:if>
                                <c:if test="${canProgress}">
                                    <form class="retrieveQuoteForm" action="${retrieveQuoteUrl}" method="GET">
                                    <input name="code" value="${quote.retrieveQuoteCartCode}" type="hidden"/>
                                    <input name="cartCode" value="${quote.currentCartCode}" type="hidden"/>
                                    <a href="" type="submit" class="retrieveBtn">
                                </c:if>
			                            <li class="my-quotes">
			                                <div class="dataContainer">
			                                    <span class="insImg"><c:if test="${not empty thumbnail_img}"><img src="${thumbnail_img.url}"/></c:if></span>
			                                    <span class="insName">${quote.planName}</span>
			                                    <span class="insNumber">${quote.quoteNumber}</span>
			                                    <span class="addInfos ${expiredCSS}">
                                                <c:choose>
                                                    <c:when test="${quote.quoteWorkflowStatus eq 'REJECTED'}">
                                                        <spring:theme code="homepage.viewquotescomponent.quote.rejected.text" />
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:choose>
                                                            <c:when test="${quote.quoteIsExpired}">
                                                                <spring:theme code="homepage.viewquotescomponent.expires.text"/>&nbsp;${quote.quoteExpiryDate}
                                                            </c:when>
                                                            <c:otherwise>
                                                                <c:choose>
                                                                    <c:when test="${quote.quoteIsBind and quote.quoteWorkflowStatus eq 'APPROVED'}">
                                                                        <spring:theme code="homepage.viewquotescomponent.expires.text"/>&nbsp;${quote.quoteExpiryDate}
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <spring:theme code="homepage.viewquotescomponent.unfinished.text"/>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </c:otherwise>
                                                </c:choose>
			                                    </span>
			                                </div>
			                            </li>
			                        <c:if test="${canProgress}">
                                        </a></form>
                                    </c:if>
			                </c:forEach>
			            </ul>
			            <c:if test="${quotes.size() gt maxItems}">
			                <p><a href="${myAccQuotePageUrlLink}"><spring:theme code="homepage.viewquotescomponent.too.many" /></a></p>
			            </c:if>
			        </div>
			    </c:when>
			    <c:otherwise>
			    	<div class="retrieveQuote noneQuote">
						<p><spring:theme code="homepage.viewquotescomponent.empty.quotes.title1"/><br>
						<spring:theme code="homepage.viewquotescomponent.empty.quotes.title2"/></p>
					</div>
			    </c:otherwise>
			</c:choose>
		</c:if>
	</c:otherwise>
</c:choose>
<financialMyAccount:confirmRetrieveQuoteActionPopup/>