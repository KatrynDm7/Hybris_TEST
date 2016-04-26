<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>



<script id="cartTotalsTemplate" type="text/x-jquery-tmpl">


<table id="orderTotals">
	<thead>
		<tr>
			<td><spring:theme code="order.order.totals" /></td>
			<td></td>
		</tr>
	</thead>
	<tfoot>
		<tr>
			<td><spring:theme code="basket.page.totals.total"/></td>
			{{if net}}
				<td>{{= totalPriceWithTax.formattedValue}}</td>
			{{else}}
				<td>{{= totalPrice.formattedValue}}</td>
			{{/if}}
		</tr>
	</tfoot>
	<tbody>
		<tr>
			<td><spring:theme code="basket.page.totals.subtotal"/></td>
			<td>{{= subTotal.formattedValue}}</td>
		</tr>
        {{if totalDiscounts && totalDiscounts.value > 0}}
			<tr class="savings">
				<td><spring:theme code="basket.page.totals.savings"/></td>
				<td>{{= totalDiscounts.formattedValue}}</td>
			</tr>
        {{/if}}
				
		{{if deliveryCost}}
			<tr>
				<td><spring:theme code="basket.page.totals.delivery"/></td>
				<td>
					{{if deliveryCost.value > 0}}
						{{= deliveryCost.formattedValue}}
					{{else}}
						<spring:theme code="basket.page.free"/>
					{{/if}}
				</td>
			</tr>
			
		{{/if}}
		
		
		{{if net && totalTax.value > 0 }}
			<tr>
				<td><spring:theme code="basket.page.totals.netTax"/></td>
				<td>{{= totalTax.formattedValue}}</td>
			</tr>
		{{/if}}
		
		

	</tbody>
	
</table>
	
	{{if !net}}
		<div class="realTotals"><p><spring:theme code="basket.page.totals.grossTax" arguments="{{= totalTax.formattedValue}}" argumentSeparator="!!!!" /></p></div>
	{{/if}}
	{{if net && totalTax.value <= 0}}
		<div class="realTotals"><p><spring:theme code="basket.page.totals.noNetTax" /></p></div>
	{{/if}}

</script>

<div id="ajaxCart">
</div>
