<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="quoteData" required="true"
              type="de.hybris.platform.commercefacades.quotation.InsuranceQuoteData" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:if test="${not empty quoteData.lifeDetail.lifeWhoCovered}">
    <li>
        <div class="left">
            <spring:theme code="checkout.multi.quoteReview.life.who.covered" text="Who is being covered: "/>
        </div>
        <div class="right">${quoteData.lifeDetail.lifeWhoCovered}</div>
    </li>
</c:if>
<c:if test="${not empty quoteData.lifeDetail.lifeCoverageAmount}">
    <li>
        <div class="left">
            <spring:theme code="checkout.multi.quoteReview.life.coverage.amount" text="Coverage amount: "/>
        </div>
        <div class="right">${quoteData.lifeDetail.lifeCoverageAmount}</div>
    </li>
</c:if>
<c:if test="${not empty quoteData.lifeDetail.lifeCoverageLast}">
    <li>
        <div class="left">
            <spring:theme code="checkout.multi.quoteReview.life.length.of.coverage" text="Length of coverage: "/>
        </div>
        <div class="right">${quoteData.lifeDetail.lifeCoverageLast}</div>
    </li>
</c:if>
<c:if test="${not empty quoteData.lifeDetail.lifeCoverStartDate}">
    <li>
        <div class="left">
            <spring:theme code="checkout.multi.quoteReview.life.start.date" text="Start date: "/>
        </div>
        <div class="right">${quoteData.lifeDetail.lifeCoverStartDate}</div>
    </li>
</c:if>
<c:if test="${not empty quoteData.lifeDetail.lifeMainDob}">
    <li>
        <div class="left">
            <spring:theme code="checkout.multi.quoteReview.life.date.of.birth" text="Date Of Birth: "/>
        </div>
        <div class="right">${quoteData.lifeDetail.lifeMainDob}</div>
    </li>
</c:if>
<c:if test="${not empty quoteData.lifeDetail.lifeMainSmoke}">
    <li>
        <div class="left">
            <spring:theme code="checkout.multi.quoteReview.life.smoke" text="Do you smoke: "/>
        </div>
        <div class="right">${quoteData.lifeDetail.lifeMainSmoke}</div>
    </li>
</c:if>
<c:if test="${not empty quoteData.lifeDetail.lifeSecondDob and not empty quoteData.lifeDetail.lifeSecondSmoke and not empty quoteData.lifeDetail.lifeRelationship}">
    <li>
        <div class="left">
            <spring:theme code="checkout.multi.quoteReview.life.person.two" text="Person Two"/>
        </div>
    </li>
</c:if>
<c:if test="${not empty quoteData.lifeDetail.lifeSecondDob}">
    <li>
        <div class="left">
            <spring:theme code="checkout.multi.quoteReview.life.date.of.birth" text="Date Of Birth: "/>
        </div>
        <div class="right">${quoteData.lifeDetail.lifeSecondDob}</div>
    </li>
</c:if>
<c:if test="${not empty quoteData.lifeDetail.lifeSecondSmoke}">
    <li>
        <div class="left">
            <spring:theme code="checkout.multi.quoteReview.life.smoke" text="Do you smoke: "/>
        </div>
        <div class="right">${quoteData.lifeDetail.lifeSecondSmoke}</div>
    </li>
</c:if>
<c:if test="${not empty quoteData.lifeDetail.lifeRelationship}">
    <li>
        <div class="left">
            <spring:theme code="checkout.multi.quoteReview.life.relationship" text="Relationship to the second person: "/>
        </div>
        <div class="right">${quoteData.lifeDetail.lifeRelationship}</div>
    </li>
</c:if>

