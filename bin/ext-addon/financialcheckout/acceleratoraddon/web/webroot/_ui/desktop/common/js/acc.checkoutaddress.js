
ACC.checkoutaddress = {

	spinner: $("<img src='" + ACC.config.commonResourcePath + "/images/spinner.gif' />"),
	addressID: '',

	showAddressBook: function ()
	{
		$(document).on("click", "#viewAddressBook", function ()
		{
			var data = $("#savedAddressListHolder").html();
			$.colorbox({

				height: false,
				html: data,
				onComplete: function ()
				{

					$(this).colorbox.resize();
				}
			});

		})
	},

	showRemoveAddressConfirmation: function ()
	{
		$(document).on("click", ".removeAddressButton", function ()
		{
			var addressId = $(this).data("addressId");
            var popupWrapper = $(this).parents('.addressItem').next();
			$.colorbox({
				inline: true,
				height: false,
				href: "#popup_confirm_address_removal_" + addressId,
				onComplete: function ()
				{

					$(this).colorbox.resize();
				},
                onCleanup: function (){

                    popupWrapper.empty();
                    $("#popup_confirm_address_removal_" + addressId).appendTo(popupWrapper);
                }
			});

		})
	}
}

// Address Verification
$(document).ready(function ()
{
	with (ACC.checkoutaddress)
	{

		showAddressBook();
		showRemoveAddressConfirmation();
	}
});


