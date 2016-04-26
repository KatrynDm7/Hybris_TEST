<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="product" required="true" type="de.hybris.platform.commercefacades.product.data.ProductData" %>
<%@ attribute name="comparisonTable" required="false" type="de.hybris.platform.financialacceleratorstorefront.comparison.ComparisonTable" %>
<%@ attribute name="addToCartBtn_label_key" required="false" type="java.lang.String"%>
<%@ attribute name="comparisonTableColumn" required="true" type="de.hybris.platform.financialacceleratorstorefront.comparison.ComparisonTableColumn" %>
<%@ attribute name="hideOptionProducts" required="false" type="java.lang.Boolean" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product" %>
<%@ taglib prefix="financialCart" tagdir="/WEB-INF/tags/addons/financialacceleratorstorefront/desktop/cart" %>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/desktop/cart" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="storepickup" tagdir="/WEB-INF/tags/desktop/storepickup" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="action" tagdir="/WEB-INF/tags/desktop/action" %>

<spring:theme code="text.addToCart" var="addToCartText"/>
<c:url value="${product.url}" var="productUrl"/>
<c:set value="/cart/addBundle" var="addBundleUrl"/>
<spring:url var="changePlanUrl" value="/cart/changePlan">
    <spring:param name="CSRFToken" value="${CSRFToken}" />
    <spring:param name="redirectUrl" value="${addBundleUrl}" />
</spring:url>
<spring:url var="addToCartUrl" value="${addBundleUrl}">
    <spring:param name="CSRFToken" value="${CSRFToken}" />
</spring:url>

<c:set value="${not empty product.potentialPromotions}" var="hasPromotion"/>
<c:set value="${product.bundleTemplates[0]}" var="productPackage"/>
<c:set value="${product.bundleTemplates[1]}" var="productComponent"/>
<c:set value="false" var="hasRecurringCharge"/>
<c:set value="recurringAnnualPrice" var="recurringAnnualPricekey"/>
<c:set value="annualSavingPrice" var="annualSavingPricekey"/>

<c:set var="detailsCss" value="details"/>
<c:if test="${hideOptionProducts eq true}">
    <c:set var="detailsCss" value="details detailsShort"/>
</c:if>

<c:if test="${not empty comparisonTable.columns}">
    <c:forEach items="${comparisonTable.columns}" var="columnTable">
      <c:if test="${columnTable.key eq recurringAnnualPricekey}">		        	
     	<c:set value="true" var="hasRecurringCharge"/>
     </c:if>	        
     </c:forEach>	        
</c:if>

<ycommerce:testId code="product_wholeProduct">
    <div class="productGridItem ${hasPromotion ? 'productGridItemPromotion' : ''}">
        <div class="${detailsCss}">
            <ycommerce:testId code="product_productName">
                <h3>${product.name}</h3>
            </ycommerce:testId>
            <ycommerce:testId code="product_payOnCheckout">
           
                <c:choose>
	                <c:when test="${hasRecurringCharge eq true and not empty product.price.recurringChargeEntries}">
	                   <c:set var="payOnCheckout" value="${product.price.recurringChargeEntries[0]}"/>		                       
                          <div class="payOnCheckout">
                              <c:set var="priceText">
                                  <format:price priceData="${payOnCheckout.price}"/>
                              </c:set>
                              <h4>${priceText}</h4> 
                              <span class="payOnCheckoutAnnual"><spring:theme code="text.annual.price" text="Annual price: "/>&nbsp;${comparisonTable.columns[recurringAnnualPricekey].items[0]}</span>
                          </div>
	                </c:when>
	                <c:otherwise>	                	
		                <c:if test="${not empty product.price.oneTimeChargeEntries}">
		                    <c:set var="payOnCheckout" value="${product.price.oneTimeChargeEntries[0]}"/>
		                    <c:choose>
		                        <c:when test="${payOnCheckout.billingTime.code eq 'paynow'}">
		                            <div class="payOnCheckout">
		                                <c:set var="priceText">
		                                    <format:price priceData="${payOnCheckout.price}"/>
		                                </c:set>
		                                <h4>${priceText}</h4>	
		                                  <c:if test="${hasRecurringCharge eq true }">
                                            <span class="payOnCheckoutMonthly"><spring:theme code="text.saving.against.monthly" text="Saving against Monthly: "/>&nbsp;${comparisonTable.columns[annualSavingPricekey].items[0]}</span>
		                                  </c:if>                              
		                            </div>
		                        </c:when>                       
		                    </c:choose>
		                </c:if>
	                </c:otherwise>
            	</c:choose>
            </ycommerce:testId>

            <form class="addToCartForm" action="${changePlanUrl}" data-addtocart="${addToCartUrl}" method="post">
                <input type="hidden" name="bundleTemplateIds" value="${productPackage.id}"/>
                <input type="hidden" name="productCodes" value="${product.code}">
                <c:if test="${hideOptionProducts eq false}">
                    <div class="optionalProducts">
                        <c:forEach var="optionalProduct" items="${productComponent.products}">
                            <c:if test="${not optionalProduct.disabled }">
                                <c:if test="${not empty optionalProduct.price.oneTimeChargeEntries}">
                                    <c:set var="optionalProductPriceText">
                                        <format:price priceData="${optionalProduct.price.oneTimeChargeEntries[0].price}"/>
                                    </c:set>
                                </c:if>
                                <p>
                                    <input type="checkbox" name="productCodes" value="${optionalProduct.code}">${optionalProductPriceText}
                                    <span>${optionalProduct.name}</span>
                                </p>
                                <input type="hidden" name="bundleTemplateIds" value="${productComponent.id}">
                            </c:if>
                        </c:forEach>
                    </div>
                </c:if>
                <c:if test="${not empty mandatoryOptionProducts.results}">
                	<c:forEach items="${mandatoryOptionProducts.results}" var="mandatoryOptionProduct">
                		<c:if test="${mandatoryOptionProduct.code eq product.code}">
                			<div class="mandatoryBundleProducts">
                                <input id='mandatoryBundleProduct' data-code="${mandatoryOptionProduct.code}" type='checkbox' name='Add Telemetry'> <spring:theme code="text.mandatory.bundle.check.label" text="Tick this checkbox to enable monthly plan"/>
                			</div>
                		</c:if>
                	</c:forEach>
                </c:if>
                <c:choose>
                    <c:when test="${not empty addToCartBtn_label_key}">
                        <input type="submit" id="${product.code}" value="<spring:theme code='${addToCartBtn_label_key}'/>">
                    </c:when>
                    <c:otherwise>
                        <input type="submit" id="${product.code}" value="<spring:message code='basket.add.to.basket'/>">
                    </c:otherwise>
                </c:choose>
            </form>
        </div>

        <div class="priceContainer">
            <c:set var="buttonType">submit</c:set>
            <ycommerce:testId code="product_productPrice">              
                <c:forEach items="${comparisonTableColumn.items}" var="oneTimeChargeEntry">
                    <div class="price">
                        <c:choose>
                            <c:when test="${empty oneTimeChargeEntry}">
                                <p>-</p>
                            </c:when>
                            <c:otherwise>
                                <c:set var="priceText">
                                    <format:price priceData="${oneTimeChargeEntry.price}"/>
                                </c:set>
                                <p><spring:theme code="text.price.up.to"/>${priceText}</p>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </c:forEach>           

            </ycommerce:testId>
        </div>

        <c:choose>
            <c:when test="${product.stock.stockLevelStatus.code eq 'outOfStock' }">
                <c:set var="buttonType">button</c:set>
                <spring:theme code="text.addToCart.outOfStock" var="addToCartText"/>
                <span class='listProductLowStock listProductOutOfStock mlist-stock'>${addToCartText}</span>
            </c:when>
        </c:choose>

        <div class="cart clearfix">
            <c:if test="${not empty product.averageRating}">
                <product:productStars rating="${product.averageRating}"/>
            </c:if>

            <c:set var="product" value="${product}" scope="request"/>
            <c:set var="addToCartText" value="${addToCartText}" scope="request"/>
            <c:set var="addToCartUrl" value="${addToCartUrl}" scope="request"/>
            <div id="actions-container-for-${component.uid}" class="listAddPickupContainer clearfix">
                <action:actions element="div" parentComponent="${component}"/>
            </div>
        </div>
    </div>
</ycommerce:testId>