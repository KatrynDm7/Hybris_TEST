ACC.order = {

	_autoload: [
	    "backToOrderHistory",
	    "backToOrders"
	],

	backToOrderHistory: function(){
		$(".orderBackBtn").on("click", function(){
			var sUrl = $(this).data("backToOrders");
			window.location = sUrl;
		});
	},
	backToOrders: function(){
		$(".orderTopBackBtn").on("click", function(){
			var sUrl = $(this).data("backToOrders");
			window.location = sUrl;
		});
	}
};