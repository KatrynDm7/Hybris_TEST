ACC.cart = {

	_autoload: [
		"bindHelp",
		"cartRestoration"
	],

	bindHelp: function(){
		$(document).on("click",".js-cart-help",function(e){
			e.preventDefault();
			var title = $(this).data("help");
			ACC.colorbox.open(title,{
				html:$(".js-help-popup-content").html(),
				width:"300px"
			});
		})
	},

	cartRestoration: function(){
		$('.cartRestoration').click(function(){
			var sCartUrl = $(this).data("cartUrl");
			window.location = sCartUrl;
		});
	}

};