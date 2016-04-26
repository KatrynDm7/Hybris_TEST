ACC.cart = {

    submitBundleRemove: function(id) {
    	$("a").attr("href", "javascript:void(0);")
        $('#deleteBundleForm'+id).get(0).submit();
    },
    
	submitRemove: function(id) {
    	$("a").attr("href", "javascript:void(0);")
		var productCode = $('#updateCartForm'+id).get(0).productCode.value;
		var initialCartQuantity = $('#updateCartForm'+id).get(0).initialQuantity.value;
		
		if(window.trackRemoveFromCart) {
			trackRemoveFromCart(productCode, initialCartQuantity);
		}

		$('#updateCartForm'+id+' input[name=quantity]').val(0);
		$('#updateCartForm'+id+' input[name=initialQuantity]').val(0);
		console.log($('#updateCartForm'+id+' input[name=quantity]').val());
		$('#updateCartForm'+id).submit();
	},
	
	submitUpdate: function (id) {
    	$("a").attr("href", "javascript:void(0);")
		var productCode = $('#updateCartForm'+id).get(0).productCode.value;
		var initialCartQuantity = $('#updateCartForm'+id).get(0).initialQuantity.value;
		var newCartQuantity = $('#updateCartForm'+id).get(0).quantity.value;
		
		if(window.trackUpdateCart) {
			trackUpdateCart(productCode, initialCartQuantity, newCartQuantity);
		}
		
		$('#updateCartForm'+id).get(0).submit();
	},

    showCartError: function () {
        $.colorbox({
            html: $("#cart_error_pop").html(),
            width:"550px",
            height:"265px"
        });
    }
};

$(document).ready(function(){
    /* Enable 'Add to cart' buttons after the whole page is loaded.
     * These buttons are initially disabled to prevent them from being clicked during page loading.
     * It causes redirection to json result instead of html */
    $('button.delayed-button[type=submit]').removeClass("disabled").enable(true);
});
