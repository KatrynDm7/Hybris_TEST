ACC.b2ctelcocheckoutcartdetails = {

	bindAll: function ()
	{
		this.bindCheckCartDetails();
	},

	bindCheckCartDetails: function ()
	{
		if ($("#b2ctelcocheckout-cart-details-btn").length) {
			$("#checkout-cart-details").hide();
	
				$("#b2ctelcocheckout-cart-details-btn").click(function (e)
				{
					e.preventDefault();
					$("#checkout-cart-details").toggle();
					if ($("#checkout-cart-details").is(":visible"))
					{
						$("#b2ctelcocheckout-cart-details-btn").html('[-] Hide Order Details');
					}
					else
					{
						$("#b2ctelcocheckout-cart-details-btn").html('[+] View Order Details');
					}
				});
			}
	}
};

$(document).ready(function ()
{
	ACC.b2ctelcocheckoutcartdetails.bindAll();
});
