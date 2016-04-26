
ACC.checkoutaddress = {

	spinner: $("<img src='" + ACC.config.commonResourcePath + "/images/spinner.gif' />"),
	addressID: '',

    showAddressBook: function ()
	{
		$(document).on("click", "#viewAddressBook", function ()
		{
			var data = $("#savedAddressListHolder").html();
			$.colorbox({
                width: 470,
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
			var addressId = $(this).data("addressId"),
                popupId = "#popup_confirm_address_removal_" + addressId,
                idWrapper = $(this).parents('.addressItem').next();
			$.colorbox({
				inline: true,
                width: "500px",
				height: false,
				href: popupId,
				onComplete: function ()
				{
					$(this).colorbox.resize();
				},
                onCleanup:  function(){
                    $("#cboxClose").unbind( 'keyup', false );
                    idWrapper.empty();
                    $(popupId).appendTo(idWrapper);
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


