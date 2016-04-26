<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="categories" required="true" type="java.util.List" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>

<div class="nav_column product-facet js-product-facet" id="product-facet">
	<div class="facet js-facet">
        <div class="facet-name js-facet-name"><spring:theme code="search.nav.categoryNav"/></div>

		<ycommerce:testId code="categoryNav_category_links">
			<div class="facet-values js-facet-values js-facet-form">
					<ul class="facet-list js-facet-list">
						<c:forEach items="${categories}" var="category">
							<li>
								<c:url value="${category.url}" var="categoryUrl"/>
                                <span class="facet-text">
									<a href="${categoryUrl}">${category.name}</a>&nbsp;
                                        <span class="facet-value-count"></span>

								</span>
							</li>
						</c:forEach>
					</ul>
			</div>
		</ycommerce:testId>
	</div>
</div>
