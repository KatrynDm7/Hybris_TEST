<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%--<c:if test="${not empty piwikSiteId}">--%>
<!-- Piwik -->
<script type="text/javascript">
	var analyticsConfig;
	if(typeof ACC != 'undefined'){
		analyticsConfig = ACC.addons.hybrisanalyticsaddon;
	}else{
		analyticsConfig = ACCMOB.addons.hybrisanalyticsaddon;
	}
	var piwikTrackerUrl = "${PIWIK_TRACKER_ENDPOINT_HTTPS_URL}"; // Use Piwik HTTPS Endpoint by default
	var piwikSiteId = "${piwikSiteId}";
	
	if(location.protocol != 'https:')	// if the current page does not use https protocol, use the normal HTTP endpoint: 
	{
		piwikTrackerUrl = "${PIWIK_TRACKER_ENDPOINT_URL}";
	}

	var _paq = _paq || [];

	_paq.push(['setTrackerUrl', piwikTrackerUrl]);
	_paq.push(['setSiteId', piwikSiteId]);
	// Hybris Analytics specifics - START
	_paq.push(['setRequestMethod', 'POST']);
	_paq.push(['setRequestContentType', 'application/json; charset=UTF-8']);
	_paq.push(['setCustomRequestProcessing', function (request) {
		try {
			var pairs = request.split('&');

			var requestParametersArray = {};
			for (index = 0; index < pairs.length; ++index) {
				var pair = pairs[index].split('=');
				requestParametersArray[pair[0]] = decodeURIComponent(pair[1] || '');
			}

			return JSON.stringify(requestParametersArray);
		} catch (err) {
			return request;
		}
	}]);

	var hybrisAnalyticsPiwikPlugin = (function() {
		function _getEventtypeParam(suffix) { return '&eventtype=' + suffix; }
		function ecommerce() { return _getEventtypeParam("ecommerce"); }
		function event() { return _getEventtypeParam("event"); }
		function goal() { return _getEventtypeParam("goal"); }
		function link() { return _getEventtypeParam("link"); }
		function load() { return _getEventtypeParam("load"); }
		function log() { return _getEventtypeParam("log"); }
		function ping() { return _getEventtypeParam("ping"); }
		function run() { return _getEventtypeParam("run"); }
		function sitesearch() { return _getEventtypeParam("sitesearch"); }
		function unload() { return _getEventtypeParam("unload"); }

		return {
			ecommerce: ecommerce,
			event : event,
			goal : goal,
			link : link,
			load : load,
			log : log,
			ping: ping,
			sitesearch : sitesearch,
			unload : unload
		};
	})();
	_paq.push(['addPlugin', 'hybrisAnalyticsPiwikPlugin', hybrisAnalyticsPiwikPlugin]);
	// Hybris Analytics specifics - END


	<c:choose>
		<c:when test="${pageType == 'PRODUCT'}">
			//View Product event
			<c:set var="categories" value="" />
				<c:forEach items="${product.categories}" var="category">
				  <c:set var="categories">${categories},'${category.code}'</c:set>
				</c:forEach>
				_paq.push(['setEcommerceView',
					'${product.code}',  // (required) SKU: Product unique identifier
					'${product.name}',  // (optional) Product name
					[${fn:substringAfter(categories, ',')}],  // (optional) Product category, or array of up to 5 categories
					'${product.price.value}'  // (optional) Product Price as displayed on the page
				]);
				_paq.push(['trackPageView','ViewProduct']);  //Do we really need this ??
		</c:when>



		<c:when test="${pageType == 'CATEGORY'}">
		//View category - Start
			<c:choose>
				<c:when test="${searchPageData.pagination.totalNumberOfResults > 0}">
					<c:if test="${not empty breadcrumbs}">
						_paq.push(['trackSiteSearch',
							'${searchPageData.freeTextSearch}',  // Search keyword searched for
							'${categoryCode}:${categoryName}',  // Search category selected in your search engine. If you do not need this, set to false
							'${searchPageData.pagination.totalNumberOfResults}',// Number of results on the Search results page. Zero indicates a 'No Result Search Keyword'. Set to false if you don't know
							_paq.push(['setCustomData',{'categoryName':'${categoryName}'}])
						]);
					</c:if>
				</c:when>
				<c:otherwise>
						_paq.push(['trackSiteSearch',
							'${searchPageData.freeTextSearch}',  // Search keyword searched for
							false,  // Search category selected in your search engine. If you do not need this, set to false
							'0'  // Number of results on the Search results page. Zero indicates a 'No Result Search Keyword'. Set to false if you don't know
						]);
				</c:otherwise>
			</c:choose>
		</c:when>



		<c:when test="${pageType == 'PRODUCTSEARCH'}">
		//View Product search - START
			<c:choose>
				<c:when test="${searchPageData.pagination.totalNumberOfResults > 0}">
					<c:if test="${not empty breadcrumbs}">
						<c:set var="categories" value="" />
						<c:forEach items="${breadcrumbs}" var="breadcrumb">
							<c:set var="categories">${categories},'${breadcrumb.name}'</c:set>
						</c:forEach>
						_paq.push(['trackSiteSearch',
							'${searchPageData.freeTextSearch}',  // Search keyword searched for
							[${fn:substringAfter(categories, ',')}],  // Search category selected in your search engine. If you do not need this, set to false
							'${searchPageData.pagination.totalNumberOfResults}'  // Number of results on the Search results page. Zero indicates a 'No Result Search Keyword'. Set to false if you don't know
						]);
					</c:if>
				</c:when>
				<c:otherwise>
					_paq.push(['trackSiteSearch',
						'${searchPageData.freeTextSearch}',  // Search keyword searched for
						false,  // Search category selected in your search engine. If you do not need this, set to false
						'0'  // Number of results on the Search results page. Zero indicates a 'No Result Search Keyword'. Set to false if you don't know
					]);
				</c:otherwise>
			</c:choose>
		</c:when>


		<c:when test="${pageType == 'ORDERCONFIRMATION'}">
			<c:forEach items="${orderData.entries}" var="entry">
				_paq.push(['setCustomVariable',1,"ec_id","${orderData.code}","page"]);
				<c:forEach items="${entry.product.categories}" var="category">
					<c:set var="categories">${categories},'${category.code}'</c:set>
				</c:forEach>
				_paq.push(['setEcommerceView',
					'${entry.product.code}',  // (required) SKU: Product unique identifier
					'${entry.product.name}',  // (optional) Product name
					[${fn:substringAfter(categories, ',')}],  // (optional) Product category. You can also specify an array of up to 5 categories eg. ["Books", "New releases", "Biography"]
					'${entry.product.price.value}'  // (recommended) Product price
				]);
				_paq.push(['addEcommerceItem',
					'${entry.product.code}',  // (required) SKU: Product unique identifier
					'${entry.product.name}', // (optional) Product name
					[${fn:substringAfter(categories, ',')}],  // (optional) Product category. You can also specify an array of up to 5 categories eg. ["Books", "New releases", "Biography"]
					'${entry.product.price.value}',  // (recommended) Product price
					'${entry.quantity}'.toString()  // (optional, default to 1) Product quantity
				]);
			</c:forEach>
			_paq.push(['trackEcommerceOrder',
				'${orderData.code}',  // (required) Unique Order ID
				'${orderData.totalPrice.value}',  // (required) Order Revenue grand total (includes tax, shipping, and subtracted discount)
				'${orderData.totalPrice.value - orderData.deliveryCost.value}',  // (optional) Order sub total (excludes shipping)
				'${orderData.totalTax.value}',  // (optional) Tax amount
				'${orderData.deliveryCost.value}',  // (optional) Shipping amount
				false  // (optional) Discount offered (set to false for unspecified parameter)
			]);
			
			_paq.push(['setCustomVariable',1,"ec_id","${orderData.code}","page"]);
			_paq.push(['trackEvent', 'checkout', 'success', '','']);
			_paq.push(['trackPageView']);  // we recommend to leave the call to trackPageView() on the Order confirmation page
		</c:when>


		<c:otherwise>
			//Otherwise default to page view event
			_paq.push(['trackPageView','ViewPage']);
		</c:otherwise>
	</c:choose>


	_paq.push(['enableLinkTracking']);
	// handlers and their subscription for cart events


	function trackAddToCart_piwik(productCode, quantityAdded, cartData) {
		_paq.push(['setEcommerceView',
			cartData.productCode,  // (required) SKU: Product unique identifier
			cartData.productName,  // (optional) Product name
			[ ], // (optional) Product category, string or array of up to 5 categories
			quantityAdded+""  // (optional, default to 1) Product quantity
		]);
		_paq.push(['addEcommerceItem',
			cartData.productCode,  // (required) SKU: Product unique identifier
			cartData.productName,  // (optional) Product name
			[ ], // (optional) Product category, string or array of up to 5 categories
			cartData.productPrice+"", // (recommended) Product price
			quantityAdded+""  // (optional, default to 1) Product quantity
		]);
		
		if (!cartData.cartCode)
		{
			cartData.cartCode="${cartData.code}";
		}
		
		_paq.push(['setCustomVariable',1,"ec_id",cartData.cartCode,"page"]);

		_paq.push(['trackEcommerceCartUpdate',
			'0'  // (required) Cart amount
		]);
		//_paq.push(['trackPageView']); // does really need to do this?
	}

	function trackBannerClick_piwik(url) {
		_paq.push(['setCustomVariable',1,"bannerid",url,"page"]);
		_paq.push(['trackLink', url, 'banner']);
	}

	function trackContinueCheckoutClick_piwik() {
		_paq.push(['setCustomVariable',1,"ec_id","${cartData.code}","page"]);
		_paq.push(['trackEvent', 'checkout', 'proceed', '','']);
	}

	function trackUpdateCart_piwik(productCode,initialQuantity,newQuantity,cartData) {
		if (initialQuantity != newQuantity) {
			trackAddToCart_piwik(productCode, newQuantity,cartData);
		}
	}

	function trackRemoveFromCart_piwik(productCode, initialQuantity,cartData) {
		trackAddToCart_piwik(productCode, 0, cartData);
	}

	window.mediator.subscribe('trackAddToCart', function(data) {
		if (data.productCode && data.quantity) {
			trackAddToCart_piwik(data.productCode, data.quantity,data.cartData);
		}
	});

	window.mediator.subscribe('trackUpdateCart', function(data) {
		if (data.productCode && data.initialCartQuantity && data.newCartQuantity) {
			trackUpdateCart_piwik(data.productCode,data.initialCartQuantity,data.newCartQuantity,data);
		}
	});

	window.mediator.subscribe('trackRemoveFromCart', function(data) {
		if (data.productCode && data.initialCartQuantity) {
			trackRemoveFromCart_piwik(data.productCode, data.initialCartQuantity,data);
		}
	});
	
	$('.simple-banner').click(function(){
 		trackBannerClick_piwik( $(this).find("a").prop('href'));
	});

	$('.continueCheckout').click(function(){
		trackContinueCheckoutClick_piwik();
	});

</script>

<noscript><p><img src="${piwikTrackerUrl}?idsite=${piwikSiteId}" style="border:0;" alt="" /></p></noscript>

