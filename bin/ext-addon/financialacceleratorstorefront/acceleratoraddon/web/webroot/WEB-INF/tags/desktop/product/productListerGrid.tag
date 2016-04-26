<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@attribute name="columns" required="true" type="java.lang.Integer"%>
<%@attribute name="addToCartBtn_label_key" required="false" type="java.lang.String"%>
<%@attribute name="searchPageData" required="true" type="de.hybris.platform.commerceservices.search.pagedata.SearchPageData" %>
<%@ attribute name="hideOptionProducts" required="false" type="java.lang.Boolean" %>
<%@attribute name="imageComponent" required="false" type="de.hybris.platform.cms2.model.contents.components.CMSImageComponentModel" %>
<%@ taglib prefix="financialtags" uri="http://hybris.com/tld/financialtags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="product" tagdir="/WEB-INF/tags/addons/financialacceleratorstorefront/desktop/product" %>

<financialtags:comparisonTable searchPageData="${searchPageData}" tableFactory="insuranceComparisonTableFactory" var="comparisonTable"/>
<c:if test="${empty comparisonTable }">
  <script>
   window.location.replace("${request.contextPath}");
  </script>
</c:if>
<%--Find out the number of package product, so able to determine the width of the columns--%>
<c:forEach items="${searchPageData.results}" var="product" varStatus="status">
    <c:set value="${product.bundleTemplates[0]}" var="productPackage"/>
    <c:forEach items="${productPackage.products}" var="bundleItem">
        <c:if test="${bundleItem.code eq product.code}">
            <c:set value="${productCount + 1}" var="productCount"/>
        </c:if>
    </c:forEach>
</c:forEach>
<c:set value="${100/(productCount+1)-1}" var="columnWidth"/>
<c:set value="label" var="labelcolumnkey"/>

<c:set var="detailsCss" value="details"/>
<c:if test="${hideOptionProducts eq true}">
    <c:set var="detailsCss" value="details detailsShort"/>    
</c:if>

<div class="productGrid">

    <%-- Label column at the left hand side.--%>
    <div class="productItem" style="width:${columnWidth}%">   
        <div class="productGridItem">         
            <div class="${detailsCss}">   
             <c:if test="${not empty imageComponent.getMedia()}">
             	<img src="${imageComponent.getMedia().getURL()}" id="categoryImage" />
             </c:if>
             </div>
            <div class="priceContainer">
                <c:forEach var="label" items="${comparisonTable.columns[labelcolumnkey].items}">
                    <div class="price">
                        <c:if test="${not empty label.helpContent}">
                         <div class="holder">
                            <div class="tooltip">i</div>
                            <span class="tip">
                                <span class="closeTip">X</span>
                                <span class="contentTip">${label.helpContent}</span>
                            </span>
                         </div>
                        </c:if>
                        <c:choose>
                            <c:when test="${not empty label.helpContent}">
                             <div class="labelNameLink">${label.name}</div>
                            </c:when>
                            <c:otherwise>
                            <div class="labelName">${label.name}</div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>
    <%--Product details in columns for comparison--%>
    <c:if test="${not empty comparisonTable.columns}">
        <c:forEach items="${comparisonTable.columns}" var="columnTable">
            <c:if test="${columnTable.key.getClass().name =='de.hybris.platform.commercefacades.product.data.ProductData'}">  
                <div class="productItem" style="width:${columnWidth}%">
                    <product:productListerGridItem comparisonTable ="${comparisonTable}" comparisonTableColumn="${columnTable.value}" product="${columnTable.key}" hideOptionProducts="${hideOptionProducts}" addToCartBtn_label_key="${addToCartBtn_label_key}"/>
                </div>
            </c:if>
        </c:forEach>
    </c:if>
</div>
